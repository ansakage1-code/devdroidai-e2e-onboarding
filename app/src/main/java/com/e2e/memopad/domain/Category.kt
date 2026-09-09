package com.e2e.memopad.domain

/**
 * メモのカテゴリを表す enum。
 */
enum class Category(val displayName: String) {
    WORK("仕事"),
    PRIVATE("プライベート"),
    SHOPPING("買い物"),
    ;

    companion object {
        fun fromString(value: String?): Category {
            return values().find { it.name == value } ?: WORK
        }

        fun all(): List<Category> = values().toList()
    }
}
