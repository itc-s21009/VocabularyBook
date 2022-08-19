package jp.ac.it_college.std.pbl_b.vocabularybook

import android.content.Intent
import android.os.Bundle
import android.util.TypedValue
import android.view.*
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.PopupWindow
import androidx.appcompat.app.AppCompatActivity
import jp.ac.it_college.std.pbl_b.vocabularybook.databinding.ActivityMainBinding
import jp.ac.it_college.std.pbl_b.vocabularybook.databinding.PopupAddCategoryBinding


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private val helper = DatabaseHelper(this)
    private lateinit var mPopupWindow: PopupWindow
    private lateinit var categoryListRaw: List<DBCategory>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        title = getString(R.string.app_name)
        drawCategoryList()
        registerForContextMenu(binding.lvCategory)
        initPopupWindow()
    }

    override fun onDestroy() {
        if (mPopupWindow.isShowing) {
            mPopupWindow.dismiss()
        }
        super.onDestroy()
    }

    override fun onCreateContextMenu(
        menu: ContextMenu?,
        v: View?,
        menuInfo: ContextMenu.ContextMenuInfo?
    ) {
        super.onCreateContextMenu(menu, v, menuInfo)
        menuInflater.inflate(R.menu.menu_context_category, menu)
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        var returnVal = true
        val info = item.menuInfo as AdapterView.AdapterContextMenuInfo
        val listPosition = info.position
        when (item.itemId) {
            R.id.itRemoveCategory -> {
                val category = categoryListRaw[listPosition]
                helper.deleteCategory(category.id.toLong())
                drawCategoryList()
            }
            else -> returnVal = super.onContextItemSelected(item)
        }
        return returnVal
    }

    private fun initPopupWindow() {
        mPopupWindow = PopupWindow(this@MainActivity)
        val view = PopupAddCategoryBinding.inflate(layoutInflater)
        view.btBack.setOnClickListener {
            if (mPopupWindow.isShowing) {
                mPopupWindow.dismiss()
            }
        }
        view.btAddCategoryConfirm.setOnClickListener {
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

    private fun drawCategoryList() {
        categoryListRaw = helper.getCategoryList()
        val categoryList =  categoryListRaw.map { it.name }
        binding.lvCategory.adapter = ArrayAdapter(
            this@MainActivity,R.layout.category_row, categoryList)
        binding.lvCategory.setOnItemClickListener{ _, _, position, _ ->
            val category = categoryListRaw[position]
            showWordListActivity(category)
        }
    }

    private fun showWordListActivity(category: DBCategory) {
        val intent = Intent(this, WordListActivity::class.java)
        intent.putExtra("CATE_ID", category.id)
        intent.putExtra("CATE_NAME", category.name)
        startActivity(intent)
    }
}