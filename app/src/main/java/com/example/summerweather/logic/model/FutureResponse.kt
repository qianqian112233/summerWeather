package com.example.summerweather.logic.model

import com.google.gson.annotations.SerializedName
import java.util.Date

/*
按照彩云天气信息接口返回的未来几天天气的JSON数据格式定义的数据模型
该类中定义了所有的数据模型类，防止出现和其他接口的数据模型类有同名冲突的情况
返回的天气数据是【数组】形式的，采用List集合对JSON中的数据元素进行映射

 */
data class FutureResponse(val status: String, val result: Result) {
    //虽然和RealtimeResponse内部都包含一个Result类，但是不会冲突。
    data class Result(val daily: Daily)

    data class Daily(val temperature: List<Temperature>, val skycon: List<skycon>,
        @SerializedName("life_index") val lifeIndex: LifeIndex)

    data class Temperature(val max: Float, val min: Float)

    data class skycon(val value: String, val date: Date)

    data class LifeIndex(val coldRisk: List<LifeDescription>, val carWashing: List<LifeDescription>,
                         val ultraviolet: List<LifeDescription>, val dressing: List<LifeDescription>)

    data class LifeDescription(val desc: String)
}