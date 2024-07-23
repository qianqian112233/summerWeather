package com.example.summerweather.logic.model

import android.os.Parcelable
import androidx.versionedparcelable.VersionedParcelize

/*
list
book_cover
book_title
book_rating
book_summary
book_category
book_rank
 */
data class Book(
    val cover: String,
    val title: String,
    val rating: String,
    val summary: String,
    val category: String,
    val rank: String? = null
)
