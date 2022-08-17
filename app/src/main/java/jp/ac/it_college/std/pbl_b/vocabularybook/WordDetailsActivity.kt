package jp.ac.it_college.std.pbl_b.vocabularybook

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.SimpleAdapter
import jp.ac.it_college.std.pbl_b.vocabularybook.databinding.ActivityWordDetailsBinding

class WordDetailsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityWordDetailsBinding

    private val helper = DatabaseHelper(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWordDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val wordId = intent.getLongExtra("WORD_ID", 0)
        val wordName = intent.getStringExtra("WORD_NAME")
        binding.JpLang.text = wordName
        val translations = helper.getTranslatedWords(wordId)
        val inputList = mutableListOf<Map<String, String>>()
        translations.forEach {
            inputList.add(mapOf(
                Pair("tvLanguageName", it.language),
                Pair("tvMeaning", it.mean))
            )
        }
        binding.lvDetails.adapter = SimpleAdapter(
            this,
            inputList,
            R.layout.word_detail_row,
            arrayOf("tvLanguageName", "tvMeaning"),
            intArrayOf(R.id.tvLanguageName, R.id.tvMeaning)
        )

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        var returnVal = true
        if(item.itemId == android.R.id.home) {
            finish()
        }
        else {
            returnVal = super.onOptionsItemSelected(item)
        }
        return returnVal
    }
}