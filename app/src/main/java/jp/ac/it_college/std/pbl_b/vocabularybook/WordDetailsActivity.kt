package jp.ac.it_college.std.pbl_b.vocabularybook

import android.content.Context
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
import jp.ac.it_college.std.pbl_b.vocabularybook.databinding.PopupEditWordDetailBinding

class WordDetailsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityWordDetailsBinding

    private val helper = DatabaseHelper(this)
    private lateinit var popupAddDetail: PopupWindow
    private lateinit var bindingAddDetail: PopupAddWordDetailBinding
    private lateinit var popupEditDetail: PopupEditDetail
    private lateinit var bindingEditDetail: PopupEditWordDetailBinding
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
        initPopupAddDetail()
        initPopupEditDetail()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return if (item.itemId == android.R.id.home) {
            finish()
            true
        } else {
            super.onOptionsItemSelected(item)
        }
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
                val translatedWord = translatedListRaw[listPosition]
                bindingEditDetail.etEditDetail.setText(translatedWord.mean)
                languageListRaw = helper.getLanguagesList()
                val languageNamesList = languageListRaw.map { it.name }
                bindingEditDetail.spLanguage.adapter = ArrayAdapter(
                    this, android.R.layout.simple_list_item_1,
                    languageNamesList
                )
                bindingEditDetail.spLanguage.setSelection(
                    languageNamesList.indexOf(translatedWord.language)
                )
                popupEditDetail.showAtLocation(
                    binding.lvDetails,
                    Gravity.CENTER,
                    0,
                    0,
                    translatedWord.translation_id
                )
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

    private fun initPopupWindow(popup: PopupWindow) {
        popup.isOutsideTouchable = true
        popup.isFocusable = true
        val width = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            300f,
            resources.displayMetrics
        )
        popup.setBackgroundDrawable(getDrawable(R.drawable.popup_background))
        popup.setWindowLayoutMode(width.toInt(), WindowManager.LayoutParams.WRAP_CONTENT)
        popup.width = width.toInt()
        popup.height = WindowManager.LayoutParams.WRAP_CONTENT
    }

    private fun initPopupAddDetail() {
        popupAddDetail = PopupWindow(this@WordDetailsActivity)
        initPopupWindow(popupAddDetail)
        bindingAddDetail = PopupAddWordDetailBinding.inflate(layoutInflater)
        popupAddDetail.contentView = bindingAddDetail.root
        bindingAddDetail.btBack.setOnClickListener {
            if (popupAddDetail.isShowing) {
                popupAddDetail.dismiss()
            }
        }
        bindingAddDetail.btAddDetailConfirm.setOnClickListener {
            val wordId = intent.getLongExtra("WORD_ID", 0)
            val text = bindingAddDetail.etAddDetail.text.toString()
            val language = languageListRaw[bindingAddDetail.spLanguage.selectedItemPosition]
            if (text.isEmpty()) {
                return@setOnClickListener
            }
            helper.registerTranslation(wordId, text, language.id.toLong())
            drawTranslatedList()
            popupAddDetail.dismiss()
        }
        binding.btAddDetail.setOnClickListener {
            bindingAddDetail.etAddDetail.setText(R.string.empty)
            languageListRaw = helper.getLanguagesList()
            bindingAddDetail.spLanguage.adapter = ArrayAdapter(
                this, android.R.layout.simple_list_item_1,
                languageListRaw.map { it.name }
            )
            popupAddDetail.showAtLocation(binding.btAddDetail, Gravity.CENTER, 0, 0)
        }
    }

    private fun initPopupEditDetail() {
        popupEditDetail = PopupEditDetail(this@WordDetailsActivity)
        initPopupWindow(popupEditDetail)
        bindingEditDetail = PopupEditWordDetailBinding.inflate(layoutInflater)
        popupEditDetail.contentView = bindingEditDetail.root
        bindingEditDetail.btBack.setOnClickListener {
            if (popupEditDetail.isShowing) {
                popupEditDetail.dismiss()
            }
        }
        bindingEditDetail.btEditDetailConfirm.setOnClickListener {
            val text = bindingEditDetail.etEditDetail.text.toString()
            val language = languageListRaw[bindingEditDetail.spLanguage.selectedItemPosition]
            if (text.isEmpty()) {
                return@setOnClickListener
            }
            helper.updateTranslation(
                popupEditDetail.translateId.toLong(),
                text,
                language.id.toLong()
            )
            drawTranslatedList()
            popupEditDetail.dismiss()
        }
    }

    private fun drawTranslatedList() {
        val wordId = intent.getLongExtra("WORD_ID", 0)
        translatedListRaw = helper.getTranslatedWords(wordId)
        val inputList = mutableListOf<Map<String, String>>()
        translatedListRaw.forEach {
            inputList.add(
                mapOf(
                    Pair("tvLanguageName", it.language),
                    Pair("tvMeaning", it.mean)
                )
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

class PopupEditDetail(context: Context) : PopupWindow(context) {
    var translateId: Int = 0
        get() {
            Log.i("Test", "$field")
            return field
        }
        set(value) {
            Log.i("Test", "$field -> $value")
            field = value
        }

    fun showAtLocation(parent: View?, gravity: Int, x: Int, y: Int, translate_id: Int) {
        this.translateId = translate_id
        super.showAtLocation(parent, gravity, x, y)
    }
}