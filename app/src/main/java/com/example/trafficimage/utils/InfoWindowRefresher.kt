package com.example.trafficimage.utils

import com.google.android.gms.maps.model.Marker
import java.lang.Exception

class InfoWindowRefresher(var marker: Marker) : com.squareup.picasso.Callback{
    override fun onSuccess() {
        marker.showInfoWindow()
    }

    override fun onError(e: Exception?) {
    }
}