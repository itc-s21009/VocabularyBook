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

data class Translation(
    val word_id: Int,
    val id: Int,
    val mean: String,
    val language: Int
)

data class Language (
    val id: Int,
    val name: String
)