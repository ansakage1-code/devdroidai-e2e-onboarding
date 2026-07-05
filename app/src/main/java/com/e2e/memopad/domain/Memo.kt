package com.e2e.memopad.domain

/**
 * 1 件のメモ。
 *
 * @param id   一意の識別子（削除時の対象特定に使う）
 * @param text メモ本文
 * @param createdAt メモの作成日時（ミリ秒単位）
 */
data class Memo(
    val id: Long,
    val text: String,
    val createdAt: Long = System.currentTimeMillis(),
)

/**
 * メモの追加・削除を行う純ロジック。
 *
 * Android / Compose に依存しないため、そのまま unit test できる。
 * すべて immutable（受け取ったリストは変更せず、新しいリストを返す）。
 */
object MemoLogic {

    /**
     * 末尾に 1 件追加した新しいリストを返す。
     * 空白のみ（trim 後に空）の text は追加しない（元のリストをそのまま返す）。
     */
    fun add(memos: List<Memo>, text: String, id: Long): List<Memo> {
        val trimmed = text.trim()
        if (trimmed.isEmpty()) return memos
        return memos + Memo(id = id, text = trimmed)
    }

    /**
     * 指定 id のメモを除いた新しいリストを返す。
     * 該当が無ければ元と同じ内容のリストを返す。
     */
    fun remove(memos: List<Memo>, id: Long): List<Memo> {
        return memos.filterNot { it.id == id }
    }
}
