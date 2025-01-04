package com.example.safespot.location

import android.annotation.SuppressLint
import android.content.Context
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.tasks.await

class LocationProvider(context: Context) {
    private val fusedLocationClient: FusedLocationProviderClient =
        LocationServices.getFusedLocationProviderClient(context)

    @SuppressLint("MissingPermission") // Ensure permissions are handled
    suspend fun getCurrentLocation(): Pair<Double, Double>? {
        val location = fusedLocationClient.lastLocation.await()
        return location?.let {
            Pair(it.latitude, it.longitude)
        }
    }
}
