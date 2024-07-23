package com.example.summerweather.ui.book

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.summerweather.R
import com.example.summerweather.logic.model.Book
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.ObjectInput

class BookListActivity : AppCompatActivity(), OnItemActionListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_book_list)

        val recyclerView: RecyclerView = findViewById(R.id.book_recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        val books = loadBooksFromAssets()
        val adapter = BookAdapter(books, this)
        recyclerView.adapter = adapter

    }

    //从assets文件中加载Book信息
    private fun loadBooksFromAssets() : List<Book>{
        //获取assetManager实例，用于访问应用的assets、目录
        val assetManager = assets
        //打开books.json文件，并获取输入流
        val inputStream = assetManager.open("books.json")
        //使用bufferReader包装
        val bufferReader = BufferedReader(InputStreamReader(inputStream))
        val builder = StringBuilder()
        bufferReader.useLines { lines -> lines.forEach { builder.append(it) } }

        val gson = Gson()
        val bookType = object : TypeToken<List<Book>>() {}.type

        return gson.fromJson(builder.toString(), bookType)
    }

    override fun onRankButtonClick(book: Book) {
        Toast.makeText(this, "这里跳转到当前展示的排行榜", Toast.LENGTH_SHORT).show()
    }

    override fun onFeedbackButtonCLick(book: Book) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("负反馈选项")
        builder.setItems(arrayOf("不感兴趣", "不喜欢该题材")) { dialog, which ->
            //用选择的选项
            when(which){
                0 -> Toast.makeText(this, "您选择了：不感兴趣", Toast.LENGTH_SHORT).show()
                1 -> Toast.makeText(this, "您选择了：不喜欢该题材", Toast.LENGTH_SHORT).show()
            }
        }
        builder.setNegativeButton("取消", null)
        builder.show()
    }

    override fun onBookItemClick(book: Book) {
//        val intent = Intent(this, BookDetailActivity::class.java)
//        intent.putExtra("Book", book)
//        startActivity(intent)
        Toast.makeText(this, "这里跳转到详细书籍界面", Toast.LENGTH_SHORT).show()
    }
}