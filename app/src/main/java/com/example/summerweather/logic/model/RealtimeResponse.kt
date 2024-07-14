package com.example.summerweather.logic.model

import com.google.gson.annotations.SerializedName

/*
按照彩云天气信息接口返回的实时天气JSON数据格式定义的数据模型
该类中定义了所有的数据模型类，防止出现和其他接口的数据模型类有同名冲突的情况
{
    "status": "ok",
    "result": {
        "realtime": {
            "temperature": 23.16,
            "skycon": "WIND",
            "air_quality": {
                "aqi": { "chn": 17.0 }
            }
        }
    }
}
 */
data class RealtimeResponse(val status: String, val result: Result){
    data class Result(val realtime: Realtime)

    data class Realtime(val skycon: String, val temperature: Float,
        @SerializedName("air_quality") val airQuality: AirQuality)

    data class AirQuality(val aqi: AQI)

    data class AQI(val chn: Float)
}
