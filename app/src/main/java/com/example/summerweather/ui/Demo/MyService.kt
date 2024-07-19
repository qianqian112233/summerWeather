package com.example.summerweather.ui.Demo

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import android.util.Log

class MyService : Service() {

    private val mBinder = DownloadBinder()

    class DownloadBinder : Binder(){
        fun startDownload(){
            Log.d("MyService", "startDownload executed")
        }

        fun getProgress() : Int{
            Log.d("MyService", "getProgress executed")
            return 0
        }
    }
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d("MyService", "Service started")
        return START_STICKY
    }

    override fun onBind(intent: Intent): IBinder? {
        return mBinder
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("MyService", "Service destroyed")
    }
}