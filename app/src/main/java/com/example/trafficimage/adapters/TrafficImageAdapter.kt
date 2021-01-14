package com.example.trafficimage.adapters

import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.util.Log
import android.view.View
import android.widget.ImageView
import com.example.trafficimage.R
import com.example.trafficimage.models.Camera
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.Marker
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import com.squareup.picasso.Target

class TrafficImageAdapter(context: Context) : GoogleMap.InfoWindowAdapter {

    private val mWindow: View =
        (context as Activity).layoutInflater.inflate(R.layout.traffic_image_layout, null)

    private fun showInfoWindow(marker: Marker?, view: View) {
        marker?.let {
            val imageView = view.findViewById<ImageView>(R.id.traffic_image)
            var target = object : Target {
                override fun onBitmapFailed(e: java.lang.Exception?, errorDrawable: Drawable?) {
                    view.findViewById<ImageView>(R.id.traffic_image)
                        .setImageResource(android.R.drawable.stat_notify_error)
                }

                override fun onPrepareLoad(placeHolderDrawable: Drawable?) {
                }

                override fun onBitmapLoaded(bitmap: Bitmap?, from: Picasso.LoadedFrom?) {
                    Log.i("Test123","onBitmapLoaded ${marker.tag}")
                    imageView.setImageBitmap(bitmap)
                }
            }
            imageView.tag = target
            var cameraDetails: Camera = marker.tag as Camera

                Picasso.get().load(cameraDetails.image)
                    .into(imageView)
            // Glide.with(context).asBitmap().load(Uri.parse("https://images.data.gov.sg/api/traffic-images/2021/01/a29cfa4d-55db-4ffb-ac4d-67eabc924867.jpg"))
            //  .placeholder(R.drawable.ic_launcher_background)
            // .into(view.findViewById(R.id.traffic_image) as ImageView)
        }
    }

    override fun getInfoContents(marker: Marker?): View? {
        Log.i("Test123","getInfoWindow ${marker?.tag}")
        showInfoWindow(marker, mWindow)
        return mWindow
    }

    override fun getInfoWindow(marker: Marker?): View {
        Log.i("Test123","getInfoWindow ${marker?.tag}")
        showInfoWindow(marker, mWindow)
        return mWindow
    }
}