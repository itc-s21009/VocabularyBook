package jp.ac.it_college.std.pbl_b.vocabularybook

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log

class DatabaseHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    companion object {
        private const val DATABASE_NAME = "data.db"
        private const val DATABASE_VERSION = 1
    }

    override fun onCreate(db: SQLiteDatabase?) {
        var sql = """
            |CREATE TABLE cate_table (
            |  cate_number INTEGER PRIMARY KEY AUTOINCREMENT,
            |  cate_name CHAR(32)
            |)
        """.trimMargin()
        db?.execSQL(sql)

        sql = """
            |CREATE TABLE word_table (
            |  cate_number INTEGER,
            |  word_id INTEGER PRIMARY KEY AUTOINCREMENT,
            |  word CHAR(32)
            |)
        """.trimMargin()
        db?.execSQL(sql)

        sql = """
            |CREATE TABLE translation_table (
            |  word_id INTEGER,
            |  language_id INTEGER PRIMARY KEY AUTOINCREMENT,
            |  language_mean CHAR(32),
            |  language INTEGER
            |)
        """.trimMargin()
        db?.execSQL(sql)

        sql = """
            |CREATE TABLE language_table (
            |  language_id INTEGER PRIMARY KEY AUTOINCREMENT,
            |  language_name CHAR(32)
            |)
        """.trimMargin()
        db?.execSQL(sql)
        Log.i("db", "onCreate called.")
    }

    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {
    }

    fun getCategoryList(): List<DBCategory> {
        val list = mutableListOf<DBCategory>()
        val sql = "SELECT * FROM cate_table"
        val cursor = readableDatabase.rawQuery(sql, null)
        while (cursor.moveToNext()) {
            val idxId = cursor.getColumnIndexOrThrow("cate_number")
            val idxName = cursor.getColumnIndexOrThrow("cate_name")
            val id = cursor.getInt(idxId)
            val name = cursor.getString(idxName)
            val categoryModel = DBCategory(id, name)
            list.add(categoryModel)
        }
        cursor.close()
        return list
    }

    fun getWordsList(cate_id: Int): List<DBWord> {
        val list = mutableListOf<DBWord>()
        val sql = "SELECT * FROM word_table WHERE cate_number = $cate_id"
        val cursor = readableDatabase.rawQuery(sql, null)
        while (cursor.moveToNext()) {
            val idxWordId = cursor.getColumnIndexOrThrow("word_id")
            val idxWord = cursor.getColumnIndexOrThrow("word")
            val wordId = cursor.getInt(idxWordId)
            val word = cursor.getString(idxWord)
            val wordModel = DBWord(cate_id, wordId, word)
            list.add(wordModel)
        }
        cursor.close()
        return list
    }

    fun registerCategory(name: String) {
        val sql = "INSERT INTO cate_table (cate_name) VALUES (?)"
        val statement = writableDatabase.compileStatement(sql)
        statement.bindString(1, name)
        statement.executeInsert()
    }

    fun deleteCategory(cate_id: Long) {
        val sql = "DELETE FROM cate_table WHERE cate_number = ?"
        val statement = writableDatabase.compileStatement(sql)
        statement.bindLong(1, cate_id)
        statement.executeUpdateDelete()
    }

    fun findCategoryById(cate_id: Long): DBCategory? {
        val sql = "SELECT * FROM cate_table WHERE cate_number = $cate_id"
        val cursor = readableDatabase.rawQuery(sql, null)
        return if (cursor.moveToNext()) DBCategory(cursor.getInt(1), cursor.getString(2)) else null
    }

    fun registerWord(cate_id: Long, word: String) {
        val sql = "INSERT INTO word_table (cate_number, word) VALUES (?, ?)"
        val statement = writableDatabase.compileStatement(sql)
        statement.bindLong(1, cate_id)
        statement.bindString(2, word)
        statement.executeInsert()
    }

    fun deleteWord(word_id: Long) {
        val sql = "DELETE FROM word_table WHERE word_id = ?"
        val statement = writableDatabase.compileStatement(sql)
        statement.bindLong(1, word_id)
        statement.executeUpdateDelete()
    }

    fun findWordById(word_id: Long): DBWord? {
        val sql = "SELECT * FROM word_table WHERE word_id = $word_id"
        val cursor = readableDatabase.rawQuery(sql, null)
        return if (cursor.moveToNext())
            DBWord(
                cursor.getInt(1),
                cursor.getInt(2),
                cursor.getString(3)
            ) else null
    }

    fun registerTranslation(word_id: Long, mean: String, language: Long) {
        val sql = """
            |INSERT INTO translation_table (
            |  word_id, 
            |  language_mean,
            |  language
            |) VALUES (?, ?, ?)
        """.trimMargin()
        val statement = writableDatabase.compileStatement(sql)
        statement.bindLong(1, word_id)
        statement.bindString(2, mean)
        statement.bindLong(3, language)
        statement.executeInsert()
    }

    fun deleteTranslation(translation_id: Long) {
        val sql = "DELETE FROM translation_table WHERE language_id = ?"
        val statement = writableDatabase.compileStatement(sql)
        statement.bindLong(1, translation_id)
        statement.executeUpdateDelete()
    }

    fun findTranslationById(translation_id: Long): DBTranslation? {
        val sql = "SELECT * FROM translation_table WHERE language_id = $translation_id"
        val cursor = readableDatabase.rawQuery(sql, null)
        return if (cursor.moveToNext())
            DBTranslation(
                cursor.getInt(1),
                cursor.getInt(2),
                cursor.getString(3),
                cursor.getInt(4)
            ) else null
    }

    fun registerLanguage(language_name: String) {
        val sql = "INSERT INTO language_table (language_name) VALUES (?)"
        val statement = writableDatabase.compileStatement(sql)
        statement.bindString(1, language_name)
        statement.executeInsert()
    }

    fun deleteLanguage(language_id: Long) {
        val sql = "DELETE FROM language_table WHERE language_id = ?"
        val statement = writableDatabase.compileStatement(sql)
        statement.bindLong(1, language_id)
        statement.executeUpdateDelete()
    }

    fun findLanguageById(language_id: Long): DBLanguage? {
        val sql = "SELECT * FROM language_table WHERE language_id = $language_id"
        val cursor = readableDatabase.rawQuery(sql, null)
        return if (cursor.moveToNext())
            DBLanguage(
                cursor.getInt(1),
                cursor.getString(2)
            ) else null
    }

    fun getTranslatedWords(word_id: Long): List<TranslatedWord> {
        val sql = """
            |SELECT translation_table.language_id, word_table.word_id, word, language_mean, language_name
            | FROM word_table, translation_table, language_table
            | WHERE word_table.word_id = $word_id
            | AND translation_table.word_id = word_table.word_id
            | AND language_table.language_id = translation_table.language
            """.trimMargin()
        val cursor = readableDatabase.rawQuery(sql, null)
        val list = mutableListOf<TranslatedWord>()
        while (cursor.moveToNext()) {
            list.add(
                TranslatedWord(
                    cursor.getInt(0),
                    cursor.getInt(1),
                    cursor.getString(2),
                    cursor.getString(3),
                    cursor.getString(4)
                )
            )
        }
        return list
    }
}