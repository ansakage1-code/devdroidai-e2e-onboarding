package com.e2e.memopad.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.e2e.memopad.R
import com.e2e.memopad.domain.Memo
import com.e2e.memopad.ui.theme.LightGreen
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * メモ帳の画面。入力欄 + 追加ボタン + 検索ボックス + メモ一覧。
 *
 * ローカル状態として editingMemoId, editText, searchQuery を持ち、
 * 編集ダイアログの表示/非表示、入力内容、検索キーワードを管理します。
 *
 * グローバル状態（memos, input）は呼び出し側（MainActivity）から受け取ります。
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MemoScreen(
    memos: List<Memo>,
    input: String,
    onInputChange: (String) -> Unit,
    onAdd: () -> Unit,
    onDelete: (Long) -> Unit,
    onEdit: (id: Long, newText: String) -> Unit,
    modifier: Modifier = Modifier,
) {
    // ローカル状態：編集中のメモID（null なら非表示）
    var editingMemoId by remember { mutableStateOf<Long?>(null) }
    // ローカル状態：編集テキスト
    var editText by remember { mutableStateOf("") }
    // ローカル状態：検索キーワード
    var searchQuery by remember { mutableStateOf("") }

    // 検索キーワードでメモをフィルタリング
    val filteredMemos = if (searchQuery.isEmpty()) {
        memos
    } else {
        memos.filter { it.text.contains(searchQuery, ignoreCase = true) }
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = { 
            TopAppBar(
                title = { 
                    Text("${stringResource(R.string.app_name)} (${filteredMemos.size}件)")
                }
            )
        },
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                OutlinedTextField(
                    value = input,
                    onValueChange = onInputChange,
                    modifier = Modifier.weight(1f),
                    singleLine = true,
                    label = { Text(stringResource(R.string.input_hint)) },
                )
                Spacer(modifier = Modifier.width(8.dp))
                Button(onClick = onAdd, enabled = input.isNotBlank()) {
                    Text(stringResource(R.string.add_button))
                }
            }
            Spacer(modifier = Modifier.height(12.dp))

            // 検索ボックス
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                label = { Text("メモを検索") },
            )
            Spacer(modifier = Modifier.height(12.dp))

            if (filteredMemos.isEmpty()) {
                Text(
                    text = if (searchQuery.isEmpty()) {
                        stringResource(R.string.empty_hint)
                    } else {
                        "検索結果がありません"
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 32.dp),
                )
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    contentPadding = PaddingValues(top = 12.dp),
                ) {
                    items(filteredMemos.sortedByDescending { it.createdAt }, key = { it.id }) { memo ->
                        MemoRow(
                            memo = memo,
                            onDelete = { onDelete(memo.id) },
                            onEdit = { text ->
                                editingMemoId = memo.id
                                editText = text
                            },
                        )
                    }
                }
            }
        }

        // 編集ダイアログ
        if (editingMemoId != null) {
            EditMemoDialog(
                text = editText,
                onTextChange = { editText = it },
                onSave = {
                    onEdit(editingMemoId!!, editText)
                    editingMemoId = null
                    editText = ""
                },
                onCancel = {
                    editingMemoId = null
                    editText = ""
                },
            )
        }
    }
}

@Composable
private fun MemoRow(
    memo: Memo,
    onDelete: () -> Unit,
    onEdit: (String) -> Unit,
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onEdit(memo.text) },
        colors = CardDefaults.cardColors(containerColor = LightGreen),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = memo.text,
                    modifier = Modifier.padding(vertical = 8.dp),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                )
                Text(
                    text = SimpleDateFormat("yyyy/MM/dd HH:mm", Locale.getDefault()).format(Date(memo.createdAt)),
                    modifier = Modifier.padding(bottom = 8.dp),
                    style = MaterialTheme.typography.labelSmall,
                    color = Color.Gray,
                )
            }
            TextButton(onClick = onDelete) {
                Text(stringResource(R.string.delete_button))
            }
        }
    }
}

@Composable
private fun EditMemoDialog(
    text: String,
    onTextChange: (String) -> Unit,
    onSave: () -> Unit,
    onCancel: () -> Unit,
) {
    AlertDialog(
        onDismissRequest = onCancel,
        title = { Text(stringResource(R.string.edit_title)) },
        text = {
            OutlinedTextField(
                value = text,
                onValueChange = onTextChange,
                modifier = Modifier.fillMaxWidth(),
                singleLine = false,
                label = { Text(stringResource(R.string.edit_hint)) },
            )
        },
        confirmButton = {
            Button(onClick = onSave, enabled = text.isNotBlank()) {
                Text(stringResource(R.string.save_button))
            }
        },
        dismissButton = {
            TextButton(onClick = onCancel) {
                Text(stringResource(R.string.cancel_button))
            }
        },
    )
}
