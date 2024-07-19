package com.example.summerweather.ui.book

import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
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

    private fun onBookItemClicked(book : Book){

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
        Toast.makeText(this, "这里跳转到当前展示的排行榜", Toast.LENGTH_LONG).show()
    }

    override fun onFeedbackButtonCLick(book: Book) {
        Toast.makeText(this, "这里弹出反馈界面", Toast.LENGTH_LONG).show()
    }
}