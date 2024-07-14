package com.example.summerweather.ui.weather

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.summerweather.R
import com.example.summerweather.databinding.ActivityWeatherBinding
import com.example.summerweather.databinding.ForecastBinding
import com.example.summerweather.databinding.ForecastItemBinding
import com.example.summerweather.databinding.LifeIndexBinding
import com.example.summerweather.databinding.NowBinding
import com.example.summerweather.logic.model.Weather
import com.example.summerweather.logic.model.getSky
import java.text.SimpleDateFormat
import java.util.Locale

class WeatherActivity : AppCompatActivity() {

    private lateinit var binding: ActivityWeatherBinding
    private lateinit var nowBinding: NowBinding
    private lateinit var forecastBinding: ForecastBinding
    private lateinit var lifeIndexBinding: LifeIndexBinding

    val viewModel by lazy { ViewModelProvider(this).get(WeatherViewModel::class.java) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        /*
        改变系统UI的显示：
        Activity的布局会显示在状态栏上面并将状态栏设置成透明色
         */
        val decoView = window.decorView
        decoView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
        window.statusBarColor = Color.TRANSPARENT
        binding = ActivityWeatherBinding.inflate(layoutInflater)

        //setContentView(R.layout.activity_weather)
        setContentView(binding.root)
        //初始化bindings
        nowBinding = NowBinding.bind(findViewById(R.id.nowLayout))
        forecastBinding = ForecastBinding.bind(findViewById(R.id.forecastLayout))
        lifeIndexBinding = LifeIndexBinding.bind(findViewById(R.id.lifeIndexLayout))

        if(viewModel.locationLng.isEmpty()){
            viewModel.locationLng = intent.getStringExtra("location_lng") ?: ""
        }
        if(viewModel.locationLat.isEmpty()){
            viewModel.locationLat = intent.getStringExtra("location_lat") ?: ""
        }
        if(viewModel.placeName.isEmpty()){
            viewModel.placeName = intent.getStringExtra("place_name") ?: ""
        }
        viewModel.weatherLiveData.observe(this, Observer { result ->
            val weather = result.getOrNull()
            if(weather != null){
                showWeatherInfo(weather)
            }else{
                Toast.makeText(this, "无法成功获取天气信息", Toast.LENGTH_SHORT).show()
                result.exceptionOrNull()?.printStackTrace()
            }
        })
        viewModel.refreshWeather(viewModel.locationLng, viewModel.locationLat)
    }

    private fun showWeatherInfo(weather: Weather){
        nowBinding.placeName.text = viewModel.placeName
        val realtime = weather.realtime
        val future = weather.future

        //填充now布局中的数据
        val currentTempText = "${realtime.temperature.toInt()} ℃"
        nowBinding.currentTemp.text = currentTempText
        val currentPM25Text = "空气指数 ${realtime.airQuality.aqi.chn.toInt()}"
        nowBinding.currentAQI.text = currentPM25Text
        nowBinding.nowLayout.setBackgroundResource(getSky(realtime.skycon).bg)

        //填充forecast.xml布局中的数据
        forecastBinding.forecastLayout.removeAllViews()
        val days = future.skycon.size
        for(i in 0 until days){
            val skycon = future.skycon[i]
            val temperature = future.temperature[i]
            val view = LayoutInflater.from(this).inflate(R.layout.forecast_item,
                forecastBinding.forecastLayout, false)
            val dateInfo = view.findViewById(R.id.dateInfo) as TextView
            val skyIcon = view.findViewById(R.id.skyIcon) as ImageView
            val skyInfo = view.findViewById(R.id.skyInfo) as TextView
            val temperatureInfo = view.findViewById(R.id.temperatureInfo) as TextView
            val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            dateInfo.text = simpleDateFormat.format(skycon.date)
            val sky = getSky(skycon.value)
            skyIcon.setImageResource(sky.icon)
            skyInfo.text = sky.info
            val tempText = "${temperature.min.toInt()} ~ ${temperature.max.toInt()} °C"
            temperatureInfo.text = tempText
            forecastBinding.forecastLayout.addView(view)
        }

        //填充life_index.xml布局
        val lifeIndex = future.lifeIndex
        lifeIndexBinding.coldRiskText.text = lifeIndex.coldRisk[0].desc
        lifeIndexBinding.dressingText.text = lifeIndex.dressing[0].desc
        lifeIndexBinding.ultravioletText.text = lifeIndex.ultraviolet[0].desc
        lifeIndexBinding.carWashingText.text = lifeIndex.carWashing[0].desc

        binding.weatherLayout.visibility = View.VISIBLE
    }
}