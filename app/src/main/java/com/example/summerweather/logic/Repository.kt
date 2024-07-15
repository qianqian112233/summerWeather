package com.example.summerweather.logic

import androidx.lifecycle.liveData
import com.example.summerweather.logic.dao.PlaceDao
import com.example.summerweather.logic.model.Place
import com.example.summerweather.logic.model.Weather
import com.example.summerweather.logic.network.SummerWeatherNetwork
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope

/*
 仓库层：主要工作是判断调用方法请求的数据应该是从本地数据源中获取还是从网络数据源中获取，
 并将获得的数据返回给调用方。
 Repository单例类是仓库层的统一封装入口
 */
object Repository {

    /*
    为了能将异步获取的数据以响应式编程的方式通知给上一层，通常会返回一个LiveData对象。
    可以在liveData()函数的代码块中调用任意的挂起函数。
     */
    //线程参数指定为Dispatchers.IO，这样代码块中的所有代码就都运行在子线程中了
    fun searchPlaces(query: String) = liveData(Dispatchers.IO) {
        val result = try {
            val placeResponse = SummerWeatherNetwork.searchPlaces(query)
            if(placeResponse.status == "ok"){//当服务器响应是ok时，使用kotlin内置的Result.success()方法来包装获取的城市数据列表
                val places = placeResponse.places
                Result.success(places)
            }else{//包装异常信息
                Result.failure(RuntimeException("response status is ${placeResponse.status}"))
            }
        }catch (e: Exception){
            Result.failure<List<Place>>(e)
        }
        emit(result)
    }

    /*
    用来根据经纬度刷新天气。获取实时天气和未来天气信息这两个请求没有先后顺序，并发执行可以提高程序的运行效率。
    但要都得到他们的响应结果才能进一步执行程序。
    分别在两个async函数中发起网络请求，再分别调用await()方法，就可以保证只有在两个网络请求都成功响应后再进一步执行程序。
     */
    fun refreshWeather(lng: String, lat: String) = liveData(Dispatchers.IO){
        val result = try{
            coroutineScope {
                val deferredRealtime = async{
                    SummerWeatherNetwork.getRealtimeWeather(lng, lat)
                }
                val deferredFuture = async {
                    SummerWeatherNetwork.getFutureWeather(lng, lat)
                }
                val realtimeResponse = deferredRealtime.await()
                val futureResponse = deferredFuture.await()
                if (realtimeResponse.status == "ok" && futureResponse.status == "ok"){//状态响应都ok
                    //将Realtime和Daily对象取出并封装到weather对象中。
                    val weather = Weather(realtimeResponse.result.realtime, futureResponse.result.daily)
                    Result.success(weather)
                }else{
                    Result.failure(
                        RuntimeException(
                            "realtime response status is ${realtimeResponse.status}" +
                            "daily response status is ${futureResponse.status}"
                        )
                    )
                }
            }
        } catch (e: Exception){
            Result.failure<Weather>(e)
        }
        emit(result)
    }

    /*
    仓库层做一个接口封装
     */
    fun savePlace(place: Place) = PlaceDao.savePlace(place)
    fun getSavedPlace() = PlaceDao.getSavedPlace()
    fun isPlaceSaved() = PlaceDao.isPlacedSaved()

}