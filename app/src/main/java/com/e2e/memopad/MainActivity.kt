package com.e2e.memopad

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.e2e.memopad.domain.Category
import com.e2e.memopad.domain.Memo
import com.e2e.memopad.domain.MemoLogic
import com.e2e.memopad.ui.MemoScreen
import com.e2e.memopad.ui.theme.MemopadTheme
import org.json.JSONArray
import org.json.JSONObject

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val store = MemoStore(applicationContext)

        setContent {
            MemopadTheme {
                var memos by remember { mutableStateOf(store.load()) }
                var input by remember { mutableStateOf("") }

                MemoScreen(
                    memos = memos,
                    input = input,
                    onInputChange = { input = it },
                    onAdd = {
                        memos = MemoLogic.add(memos, input, id = nextId(memos), createdAt = System.currentTimeMillis())
                        input = ""
                        store.save(memos)
                    },
                    onDelete = { id ->
                        memos = MemoLogic.remove(memos, id)
                        store.save(memos)
                    },
                    onEdit = { id, newText ->
                        memos = MemoLogic.update(memos, id, newText)
                        store.save(memos)
                    },
                )
            }
        }
    }

    /** リスト内の最大 id + 1（空なら 1）。同一リスト内で衝突しない単純採番。 */
    private fun nextId(memos: List<Memo>): Long =
        (memos.maxOfOrNull { it.id } ?: 0L) + 1L
}

/** メモを SharedPreferences に JSON 配列で永続化する簡易ストア。 */
private class MemoStore(context: Context) {
    private val prefs = context.getSharedPreferences("memopad", Context.MODE_PRIVATE)

    fun load(): List<Memo> {
        val raw = prefs.getString(KEY, null) ?: return emptyList()
        return runCatching {
            val arr = JSONArray(raw)
            (0 until arr.length()).map { i ->
                val o = arr.getJSONObject(i)
                Memo(
                    id = o.getLong("id"),
                    text = o.getString("text"),
                    createdAt = o.optLong("createdAt", 0L),
                    category = Category.fromString(o.optString("category", Category.WORK.name)),
                )
            }
        }.getOrDefault(emptyList())
    }

    fun save(memos: List<Memo>) {
        val arr = JSONArray()
        memos.forEach { m ->
            arr.put(
                JSONObject()
                    .put("id", m.id)
                    .put("text", m.text)
                    .put("createdAt", m.createdAt)
                    .put("category", m.category.name)
            )
        }
        prefs.edit().putString(KEY, arr.toString()).apply()
    }

    private companion object {
        const val KEY = "memos"
    }
}
