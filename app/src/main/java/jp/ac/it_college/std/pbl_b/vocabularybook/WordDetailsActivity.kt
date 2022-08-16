package jp.ac.it_college.std.pbl_b.vocabularybook

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import jp.ac.it_college.std.pbl_b.vocabularybook.databinding.ActivityMainBinding
import jp.ac.it_college.std.pbl_b.vocabularybook.databinding.ActivityWordDetailsBinding

class WordDetailsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityWordDetailsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWordDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val category = intent.getStringExtra("CATE_NAME")
        val categoryId = intent.getIntExtra("CATE_ID", 0)

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