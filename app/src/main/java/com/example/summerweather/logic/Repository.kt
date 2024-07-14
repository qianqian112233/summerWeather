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
 仓库层的主要工作时判断调用方法请求的数据应该是从本地数据源中获取还是从网络数据源中获取，
 并将获得的数据返回给调用方。
 Repository单例类是仓库层的统一封装入口
 */
object Repository {

    /*
    可以在liveData()函数的代码块中调用任意的挂起函数。
     */
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
                if (realtimeResponse.status == "ok" && futureResponse.status == "ok"){
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