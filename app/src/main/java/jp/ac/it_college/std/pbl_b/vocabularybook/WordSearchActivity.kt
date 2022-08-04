package jp.ac.it_college.std.pbl_b.vocabularybook

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import jp.ac.it_college.std.pbl_b.vocabularybook.databinding.ActivityWordSearchBinding

class WordSearchActivity : AppCompatActivity() {
    private lateinit var binding: ActivityWordSearchBinding
    private val test = mutableListOf("aaaaa", "bbbb", "cccccc")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWordSearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.lvSearchResult.adapter = ArrayAdapter(
            this@WordSearchActivity, R.layout.word_row, test
        )

        binding.btWordSearch.setOnClickListener {

        }
    }
}