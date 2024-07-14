package com.example.summerweather.ui.weather

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.summerweather.logic.Repository
import com.example.summerweather.logic.model.Location
import com.example.summerweather.logic.model.Weather

class WeatherViewModel: ViewModel() {
    private val locationLiveData = MutableLiveData<Location>()
    var locationLng = ""
    var locationLat = ""
    var placeName = ""

    private val _weatherLiveData = MediatorLiveData<Result<Weather>>()
    val weatherLiveData: LiveData<Result<Weather>> get() = _weatherLiveData

    init {
        _weatherLiveData.addSource(locationLiveData) { location ->
            if (location != null) {
                Log.d("WeatherViewModel", "Location updated: lng=${location.lng}, lat=${location.lat}")
                val result = Repository.refreshWeather(location.lng, location.lat)
                _weatherLiveData.addSource(result) { weatherResponse ->
                    _weatherLiveData.value = weatherResponse
                    Log.d("WeatherViewModel", "Weather response received: $weatherResponse")
                    _weatherLiveData.removeSource(result)
                }
            }else{
                Log.d("WeatherViewModel", "Received null location")
            }
        }
    }

    //    val weatherLiveData = Transformation.switch(locationLiveData){location ->
//        Repository.refreshWeather(location.lng, location.lat)
//    }

    /*
    刷新天气信息，并将传入的经纬度参数封装成一个Location对象后赋值给locationLiveData对象
     */
    fun refreshWeather(lng: String, lat: String){
        Log.d("WeatherViewModel", "refreshWeather called with lng=$lng, lat=$lat")
        locationLiveData.value = Location(lng, lat)
    }
}