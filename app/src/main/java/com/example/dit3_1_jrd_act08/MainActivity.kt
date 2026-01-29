package com.example.dit3_1_jrd_act08

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Looper
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import com.example.dit3_1_jrd_act08.ui.theme.DIT31JRDAct08Theme
import com.google.android.gms.location.*
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker

class MainActivity : ComponentActivity() {
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var locationCallback: LocationCallback
    private var mapView: MapView? = null
    
    private val locationPermissionRequest = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        when {
            permissions.getOrDefault(Manifest.permission.ACCESS_FINE_LOCATION, false) -> {
                startLocationUpdates()
            }
            permissions.getOrDefault(Manifest.permission.ACCESS_COARSE_LOCATION, false) -> {
                startLocationUpdates()
            }
            else -> {
                // Permission denied
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        Configuration.getInstance().userAgentValue = packageName
        
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        
        setContent {
            DIT31JRDAct08Theme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MapScreen(
                        onMapReady = { map ->
                            mapView = map
                        }
                    )
                }
            }
        }
        
        checkLocationPermission()
    }
    
    private fun checkLocationPermission() {
        when {
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED -> {
                startLocationUpdates()
            }
            else -> {
                locationPermissionRequest.launch(
                    arrayOf(
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    )
                )
            }
        }
    }
    
    @SuppressLint("MissingPermission")
    private fun startLocationUpdates() {
        val locationRequest = LocationRequest.Builder(
            Priority.PRIORITY_HIGH_ACCURACY,
            5000
        ).apply {
            setMinUpdateIntervalMillis(2000)
        }.build()
        
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                locationResult.lastLocation?.let { location ->
                    currentLocation.value = GeoPoint(location.latitude, location.longitude)
                    currentLatitude.value = location.latitude
                    currentLongitude.value = location.longitude
                    
                    mapView?.let { map ->
                        map.controller.setCenter(currentLocation.value)
                        updateMarker(map, currentLocation.value!!)
                    }
                }
            }
        }
        
        fusedLocationClient.requestLocationUpdates(
            locationRequest,
            locationCallback,
            Looper.getMainLooper()
        )
    }
    
    private fun updateMarker(mapView: MapView, location: GeoPoint) {
        mapView.overlays.removeAll { it is Marker }
        
        val marker = Marker(mapView)
        marker.position = location
        marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
        marker.title = "Current Location"
        marker.snippet = "Lat: ${location.latitude}, Lng: ${location.longitude}"
        
        mapView.overlays.add(marker)
        mapView.invalidate()
    }
    
    override fun onResume() {
        super.onResume()
        mapView?.onResume()
    }
    
    override fun onPause() {
        super.onPause()
        mapView?.onPause()
    }
    
    override fun onDestroy() {
        super.onDestroy()
        fusedLocationClient.removeLocationUpdates(locationCallback)
        mapView?.onDetach()
    }
    
    companion object {
        val currentLocation = mutableStateOf<GeoPoint?>(null)
        val currentLatitude = mutableStateOf<Double?>(null)
        val currentLongitude = mutableStateOf<Double?>(null)
    }
}

@Composable
fun MapScreen(onMapReady: (MapView) -> Unit) {
    val location by MainActivity.currentLocation
    val latitude by MainActivity.currentLatitude
    val longitude by MainActivity.currentLongitude
    
    Box(modifier = Modifier.fillMaxSize()) {
        AndroidView(
            factory = { context ->
                MapView(context).apply {
                    setTileSource(TileSourceFactory.MAPNIK)
                    setMultiTouchControls(true)
                    controller.setZoom(15.0)
                    
                    val defaultLocation = GeoPoint(14.5995, 120.9842)
                    controller.setCenter(location ?: defaultLocation)
                    
                    onMapReady(this)
                }
            },
            modifier = Modifier.fillMaxSize(),
            update = { mapView ->
                location?.let {
                    mapView.controller.setCenter(it)
                }
            }
        )
        
        Card(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(16.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "GPS Location Tracker",
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.height(8.dp))
                if (latitude != null && longitude != null) {
                    Text(
                        text = "Latitude: ${String.format("%.6f", latitude)}",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Text(
                        text = "Longitude: ${String.format("%.6f", longitude)}",
                        style = MaterialTheme.typography.bodyMedium
                    )
                } else {
                    Text(
                        text = "Waiting for location...",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }
    }
}