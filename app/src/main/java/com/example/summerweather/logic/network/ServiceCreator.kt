package com.example.summerweather.logic.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/*
创建一个Retrofit构建器，使用PlaceService接口
只要在调用create()方法时针对不同的Service接口传入相应的Class类型即可
是一个单例类
 */
object ServiceCreator {
    //外部不可见
    private const val BASE_URL = "https://api.caiyunapp.com/"
    //外部不可见
    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    /*
    外部可见的create方法，接收一个Class类型的参数。
    当在外部调用这个方法时，就是调用了Retrofit对象的create()方法，从而创建出相应的Service接口的动态代理对象。
     */
    fun <T> create(serviceClass: Class<T>): T = retrofit.create(serviceClass)
    inline fun <reified T> create(): T = create(T::class.java)
}