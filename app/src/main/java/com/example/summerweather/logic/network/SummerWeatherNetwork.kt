package com.example.summerweather.logic.network

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.await
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

object SummerWeatherNetwork {
    //使用ServiceCreator创建了一个PlaceService接口的动态代理对象
    private val placeService = ServiceCreator.create<PlaceService>()

    //调用在PlaceService中定义的searchPlaces()方法，以发起搜索城市数据请求
    //将searchPlaces()函数也声明成一个挂起函数
    suspend fun searchPlaces(query: String) = placeService.searchPlaces(query).await()

    //Retrofit回调的写法，借助携程技术来实现，因此定义了一个await()函数
    /*当外部调用SummerWeatherNetwork中的searchPlaces()函数时，Retrofit会立即发起网络请求，当前的协程也会被阻塞住
      直到服务器响应了请求，await()函数会将解析出来的数据模型对象取出并返回，同时恢复当前协程的执行。
     */
    private suspend fun <T> Call<T>.await(): T{
        return suspendCoroutine { continuation ->
            enqueue(object : Callback<T>{
                override fun onResponse(call: Call<T>, response: Response<T>) {
                    val body = response.body()
                    if(body != null) continuation.resume(body)
                    else continuation.resumeWithException(
                        RuntimeException("response body is null"))
                }

                override fun onFailure(call: Call<T>, t: Throwable) {
                    continuation.resumeWithException(t)
                }
            })
        }
    }
}