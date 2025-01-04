package com.example.safespot.ui

import android.Manifest
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.safespot.CrimeDataViewModel
import com.example.safespot.network.CrimeIncident
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun PostLoginScreen(viewModel: CrimeDataViewModel = viewModel()) {
    val incidents by viewModel.incidents.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()

    val locationPermissionState = rememberPermissionState(permission = Manifest.permission.ACCESS_FINE_LOCATION)

    val currentDate = LocalDate.now()
    val oneMonthBackDate = currentDate.minusMonths(1)
    val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'00:00:00.000'Z'")
    val formattedCurrentDate = currentDate.format(dateFormatter)
    val formattedOneMonthBackDate = oneMonthBackDate.format(dateFormatter)

    var selectedIncident by remember { mutableStateOf<CrimeIncident?>(null) }

    LaunchedEffect(Unit) {
        if (!locationPermissionState.status.isGranted) {
            locationPermissionState.launchPermissionRequest()
        } else {
            viewModel.fetchCrimeData(
                lon = -87.6472903440513,
                lat = 41.9777476245164,
                distance = "5mi",
                start = formattedOneMonthBackDate,
                end = formattedCurrentDate
            )
        }
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color(0xFF2B2B2B)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Text(
                text = "Neighborhood Safety Dashboard",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFFFFFF00)
            )

            Spacer(modifier = Modifier.height(16.dp))

            when {
                !locationPermissionState.status.isGranted -> {
                    Text(
                        text = "Location permission is required to fetch nearby crime data.",
                        color = Color.Red,
                        fontSize = 14.sp
                    )
                }
                errorMessage != null -> {
                    Text(
                        text = "Error: $errorMessage",
                        color = Color.Red,
                        fontSize = 14.sp
                    )
                }
                incidents.isEmpty() -> {
                    Text(
                        text = "Fetching crime incidents...",
                        color = Color.LightGray,
                        fontSize = 14.sp
                    )
                }
                else -> {
                    Text(
                        text = "Recent Crime Incidents",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color(0xFFFFFF00) // Yellow text
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    LazyColumn(
                        contentPadding = PaddingValues(vertical = 8.dp)
                    ) {
                        items(incidents) { incident ->
                            IncidentCard(
                                incident = incident,
                                onClick = { selectedIncident = incident }
                            )
                        }
                    }
                }
            }
        }
    }

    if (selectedIncident != null) {
        IncidentDetailsDialog(
            incident = selectedIncident,
            onDismiss = { selectedIncident = null }
        )
    }
}

@Composable
fun IncidentCard(incident: CrimeIncident, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .clickable { onClick() }, // Handle card click
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = incident.incident_offense,
                color = Color(0xFF000000),
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Address: ${incident.incident_address}",
                color = Color(0xFF424242),
                fontSize = 14.sp
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Date: ${incident.incident_date}",
                color = Color(0xFF757575),
                fontSize = 12.sp
            )
        }
    }
}

@Composable
fun IncidentDetailsDialog(incident: CrimeIncident?, onDismiss: () -> Unit) {
    if (incident != null) {
        AlertDialog(
            onDismissRequest = { onDismiss() },
            confirmButton = {
                Button(onClick = onDismiss) {
                    Text(text = "Close")
                }
            },
            title = {
                Text(
                    text = "Incident Details",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF000000)
                )
            },
            text = {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Text(
                        text = "Offense: ${incident.incident_offense}",
                        fontSize = 16.sp,
                        color = Color(0xFF424242)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Description: ${incident.incident_offense_description}",
                        fontSize = 14.sp,
                        color = Color(0xFF757575)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Address: ${incident.incident_address}",
                        fontSize = 14.sp,
                        color = Color(0xFF757575)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Date: ${incident.incident_date}",
                        fontSize = 14.sp,
                        color = Color(0xFF757575)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Coordinates: ${incident.incident_latitude}, ${incident.incident_longitude}",
                        fontSize = 14.sp,
                        color = Color(0xFF757575)
                    )
                }
            }
        )
    }
}
