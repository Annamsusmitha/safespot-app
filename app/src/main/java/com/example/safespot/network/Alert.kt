package com.example.safespot.network

data class Alert(
    val incident_offense: String,
    val incident_date: String,
    val incident_address: String,
    val incident_latitude: Double,
    val incident_longitude: Double
)


data class AlertLocation(
    val latitude: Double,
    val longitude: Double
)
