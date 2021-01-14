package com.example.trafficimage.api

import com.example.trafficimage.models.TrafficDetails
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET("traffic-images?")
    suspend fun getTrafficImages(@Query("date_time")timeStamp: String) : TrafficDetails
}