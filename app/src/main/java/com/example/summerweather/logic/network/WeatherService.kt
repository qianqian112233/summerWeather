package com.example.summerweather.logic.network

import com.example.summerweather.SummerWeatherApplication
import com.example.summerweather.logic.model.FutureResponse
import com.example.summerweather.logic.model.RealtimeResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

/*
用于访问天气信息的Retrofit接口
 */
interface WeatherService
{
    //获取实时天气
    @GET("v2.5/${SummerWeatherApplication.TOKEN}/{lng},{lat}/realtime.json")
    fun getRealtimeWeather(@Path("lng") lng: String, @Path("lat") lat: String): Call<RealtimeResponse>

    //获取未来天气
    @GET("v2.5/${SummerWeatherApplication.TOKEN}/{lng},{lat}/daily.json")
    fun getFutureWeather(@Path("lng") lng: String, @Path("lat") lat: String): Call<FutureResponse>
}