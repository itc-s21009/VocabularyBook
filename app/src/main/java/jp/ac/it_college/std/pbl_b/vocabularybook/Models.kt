package jp.ac.it_college.std.pbl_b.vocabularybook

data class Category(
    val id: Int,
    val name: String
)

data class Word(
    val cate_id: Int,
    val id: Int,
    val word: String
)