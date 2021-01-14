package com.example.trafficimage

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.trafficimage.adapters.TrafficImageAdapter
import com.example.trafficimage.models.Camera
import com.example.trafficimage.models.Item
import com.example.trafficimage.models.TrafficDetails
import com.example.trafficimage.utils.ApiStatus
import com.example.trafficimage.viewmodels.TrafficViewModel

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions

class MapsActivity : AppCompatActivity(), OnMapReadyCallback{

    private lateinit var mMap: GoogleMap
    private var trafficDetails: TrafficDetails? = null
    private var markerHashset: HashSet<String> = HashSet()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        val trafficViewmodel = ViewModelProviders.of(this).get(TrafficViewModel::class.java)
        trafficViewmodel.getTrafficCameras().observe(this, Observer {
            it?.let { response ->
                when (response.apiStatus) {
                    ApiStatus.SUCCESS -> {
                        trafficDetails = response.data
                        renderTrafficSignalMarkers()
                    }
                    ApiStatus.ERROR -> {
                        Toast.makeText(this, "Failed :: ${response.message}", Toast.LENGTH_LONG)
                            .show()
                    }
                    ApiStatus.LOADING -> {
                        Toast.makeText(this, "loading....", Toast.LENGTH_LONG).show()

                    }
                }
            }
        })
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMap.setInfoWindowAdapter(TrafficImageAdapter(this))
    }

    private fun renderTrafficSignalMarkers() {
        trafficDetails?.let { trafficDetails ->
            var boundsBuilder: LatLngBounds.Builder = LatLngBounds.Builder()
            for (items in trafficDetails.items) {
                for (camera in items.cameras) {
                    LatLng(camera.location.latitude, camera.location.longitude).let {
                        boundsBuilder.include(it)
                        addMarker(camera)
                    }
                }
            }
            mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(boundsBuilder.build(), 0))
        }
    }

    private fun addMarker(
        camera: Camera
    ) {
        LatLng(camera.location.latitude, camera.location.longitude).let {
            val marker = mMap.addMarker(MarkerOptions().position(it).title(camera.camera_id))
            marker.tag = camera
            mMap.moveCamera(CameraUpdateFactory.newLatLng(it))
        }
    }

     fun onMarkerClick(marker: Marker?): Boolean {
        val recentlyClickedMarker = marker
        marker?.let { marker ->
            if(!markerHashset.contains(marker.id)) {
                marker.showInfoWindow()
                marker.hideInfoWindow()
                Handler(Looper.getMainLooper()).postDelayed({
                    recentlyClickedMarker?.showInfoWindow()
                }, 150)

                markerHashset.add(marker.id)
                return true
            }
            else
            {
                marker.showInfoWindow()
                return true
            }
        }
        return true

    }


}