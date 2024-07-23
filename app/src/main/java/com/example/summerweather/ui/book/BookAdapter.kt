package com.example.summerweather.ui.book

import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.compose.ui.layout.Layout
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.summerweather.R
import com.example.summerweather.logic.model.Book

/*
在BookAdapter中接收OnItemActionListener,并在ViewHolder中设置点击事件
 */
class BookAdapter (private val books: List<Book>, private val itemActionListener: OnItemActionListener) : RecyclerView.Adapter<BookAdapter.BookViewHolder>() {

    /*
    将BookAdapter适配器的ViewHolder改为使用新的自定义View
     */
    class BookViewHolder(val bookItemView: BookItemView) : RecyclerView.ViewHolder(bookItemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookViewHolder {
        val bookItemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.activity_book_list_item, parent, false) as BookItemView
        return BookViewHolder(bookItemView)
    }

    override fun getItemCount() = books.size

    override fun onBindViewHolder(holder: BookViewHolder, position: Int) {
        val book = books[position]
        holder.bookItemView.bind(book, itemActionListener)

        //设置点击监听器
        holder.bookItemView.setOnClickListener {
            itemActionListener.onBookItemClick(book)
        }
    }
}