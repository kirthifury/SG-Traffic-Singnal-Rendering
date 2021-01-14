package com.example.trafficimage.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.example.trafficimage.api.ApiClient
import com.example.trafficimage.utils.ApiResponse
import com.example.trafficimage.utils.ApiStatus
import kotlinx.coroutines.Dispatchers
import java.text.SimpleDateFormat
import java.util.*

class TrafficViewModel() : ViewModel() {

    fun getTrafficCameras() = liveData(Dispatchers.IO) {
        emit(ApiResponse.progress(data = null))
        try {
            val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault()).format(Date())
            emit(
                ApiResponse(
                    apiStatus = ApiStatus.SUCCESS,
                    data = ApiClient.apiService.getTrafficImages(sdf),
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