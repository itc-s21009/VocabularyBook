package jp.ac.it_college.std.pbl_b.vocabularybook

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.ContextMenu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.SimpleAdapter
import jp.ac.it_college.std.pbl_b.vocabularybook.databinding.ActivityWordDetailsBinding

class WordDetailsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityWordDetailsBinding

    private val helper = DatabaseHelper(this)
    private lateinit var translatedListRaw: List<TranslatedWord>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWordDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val wordName = intent.getStringExtra("WORD_NAME")
        binding.JpLang.text = wordName
        drawTranslatedList()
        registerForContextMenu(binding.lvDetails)
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

    override fun onCreateContextMenu(
        menu: ContextMenu?,
        v: View?,
        menuInfo: ContextMenu.ContextMenuInfo?
    ) {
        super.onCreateContextMenu(menu, v, menuInfo)
        menuInflater.inflate(R.menu.menu_context_translated_word, menu)
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        var returnVal = true
        val info = item.menuInfo as AdapterView.AdapterContextMenuInfo
        val listPosition = info.position
        when (item.itemId) {
            R.id.itEditTranslatedWord -> {

            }
            R.id.itRemoveTranslatedWord -> {
                val translatedId = translatedListRaw[listPosition].translation_id.toLong()
                helper.deleteTranslation(translatedId)
                drawTranslatedList()
            }
            else -> returnVal = super.onContextItemSelected(item)
        }
        return returnVal
    }

    private fun drawTranslatedList() {
        val wordId = intent.getLongExtra("WORD_ID", 0)
        translatedListRaw = helper.getTranslatedWords(wordId)
        val inputList = mutableListOf<Map<String, String>>()
        translatedListRaw.forEach {
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
}