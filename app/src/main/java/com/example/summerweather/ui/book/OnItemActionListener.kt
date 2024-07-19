package com.example.summerweather.ui.book

import com.example.summerweather.logic.model.Book

/*
使用接口回调的方法，定义用于处理点击事件的接口。
从Adapter中将事件回调到Activity，从而在Activity中处理点击事件。
 */
interface OnItemActionListener {
    fun onRankButtonClick(book: Book)
    fun onFeedbackButtonCLick(book: Book)
}