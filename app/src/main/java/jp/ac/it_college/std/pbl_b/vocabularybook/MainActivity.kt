package jp.ac.it_college.std.pbl_b.vocabularybook

import android.content.Intent
import android.os.Bundle
import android.util.TypedValue
import android.view.Gravity
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Button
import android.widget.LinearLayout
import android.widget.PopupWindow
import androidx.appcompat.app.AppCompatActivity
import jp.ac.it_college.std.pbl_b.vocabularybook.databinding.ActivityMainBinding
import jp.ac.it_college.std.pbl_b.vocabularybook.databinding.PopupAddCategoryBinding


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private val helper = DatabaseHelper(this)
    private lateinit var mPopupWindow: PopupWindow

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        title = getString(R.string.app_name)
        drawCategoryList()
        mPopupWindow = PopupWindow(this@MainActivity)
        val view = PopupAddCategoryBinding.inflate(layoutInflater)
        view.btBack.setOnClickListener {
            if (mPopupWindow.isShowing) {
                mPopupWindow.dismiss()
            }
        }
        view.btAddCategoryConfirm.setOnClickListener{
            val text = view.etAddCategory.text.toString()
            if (text.isEmpty()) {
                return@setOnClickListener
            }
            helper.registerCategory(text)
            drawCategoryList()
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
        binding.btAddCategory.setOnClickListener {
            view.etAddCategory.setText(R.string.empty)
            mPopupWindow.showAtLocation(binding.btAddCategory, Gravity.CENTER, 0, 0)
        }
    }

    override fun onDestroy() {
        if (mPopupWindow.isShowing) {
            mPopupWindow.dismiss()
        }
        super.onDestroy()
    }

    private fun drawCategoryList() {
        binding.categoryList.removeAllViews()
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