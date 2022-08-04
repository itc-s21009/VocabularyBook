package jp.ac.it_college.std.pbl_b.vocabularybook

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log

class DatabaseHelper(context: Context): SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    companion object {
        private const val DATABASE_NAME = "20220804.db"
        private const val DATABASE_VERSION = 1
    }

    override fun onCreate(db: SQLiteDatabase?) {
        var sql = """
            |CREATE TABLE cate_table (
            |  cate_number INTEGER PRIMARY KEY,
            |  cate_name CHAR(32)
            |)
        """.trimMargin()
        db?.execSQL(sql)

        sql = """
            |CREATE TABLE word_table (
            |  cate_number INTEGER,
            |  word_id INTEGER PRIMARY KEY,
            |  word CHAR(32)
            |)
        """.trimMargin()
        db?.execSQL(sql)

        sql = """
            |CREATE TABLE translation_table (
            |  word_id INTEGER,
            |  language_id INTEGER PRIMARY KEY,
            |  language_mean CHAR(32),
            |  language INTEGER
            |)
        """.trimMargin()
        db?.execSQL(sql)

        sql = """
            |CREATE TABLE language_table (
            |  language_id INTEGER PRIMARY KEY,
            |  language_name CHAR(32)
            |)
        """.trimMargin()
        db?.execSQL(sql)
        Log.i("db", "onCreate called.")
    }

    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {
    }

    fun getCategoryList() : List<Category> {
        val list = mutableListOf<Category>()
        val sql = "SELECT * FROM cate_table"
        val cursor = readableDatabase.rawQuery(sql, null)
        while (cursor.moveToNext()) {
            val idxId = cursor.getColumnIndexOrThrow("id")
            val idxName = cursor.getColumnIndexOrThrow("name")
            val id = cursor.getInt(idxId)
            val name = cursor.getString(idxName)
            val categoryModel = Category(id, name)
            list.add(categoryModel)
        }
        cursor.close()
        return list
    }

    fun getWordsList(cate_id: Int): List<Word> {
        val list = mutableListOf<Word>()
        val sql = "SELECT * FROM word_table WHERE cate_number = $cate_id"
        val cursor = readableDatabase.rawQuery(sql, null)
        while (cursor.moveToNext()) {
            val idxWordId = cursor.getColumnIndexOrThrow("word_id")
            val idxWord = cursor.getColumnIndexOrThrow("word")
            val wordId = cursor.getInt(idxWordId)
            val word = cursor.getString(idxWord)
            val wordModel = Word(cate_id, wordId, word)
            list.add(wordModel)
        }
        cursor.close()
        return list
    }
}