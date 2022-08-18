package jp.ac.it_college.std.pbl_b.vocabularybook

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.ArrayAdapter
import jp.ac.it_college.std.pbl_b.vocabularybook.databinding.ActivityWordSearchBinding

class WordSearchActivity : AppCompatActivity() {
    private lateinit var binding: ActivityWordSearchBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWordSearchBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val wordNameList = intent.getStringArrayExtra("WORD_LIST") ?: arrayOf()
        val wordIdList = intent.getIntArrayExtra("WORD_ID_LIST") ?: intArrayOf()
        val wordList = wordIdList.zip(wordNameList).map { DBWord(0, it.first, it.second) }
        binding.edWord.setOnKeyListener { _, _, _ ->
            drawResult(wordList)
            false
        }
        binding.btWordSearch.setOnClickListener {
            drawResult(wordList)
        }
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return if (item.itemId == android.R.id.home) {
            finish()
            true
        } else {
            super.onOptionsItemSelected(item)
        }
    }

    private fun drawResult(wordList: List<DBWord>) {
        val searchWord = binding.edWord.text.toString()
        binding.lvSearchResult.adapter = ArrayAdapter(
            this@WordSearchActivity,
            R.layout.word_row,
            wordList.map{it.word}.filter { it.contains(searchWord) }
        )
        binding.lvSearchResult.setOnItemClickListener { _, _, position, _ ->
            showWordDetailsActivity(wordList[position])
        }
    }

    private fun showWordDetailsActivity(word: DBWord) {
        val intent = Intent(this, WordDetailsActivity::class.java)
        intent.putExtra("WORD_ID", word.id.toLong())
        intent.putExtra("WORD_NAME", word.word)
        startActivity(intent)
    }
}