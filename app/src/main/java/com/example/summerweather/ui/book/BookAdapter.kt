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

    class BookViewHolder(itemView: View, private val itemActionListener: OnItemActionListener) : RecyclerView.ViewHolder(itemView) {
        val bookImage: ImageView = itemView.findViewById(R.id.book_image)
        val bookTitle: TextView = itemView.findViewById(R.id.book_title)
        val bookRating: TextView = itemView.findViewById(R.id.book_rating)
        val bookSummary: TextView = itemView.findViewById(R.id.book_summary)
        val bookCategory: TextView = itemView.findViewById(R.id.book_category)
        val bookRank: Button = itemView.findViewById(R.id.book_rank)
        val feedbackButton: ImageButton = itemView.findViewById(R.id.feedback_button)

        fun bind(book: Book){
            bookTitle.text = book.title
            bookRating.text = book.rating
            bookCategory.text = book.category
            bookSummary.text = book.summary
            //文字过长时只展示两行，其余的...用代替
            bookSummary.ellipsize = TextUtils.TruncateAt.END

            if(book.rank.isNullOrEmpty()){
                bookRank.visibility = View.GONE
            }else{
                bookRank.text = book.rank
                bookRank.visibility = View.VISIBLE
            }

            bookRank.setOnClickListener {
                itemActionListener.onRankButtonClick(book)
            }

            feedbackButton.setOnClickListener {
                itemActionListener.onFeedbackButtonCLick(book)
            }

            //根据assets中的json文件读取封面图片并加载
            Glide.with(itemView.context)
                .load("file:///android_asset/${book.cover}")
                .into(bookImage)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.activity_book_list_item, parent, false)
        return BookViewHolder(itemView, itemActionListener)
    }

    override fun getItemCount() = books.size

    override fun onBindViewHolder(holder: BookViewHolder, position: Int) {
        val book = books[position]
        holder.bind(book)
    }

}