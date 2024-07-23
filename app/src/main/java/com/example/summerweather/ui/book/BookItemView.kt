package com.example.summerweather.ui.book

import android.content.Context
import android.media.Image
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.bumptech.glide.Glide
import com.example.summerweather.R
import com.example.summerweather.logic.model.Book
import org.w3c.dom.Attr

/*
自定义View类
 */
class BookItemView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr){
    private val bookImage: ImageView
    private val bookTitle: TextView
    private val bookRating: TextView
    private val bookSummary: TextView
    private val bookCategory: TextView
    private val bookRank: Button
    private val feedbackButton: ImageButton

    init {
        /*
        LayoutInflater是用于加载XML布局文件并转换为View对象的类；
        from(context)是一个静态方法，用于获取LayoutInflater的实例；
        .inflate(R.layout.view_book_item, this, true)：使用LayoutInflater实例来执行实际的布局加载和视图创建过程，
        其中，this代表当前对象，true用于控制是否立即添加加载的视图到父视图中。
         */
        val view = LayoutInflater.from(context).inflate(R.layout.view_book_item, this, true)
        bookImage = view.findViewById(R.id.book_image)
        bookTitle = view.findViewById(R.id.book_title)
        bookRating = view.findViewById(R.id.book_rating)
        bookSummary = view.findViewById(R.id.book_summary)
        bookCategory = view.findViewById(R.id.book_category)
        bookRank = view.findViewById(R.id.book_rank)
        feedbackButton = view.findViewById(R.id.feedback_button)
    }

    fun bind(book: Book, itemActionListener: OnItemActionListener){
        bookTitle.text = book.title
        bookRating.text = book.rating
        bookCategory.text = book.category
        bookSummary.text = book.summary
        //文字过长时只展示两行，其余的用...代替
        bookSummary.ellipsize = android.text.TextUtils.TruncateAt.END

        if (book.rank.isNullOrEmpty()){
            bookRank.visibility = View.GONE
        } else{
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
        Glide.with(context)
            .load("file:///android_asset/${book.cover}")
            .into(bookImage)
    }
}

