package com.example.summerweather.logic.network

import com.example.summerweather.SummerWeatherApplication
import com.example.summerweather.logic.model.PlaceResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

/*
用于访问彩云天气城市搜索API的Retrofit接口
 */
interface PlaceService {
    /*
    声明一个@GET注解，这样当调用searchPlaces()方法时。Retrofit会自动发起一条GET请求，去访问注解中配置的地址。
    搜索城市数据的API中只有query这个参数需要动态指定，使用@Query注解来实现，另外两个参数不变，固定写在@GET注解中即可。
     */
    @GET("v2/place?token=${SummerWeatherApplication.TOKEN}&lang=zh_CN")
    fun searchPlaces(@Query("query") query:String): Call<PlaceResponse>
}