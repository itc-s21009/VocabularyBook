package jp.ac.it_college.std.pbl_b.vocabularybook

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.ContextMenu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import jp.ac.it_college.std.pbl_b.vocabularybook.databinding.ActivityWordListBinding

class WordListActivity : AppCompatActivity() {
    private lateinit var binding: ActivityWordListBinding

    private val helper = DatabaseHelper(this)

    private lateinit var wordsListRaw: List<Word>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWordListBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val category = intent.getStringExtra("CATE_NAME")
        title = "${getString(R.string.category)} --- $category"
        drawWordList()
        val wordIdList = wordsListRaw.map { it.id.toLong() }
        val wordsList =  wordsListRaw.map { it.word }
        binding.btBack.setOnClickListener{
            finish()
        }
        binding.btSearch.setOnClickListener{
            val intent = Intent(this, WordSearchActivity::class.java)
            intent.putExtra("WORD_LIST", wordsList.toTypedArray())
            startActivity(intent)
        }
        registerForContextMenu(binding.lvWordList)
//        binding.btAddWord.setOnClickListener{
//            val intent = Intent(this, WordDetailsActivity::class.java)
//            intent.putExtra("CATE_NAME", category)
//            intent.putExtra("CATE_ID", categoryId)
//            startActivity(intent)
//        }
    }

    override fun onDestroy() {
        helper.close()
        super.onDestroy()
    }

    override fun onCreateContextMenu(
        menu: ContextMenu?,
        v: View?,
        menuInfo: ContextMenu.ContextMenuInfo?
    ) {
        super.onCreateContextMenu(menu, v, menuInfo)
        menuInflater.inflate(R.menu.menu_context_word, menu)
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        var returnVal = true
        val info = item.menuInfo as AdapterView.AdapterContextMenuInfo
        val listPosition = info.position
        when (item.itemId) {
            R.id.itDetail -> {
                val wordIdList = wordsListRaw.map { it.id.toLong() }
                showWordDetailsActivity(wordIdList[listPosition])
            }
            R.id.itRemove -> {
                val wordIdList = wordsListRaw.map { it.id.toLong() }
                helper.deleteWord(wordIdList[listPosition])
                drawWordList()
            }
            else -> returnVal = super.onContextItemSelected(item)
        }
        return returnVal
    }

    private fun showWordDetailsActivity(wordId: Long) {
        val intent = Intent(this, WordDetailsActivity::class.java)
        intent.putExtra("WORD_ID", wordId)
        startActivity(intent)
    }

    private fun drawWordList() {
        val categoryId = intent.getIntExtra("CATE_ID", 0)
        wordsListRaw = helper.getWordsList(categoryId)
        val wordIdList = wordsListRaw.map { it.id.toLong() }
        val wordsList =  wordsListRaw.map { it.word }
        binding.lvWordList.adapter = ArrayAdapter(
            this@WordListActivity,R.layout.word_row, wordsList)
        binding.lvWordList.setOnItemClickListener{ _, _, position, _ ->
            val wordId = wordIdList[position]
            showWordDetailsActivity(wordId)
        }
    }
}