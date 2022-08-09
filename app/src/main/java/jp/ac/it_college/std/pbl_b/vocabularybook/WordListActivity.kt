package jp.ac.it_college.std.pbl_b.vocabularybook

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import jp.ac.it_college.std.pbl_b.vocabularybook.databinding.ActivityWordListBinding

class WordListActivity : AppCompatActivity() {
    private lateinit var binding: ActivityWordListBinding

    private val helper = DatabaseHelper(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWordListBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val category = intent.getStringExtra("CATE_NAME")
        val categoryId = intent.getIntExtra("CATE_ID", 0)
        title = "${getString(R.string.category)} --- $category"
        val wordsList = helper.getWordsList(categoryId).map { it.word }
        binding.lvWordList.adapter = ArrayAdapter(
            this@WordListActivity,R.layout.word_row, wordsList)
        binding.btBack.setOnClickListener{
            finish()
        }
    }

    override fun onDestroy() {
        helper.close()
        super.onDestroy()
    }
}