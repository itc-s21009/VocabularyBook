package jp.ac.it_college.std.pbl_b.vocabularybook

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.ArrayAdapter
import jp.ac.it_college.std.pbl_b.vocabularybook.databinding.ActivityWordSearchBinding

class WordSearchActivity : AppCompatActivity() {
    private lateinit var binding: ActivityWordSearchBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWordSearchBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val wordList = intent.getStringArrayExtra("WORD_LIST")
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

    private fun drawResult(wordList: Array<String>?) {
        val searchWord = binding.edWord.text.toString()
        val resultList = mutableListOf<String>()
        wordList?.forEach { resultList.add(it) }
        binding.lvSearchResult.adapter = ArrayAdapter(
            this@WordSearchActivity,
            R.layout.word_row,
            resultList.filter { it.contains(searchWord) }
        )
    }
}