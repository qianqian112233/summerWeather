package com.example.summerweather.logic.model

data class Weather(val realtime: RealtimeResponse.Realtime,
    val future: FutureResponse.Daily)
