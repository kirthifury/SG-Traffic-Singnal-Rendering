package com.example.trafficimage.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.example.trafficimage.api.ApiClient
import com.example.trafficimage.utils.ApiResponse
import com.example.trafficimage.utils.ApiStatus
import kotlinx.coroutines.Dispatchers
import java.lang.Exception

class TrafficViewModel() : ViewModel() {
    fun getTrafficCameras() = liveData(Dispatchers.IO) {
        emit(ApiResponse.progress(data = null))
        try {
            emit(
                ApiResponse(
                    apiStatus = ApiStatus.SUCCESS,
                    data = ApiClient.apiService.getTrafficImages("2021-01-12T13:00:00"),
                    message = null
                )
            )
        } catch (exception: Exception) {
            emit(
                ApiResponse(
                    apiStatus = ApiStatus.ERROR,
                    data = null,
                    message = exception.message ?: "Something went wrong!"
                )
            )

        }
    }
}