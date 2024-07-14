package com.example.summerweather.ui.place

import androidx.lifecycle.*
import com.example.summerweather.logic.model.Place
import com.example.summerweather.logic.Repository
import kotlinx.coroutines.Dispatchers

class PlaceViewModel : ViewModel() {
    private val searchLiveData = MutableLiveData<String>()
    val placeList = ArrayList<Place>()

    private val _placeLiveData = MediatorLiveData<Result<List<Place>>>()
    val placeLiveData: LiveData<Result<List<Place>>> get() = _placeLiveData

    init {
        _placeLiveData.addSource(searchLiveData) { query ->
            query?.let {
                // 开始新的搜索请求
                val source = Repository.searchPlaces(it)
                // 清除以前的结果源以免内存泄漏
                _placeLiveData.removeSource(source)

                // 添加新的结果源
                _placeLiveData.addSource(source) { result ->
                    _placeLiveData.value = result
                }
            }
        }
    }

//    val placeLiveData = Transformations.switchMap(searchLiveData) { query ->
//        Repository.searchPlaces(query)
//    }

//    val placeLiveData : LiveData<List<Place>> = searchLiveData.switchMap { query ->
//        Repository.searchPlaces(query)
//    }

    fun searchPlaces(query: String) {
        searchLiveData.value = query
    }


    fun savePlace(place: Place) = Repository.savePlace(place)

    fun getSavedPlace() = Repository.getSavedPlace()

    fun isPlaceSaved() = Repository.isPlaceSaved()

//    private val _placeLiveData = MediatorLiveData<Result<List<Place>>>()
//    val placeLiveData: LiveData<Result<List<Place>>> get() = _placeLiveData

//    init {
//        _placeLiveData.addSource(searchLiveData) { query ->
//            query?.let {
//                // 开始新的搜索请求
//                val source = Repository.searchPlaces(it)
//                // 清除以前的结果源以免内存泄漏
//                _placeLiveData.removeSource(source)
//
//                // 添加新的结果源
//                _placeLiveData.addSource(source) { result ->
//                    _placeLiveData.value = result
//                }
//            }
//        }
//    }

}