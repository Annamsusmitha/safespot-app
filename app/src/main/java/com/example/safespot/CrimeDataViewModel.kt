package com.example.safespot

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.safespot.network.CrimeIncident
import com.example.safespot.network.RetrofitInstance
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class CrimeDataViewModel : ViewModel() {
    private val _incidents = MutableStateFlow<List<CrimeIncident>>(emptyList())
    val incidents: StateFlow<List<CrimeIncident>> = _incidents

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

//    private val authorizationToken = "Bearer 5879|uHZHvp9X1fS7SlBgVDOsJ8Co6wCfwk5sNFvWgINL"
    private val authorizationToken = "Bearer 6204|HMW4agTixXsXWb9UEfcK182D1qeiy8SaOBeb1azY"

    fun fetchCrimeData(lon: Double, lat: Double, distance: String, start: String, end: String) {
        viewModelScope.launch {
            try {
                Log.d("CrimeDataViewModel", "Fetching incidents for location: $lat, $lon")
                Log.d("CrimeDataViewModel", "Request URL: https://zylalabs.com/api/3976/crime+data++api/4728/crime-incidents")
                Log.d("CrimeDataViewModel", "Authorization: $authorizationToken")

                val response = RetrofitInstance.api.getCrimeIncidents(
                    token = authorizationToken,
                    latitude = lat,
                    longitude = lon,
                    distance = distance,
                    startTime = start,
                    endTime = end
                )
                _incidents.value = response.incidents
                Log.d("CrimeDataViewModel", "Fetched ${response.incidents.size} incidents")
            } catch (e: Exception) {
                Log.e("CrimeDataViewModel", "Error fetching data: ${e.localizedMessage}")
                _errorMessage.value = "Error fetching data: ${e.localizedMessage}"
            }
        }
    }
}
