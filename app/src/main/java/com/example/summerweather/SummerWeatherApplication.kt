package com.example.summerweather

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context

class SummerWeatherApplication : Application() {
    companion object{
        const val TOKEN = "3PoYNX2UuMfGDLu7"
        @SuppressLint("StaticFieldLeak")
        lateinit var context: Context
    }

    override fun onCreate() {
        super.onCreate()
        context = applicationContext
    }
}