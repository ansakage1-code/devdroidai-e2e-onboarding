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
}
