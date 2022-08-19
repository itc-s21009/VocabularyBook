package jp.ac.it_college.std.pbl_b.vocabularybook

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import jp.ac.it_college.std.pbl_b.vocabularybook.databinding.ActivityWordAddBinding

class WordAddActivity : AppCompatActivity() {
    private lateinit var binding: ActivityWordAddBinding

    private val helper = DatabaseHelper(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWordAddBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val categoryId = intent.getIntExtra("CATE_ID", 0)
        val category = intent.getStringExtra("CATE_NAME")

        title = "${getString(R.string.add_word)} --- $category"

        binding.btAddNewWord.setOnClickListener{
            val wordName = binding.etJapanese.text.toString()
            if (wordName.isEmpty()) {
                return@setOnClickListener
            }
            helper.registerWord(categoryId.toLong(), wordName)
            finish()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return if (item.itemId == android.R.id.home) {
            finish()
            true
        } else {
            super.onOptionsItemSelected(item)
        }
    }
}