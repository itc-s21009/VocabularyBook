package jp.ac.it_college.std.pbl_b.vocabularybook

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.ContextMenu
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import jp.ac.it_college.std.pbl_b.vocabularybook.databinding.ActivityWordListBinding

class WordListActivity : AppCompatActivity() {
    private lateinit var binding: ActivityWordListBinding

    private val helper = DatabaseHelper(this)

    private lateinit var wordsListRaw: List<DBWord>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWordListBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val category = intent.getStringExtra("CATE_NAME")
        title = "${getString(R.string.category)} --- $category"
        drawWordList()
        registerForContextMenu(binding.lvWordList)
//        binding.btAddWord.setOnClickListener{
//            val intent = Intent(this, WordDetailsActivity::class.java)
//            intent.putExtra("CATE_NAME", category)
//            intent.putExtra("CATE_ID", categoryId)
//            startActivity(intent)
//        }
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                finish()
                true
            }
            R.id.itSearch -> {
                val intent = Intent(this, WordSearchActivity::class.java)
                intent.putExtra("WORD_ID_LIST", wordsListRaw.map { it.id }.toIntArray())
                intent.putExtra("WORD_LIST", wordsListRaw.map { it.word }.toTypedArray())
                startActivity(intent)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
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
                showWordDetailsActivity(wordsListRaw[listPosition])
            }
            R.id.itRemove -> {
                helper.deleteWord(wordsListRaw[listPosition].id.toLong())
                drawWordList()
            }
            else -> returnVal = super.onContextItemSelected(item)
        }
        return returnVal
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_action_search, menu)
        return true
    }

    private fun showWordDetailsActivity(word: DBWord) {
        val intent = Intent(this, WordDetailsActivity::class.java)
        intent.putExtra("WORD_ID", word.id.toLong())
        intent.putExtra("WORD_NAME", word.word)
        startActivity(intent)
    }

    private fun drawWordList() {
        val categoryId = intent.getIntExtra("CATE_ID", 0)
        wordsListRaw = helper.getWordsList(categoryId)
        val wordsList =  wordsListRaw.map { it.word }
        binding.lvWordList.adapter = ArrayAdapter(
            this@WordListActivity,R.layout.word_row, wordsList)
        binding.lvWordList.setOnItemClickListener{ _, _, position, _ ->
            showWordDetailsActivity(wordsListRaw[position])
        }
    }
}