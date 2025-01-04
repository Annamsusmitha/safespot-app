package com.example.safespot

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.safespot.location.LocationProvider
import com.example.safespot.network.CrimeIncident
import com.example.safespot.network.RetrofitInstance
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AlertsViewModel(application: Application) : AndroidViewModel(application) {
    private val locationProvider = LocationProvider(application.applicationContext)

    private val _alerts = MutableStateFlow<List<CrimeIncident>>(emptyList())
    val alerts: StateFlow<List<CrimeIncident>> = _alerts

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    private val _location = MutableStateFlow<String?>(null)
    val location: StateFlow<String?> = _location

    private val authorizationToken = "Bearer 5757|Nx5IdYQZZkqHxrdz5Hup0lurxDb16L45hwjAyjkm"

    init {
        fetchLocationAndIncidents()
    }

    private fun fetchLocationAndIncidents() {
        viewModelScope.launch {
            val currentLocation = locationProvider.getCurrentLocation()
            if (currentLocation != null) {
                val (latitude, longitude) = currentLocation
                _location.value = "$latitude, $longitude"

                try {
                    Log.d("AlertsViewModel", "Fetching alerts for location: $latitude, $longitude")
                    Log.d("AlertsViewModel", "Start Time: 2024-01-01T00:00:00.000Z, End Time: 2024-02-01T00:00:00.000Z")

                    // Fetch incidents from the API
                    val response = RetrofitInstance.api.getCrimeIncidents(
                        token = authorizationToken,
                        latitude = latitude,
                        longitude = longitude,
                        startTime = "2024-01-01T00:00:00.000Z",
                        endTime = "2024-02-01T00:00:00.000Z",
                        distance = "5mi"
                    )
                    _alerts.value = response.incidents
                    Log.d("AlertsViewModel", "Fetched ${response.incidents.size} incidents")
                } catch (e: Exception) {
                    val errorMessage = "Error fetching data: ${e.localizedMessage}"
                    Log.e("AlertsViewModel", errorMessage, e)
                    _errorMessage.value = errorMessage
                }
            } else {
                Log.d("AlertsViewModel", "Unable to determine current location")
                _location.value = "Location unavailable"
                _errorMessage.value = "Unable to determine current location"
            }
        }
    }
}
