package com.example.safespot.network

import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface CrimeDataApiService {
    @GET("crime-incidents")
    suspend fun getCrimeIncidents(
        @Header("Authorization") token: String,
        @Query("lat") latitude: Double,
        @Query("lon") longitude: Double,
        @Query("distance") distance: String,
        @Query("datetime_ini") startTime: String,
        @Query("datetime_end") endTime: String
    ): CrimeResponse
}

data class CrimeResponse(
    val incidents_count: Int,
    val pages_count: Int,
    val incidents: List<CrimeIncident>
)

data class CrimeIncident(
    val incident_offense: String,
    val incident_date: String,
    val incident_address: String,
    val incident_latitude: Double,
    val incident_longitude: Double,
    val incident_offense_description: String
)
