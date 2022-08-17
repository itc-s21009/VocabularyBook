package jp.ac.it_college.std.pbl_b.vocabularybook

data class DBCategory(
    val id: Int,
    val name: String
)

data class DBWord(
    val cate_id: Int,
    val id: Int,
    val word: String
)

data class DBTranslation(
    val word_id: Int,
    val id: Int,
    val mean: String,
    val language: Int
)

data class DBLanguage (
    val id: Int,
    val name: String
)

data class TranslatedWord(
    val word_id: Int,
    val word: String,
    val mean: String,
    val language: String
)