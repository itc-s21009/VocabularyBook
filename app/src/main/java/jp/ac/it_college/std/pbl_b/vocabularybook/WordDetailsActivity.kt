package jp.ac.it_college.std.pbl_b.vocabularybook

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import android.view.*
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.PopupWindow
import android.widget.SimpleAdapter
import jp.ac.it_college.std.pbl_b.vocabularybook.databinding.ActivityWordDetailsBinding
import jp.ac.it_college.std.pbl_b.vocabularybook.databinding.PopupAddWordDetailBinding

class WordDetailsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityWordDetailsBinding

    private val helper = DatabaseHelper(this)
    private lateinit var mPopupWindow: PopupWindow
    private lateinit var translatedListRaw: List<TranslatedWord>
    private lateinit var languageListRaw: List<DBLanguage>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWordDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val wordName = intent.getStringExtra("WORD_NAME")
        binding.JpLang.text = wordName
        drawTranslatedList()
        registerForContextMenu(binding.lvDetails)
        mPopupWindow = PopupWindow(this@WordDetailsActivity)
        val view = PopupAddWordDetailBinding.inflate(layoutInflater)
        view.btBack.setOnClickListener {
            if (mPopupWindow.isShowing) {
                mPopupWindow.dismiss()
            }
        }
        view.btAddDetailConfirm.setOnClickListener {
            val wordId = intent.getLongExtra("WORD_ID", 0)
            val text = view.etAddDetail.text.toString()
            val language = languageListRaw[view.spLanguage.selectedItemPosition]
            if (text.isEmpty()) {
                return@setOnClickListener
            }
            helper.registerTranslation(wordId, text, language.id.toLong())
            drawTranslatedList()
            mPopupWindow.dismiss()
        }
        mPopupWindow.contentView = view.root
        mPopupWindow.isOutsideTouchable = true
        mPopupWindow.isFocusable = true
        val width = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            300f,
            resources.displayMetrics
        )
        mPopupWindow.setBackgroundDrawable(getDrawable(R.drawable.popup_background))
        mPopupWindow.setWindowLayoutMode(width.toInt(), WindowManager.LayoutParams.WRAP_CONTENT)
        mPopupWindow.width = width.toInt()
        mPopupWindow.height = WindowManager.LayoutParams.WRAP_CONTENT
        binding.btAddDetail.setOnClickListener {
            view.etAddDetail.setText(R.string.empty)
            languageListRaw = helper.getLanguagesList()
            view.spLanguage.adapter = ArrayAdapter(
                this, android.R.layout.simple_list_item_1,
                languageListRaw.map { it.name }
            )
            mPopupWindow.showAtLocation(binding.btAddDetail, Gravity.CENTER, 0, 0)
        }
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