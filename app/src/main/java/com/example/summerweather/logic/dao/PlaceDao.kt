package com.example.summerweather.logic.dao

import android.content.Context
import android.provider.Settings
import android.provider.Settings.Global.putString
import androidx.core.content.edit
import com.example.summerweather.SummerWeatherApplication
import com.example.summerweather.logic.model.Place
import com.google.gson.Gson

object PlaceDao {
    /*
    存储：
    将Place对象存储到SharedPreferences文件中，
    先通过GSON将Place对象转成一个JSON字符串，
    然后就用字符串存储的方式来保存数据
     */
    fun savePlace(place: Place){
        sharedPreferences().edit{
            putString("place", Gson().toJson(place))
        }
    }

    /*
    读取：
    将JSON字符串从SharedPreferences文件中读取出来，
    然后再通过GSON将JSON字符串解析成Place对象并返回
     */
    fun getSavedPlace(): Place{
        val placeJson = sharedPreferences().getString("place", "")
        return Gson().fromJson(placeJson, Place::class.java)
    }

    //判断是否有数据被存储
    fun isPlacedSaved() = sharedPreferences().contains("place")

    private fun sharedPreferences() = SummerWeatherApplication.context
        .getSharedPreferences("summer_weather", Context.MODE_PRIVATE)
}