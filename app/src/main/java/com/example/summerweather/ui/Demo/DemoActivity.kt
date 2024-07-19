package com.example.summerweather.ui.Demo

import android.content.BroadcastReceiver
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.ServiceConnection
import android.net.Uri
import android.os.Bundle
import android.os.IBinder
import android.widget.Button
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.summerweather.R

class DemoActivity : AppCompatActivity() {

    lateinit var timeChangeReceiver: TimeChangeReceiver

    lateinit var downloadBinder: MyService.DownloadBinder

    private var isReceivedRegistered = false

    private val connection = object : ServiceConnection{
        override fun onServiceConnected(name: ComponentName, service: IBinder) {
            downloadBinder = service as MyService.DownloadBinder
            downloadBinder.startDownload()
            downloadBinder.getProgress()
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            TODO("Not yet implemented")
        }

        override fun onBindingDied(name: ComponentName) {
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_demo)

        // 初始化按钮
        //val startServiceButton = findViewById<Button>(R.id.start_service_button)
        //val stopServiceButton = findViewById<Button>(R.id.stop_service_button)
        val bindServiceButton = findViewById<Button>(R.id.bind_service_button)
        val unbindServiceButton = findViewById<Button>(R.id.unbind_service_button)
        val queryContentProviderButton = findViewById<Button>(R.id.query_content_provider_button)
        val broadcastButton = findViewById<Button>(R.id.broadcast_button)

        bindServiceButton.setOnClickListener {
            val intent = Intent(this, MyService::class.java)
            bindService(intent, connection, Context.BIND_AUTO_CREATE)//绑定Service
        }

        unbindServiceButton.setOnClickListener {
            unbindService(connection)//解绑Service
        }
        //跳转到ContentProviderActivity
        queryContentProviderButton.setOnClickListener {
            val intent = Intent(this, ContentProviderActivity::class.java)
            startActivity(intent)
        }

        // 设置启动和停止Service的按钮点击事件
//        startServiceButton.setOnClickListener {
//            startService(Intent(this, MyService::class.java))
//        }
//
//        stopServiceButton.setOnClickListener {
//            stopService(Intent(this, MyService::class.java))
//        }

        //初始化并注册广播接收器
        timeChangeReceiver = TimeChangeReceiver()


        //动态注册（在代码中注册）监听系统时间变化；静态注册：在AndroidManifest.xml中注册
        broadcastButton.setOnClickListener {
            if(isReceivedRegistered){
                unregisterReceiver(timeChangeReceiver)
                isReceivedRegistered = false
                Toast.makeText(this, "Broadcast Receiver Unregistered", Toast.LENGTH_SHORT).show()
            }else{
                val intentFilter = IntentFilter()
                intentFilter.addAction("android.intent.action.TIME_TICK")
                registerReceiver(timeChangeReceiver, intentFilter)
                isReceivedRegistered = true
                Toast.makeText(this, "Broadcast Receiver Registered", Toast.LENGTH_SHORT).show()
            }
        }


    }

    override fun onDestroy() {
        super.onDestroy()
        if(isReceivedRegistered){
            unregisterReceiver(timeChangeReceiver)
            isReceivedRegistered = false
        }
    }
    inner class  TimeChangeReceiver : BroadcastReceiver(){
        override fun onReceive(context: Context?, intent: Intent?) {
            Toast.makeText(context, "Time has changed", Toast.LENGTH_SHORT).show()
        }

    }
}