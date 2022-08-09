package jp.ac.it_college.std.pbl_b.vocabularybook

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import jp.ac.it_college.std.pbl_b.vocabularybook.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private val helper = DatabaseHelper(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        title = getString(R.string.app_name)
        val categoryList = helper.getCategoryList()
        categoryList.forEach { category ->
            val button = Button(this)
            binding.categoryList.addView(button)
            button.text = category.name

            button.setOnClickListener {
                val intent = Intent(this@MainActivity, WordListActivity::class.java)
                intent.putExtra("CATE_ID", category.id)
                intent.putExtra("CATE_NAME", category.name)
                startActivity(intent)
            }
            val layoutParams = LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            layoutParams.setMargins(32, 16, 32, 0)
            button.layoutParams = LinearLayout.LayoutParams(layoutParams)
        }
    }
}