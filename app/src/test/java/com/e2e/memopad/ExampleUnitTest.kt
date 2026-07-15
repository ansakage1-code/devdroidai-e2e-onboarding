package com.e2e.memopad

import com.e2e.memopad.domain.Memo
import com.e2e.memopad.domain.MemoLogic
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * MemoLogic（追加・削除の純ロジック）の unit test。
 * Android 非依存なので開発マシン上で実行される。
 */
class ExampleUnitTest {

    @Test
    fun add_appendsTrimmedMemo() {
        val result = MemoLogic.add(emptyList(), "  買い物  ", id = 1L, createdAt = 1000L)
        assertEquals(1, result.size)
        assertEquals("買い物", result[0].text)
        assertEquals(1L, result[0].id)
    }

    @Test
    fun add_ignoresBlankInput() {
        val result = MemoLogic.add(emptyList(), "   ", id = 1L, createdAt = 1000L)
        assertTrue(result.isEmpty())
    }

    @Test
    fun add_isImmutable() {
        val original = listOf(Memo(1L, "a", createdAt = 1000L))
        val result = MemoLogic.add(original, "b", id = 2L, createdAt = 2000L)
        assertEquals(1, original.size) // 元のリストは不変
        assertEquals(2, result.size)
    }

    @Test
    fun remove_dropsMatchingId() {
        val memos = listOf(Memo(1L, "a", createdAt = 1000L), Memo(2L, "b", createdAt = 2000L))
        val result = MemoLogic.remove(memos, id = 1L)
        assertEquals(1, result.size)
        assertEquals(2L, result[0].id)
    }

    @Test
    fun remove_noMatchKeepsAll() {
        val memos = listOf(Memo(1L, "a", createdAt = 1000L))
        val result = MemoLogic.remove(memos, id = 99L)
        assertEquals(1, result.size)
    }

    @Test
    fun update_replacesTextForMatchingId() {
        val memos = listOf(
            Memo(1L, "買い物", createdAt = 1000L),
            Memo(2L, "読書", createdAt = 2000L)
        )
        val result = MemoLogic.update(memos, id = 1L, "  掃除  ")
        assertEquals(2, result.size)
        assertEquals("掃除", result[0].text) // id=1 のテキストが更新される
        assertEquals(1L, result[0].id)
        assertEquals("読書", result[1].text) // id=2 は変わらず
    }

    @Test
    fun update_ignoresBlankInput() {
        val memos = listOf(Memo(1L, "買い物", createdAt = 1000L))
        val result = MemoLogic.update(memos, id = 1L, "   ")
        assertEquals(1, result.size)
        assertEquals("買い物", result[0].text) // 空白のみなら更新されない
    }

    @Test
    fun update_noMatchKeepsAll() {
        val memos = listOf(Memo(1L, "買い物", createdAt = 1000L))
        val result = MemoLogic.update(memos, id = 99L, "新しいテキスト")
        assertEquals(1, result.size)
        assertEquals("買い物", result[0].text) // id が見つからなければ変わらず
    }

    @Test
    fun update_isImmutable() {
        val original = listOf(Memo(1L, "a", createdAt = 1000L))
        val result = MemoLogic.update(original, id = 1L, "b")
        assertEquals("a", original[0].text) // 元のリストは不変
        assertEquals("b", result[0].text)
    }

    @Test
    fun sortByCreatedAtDesc_sortsInDescendingOrder() {
        val memos = listOf(
            Memo(1L, "最初", createdAt = 1000L),
            Memo(2L, "2番目", createdAt = 3000L),
            Memo(3L, "3番目", createdAt = 2000L)
        )
        val result = MemoLogic.sortByCreatedAtDesc(memos)
        assertEquals(3, result.size)
        assertEquals(3000L, result[0].createdAt) // 最も新しい順
        assertEquals(2000L, result[1].createdAt)
        assertEquals(1000L, result[2].createdAt) // 最も古い順
    }

    @Test
    fun sortByCreatedAtDesc_emptyListReturnsEmpty() {
        val result = MemoLogic.sortByCreatedAtDesc(emptyList())
        assertTrue(result.isEmpty())
    }

    @Test
    fun sortByCreatedAtDesc_singleItemReturnsSingleItem() {
        val memos = listOf(Memo(1L, "テスト", createdAt = 1000L))
        val result = MemoLogic.sortByCreatedAtDesc(memos)
        assertEquals(1, result.size)
        assertEquals(1000L, result[0].createdAt)
    }

    @Test
    fun sortByCreatedAtDesc_isImmutable() {
        val original = listOf(
            Memo(1L, "a", createdAt = 1000L),
            Memo(2L, "b", createdAt = 2000L)
        )
        val result = MemoLogic.sortByCreatedAtDesc(original)
        assertEquals(2000L, original[0].createdAt) // 元のリストは並び替わっていない
        assertEquals(1000L, original[1].createdAt)
        assertEquals(2000L, result[0].createdAt) // 返されたリストは並び替わっている
        assertEquals(1000L, result[1].createdAt)
    }
}
