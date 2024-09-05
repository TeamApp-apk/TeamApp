package com.example.TeamApp.event
import DescriptionTextField
import android.content.Context
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavController
import com.example.TeamApp.R
import com.example.TeamApp.excludedUI.EventButton
import com.google.android.gms.maps.model.LatLng
import com.tomtom.sdk.location.GeoPoint
import com.tomtom.sdk.map.display.MapOptions
import com.tomtom.sdk.map.display.camera.CameraOptions
import com.tomtom.sdk.map.display.common.screen.Padding
import com.tomtom.sdk.map.display.map.OnlineCachePolicy
import com.tomtom.sdk.map.display.style.StyleMode
import com.tomtom.sdk.map.display.ui.MapView
import java.util.Properties


@Composable
fun DetailsScreen(navController: NavController, activityId: String) {
    val viewModel: CreateEventViewModel = ViewModelProvider.createEventViewModel
    val event = viewModel.getEventById(activityId)
    var geoPoint by remember { mutableStateOf<GeoPoint?>(null) }
    val locationQuery = event?.location ?: ""

    val gradientColors = listOf(
        Color(0xFFE8E8E8),
        Color(0xFF007BFF)
    )

    var isJoined by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(brush = Brush.linearGradient(colors = gradientColors))
            .padding(horizontal = 16.dp, vertical = 36.dp),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxSize()
                .background(color = Color(0xFFF2F2F2), shape = RoundedCornerShape(size = 16.dp))
        ) {
            Column(
                modifier = Modifier
                    .padding(8.dp)
                    .navigationBarsPadding() // Automatyczny padding na dole, jeśli jest navbar
            ) {
                // Header
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 24.dp)
                ) {
                    IconButton(
                        onClick = { navController.popBackStack() },
                        modifier = Modifier.size(26.dp)
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.arrowleft),
                            contentDescription = "Back Icon"
                        )
                    }

                    Text(
                        text = "Szczegóły wydarzenia",
                        style = TextStyle(
                            fontSize = 22.sp,
                            fontFamily = FontFamily(Font(R.font.robotobold)),
                            fontWeight = FontWeight(900),
                            color = Color(0xFF003366),
                            textAlign = TextAlign.Start,
                            lineHeight = 25.sp,
                        ),
                        modifier = Modifier.align(Alignment.CenterVertically)
                    )
                }

                // Event details
                if (event != null) {
                    DescriptionTextField(
                        label = "Opis wydarzenia",
                        isEditable = false,
                        text = event.description
                    )
                }

                // Location label
                Text(
                    text = "Lokalizacja:",
                    style = TextStyle(
                        fontSize = 22.sp,
                        fontFamily = FontFamily(Font(R.font.robotobold)),
                        fontWeight = FontWeight(900),
                        color = Color(0xFF003366),
                        textAlign = TextAlign.Start,
                        lineHeight = 25.sp,
                    ),
                    modifier = Modifier.padding(bottom = 20.dp)
                )

                // Map View
                if (event != null && event.location != null) {
                    // Parse the location string into LatLng
                    val location = parseLocation(event.location)

                    // Display the map only if location is valid
                    if (location != null) {
                        TomTomMapView(context = LocalContext.current, coordinates = location)
                    } else {
                        Text(text = "Invalid location data")
                    }
                }

                // Join status row
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 20.dp)
                ) {
                    Image(
                        painter = painterResource(id = if (isJoined) R.drawable.joinstatuson else R.drawable.joinstatusoff),
                        contentDescription = "Join status",
                        modifier = Modifier
                            .wrapContentWidth()
                            .height(24.dp)
                    )
                    Spacer(Modifier.width(8.dp))
                    Text(
                        text = if (isJoined) "dołączono" else "nie dołączono",
                        style = TextStyle(
                            fontSize = 14.sp,
                            fontFamily = FontFamily(Font(R.font.robotoblackitalic)),
                            fontWeight = FontWeight.ExtraBold,
                            color = Color.Black,
                        )
                    )
                }

                // Action buttons
                Column(
                    modifier = Modifier
                        .padding(horizontal = 24.dp)
                        .navigationBarsPadding() // Dodaj to tutaj
                ) {
                    EventButton(
                        text = if (isJoined) "OPUŚĆ" else "DOŁĄCZ",
                        onClick = { isJoined = !isJoined }
                    )
                    EventButton(
                        text = "CZAT",
                        onClick = { /*TODO*/ }
                    )
                }
            }
        }
    }
}


@Composable
fun TomTomMapView(context: Context, coordinates: LatLng) {
    val localContext = LocalContext.current // Ensure correct LocalContext usage

    AndroidView(
        factory = { ctx ->
            // Create MapOptions for initializing MapView
            val mapOptions = MapOptions(
                mapKey = getApiKey(localContext), // Retrieve your actual TomTom API key
                cameraOptions = CameraOptions(
                    position = GeoPoint(coordinates.latitude, coordinates.longitude), // Set camera position from passed coordinates
                    zoom = 15.0, // Set zoom level
                    tilt = 0.0, // Optional tilt
                    rotation = 0.0 // Optional rotation
                ),
                padding = Padding(), // Default padding
                mapStyle = null, // Optional, or you can set a custom style
                styleMode = StyleMode.MAIN, // Default style mode
                onlineCachePolicy = OnlineCachePolicy.Default, // Default caching policy
                renderToTexture = false // Default value
            )
            val mapView = MapView(ctx, mapOptions)
            mapView.onCreate(null)
            mapView.getMapAsync { tomTomMap ->
                try {
                    // Set camera options to the map
                    tomTomMap.moveCamera(CameraOptions(
                        position = GeoPoint(coordinates.latitude, coordinates.longitude), // Dynamic coordinates
                        zoom = 15.0 // Set zoom level
                    ))

                    // You can add markers, overlays, or interact with the map here
                } catch (e: Exception) {
                    Log.e("TomTomMapView", "Error setting up map: ${e.message}")
                }
            }

            // Return the MapView instance
            mapView
        },
        modifier = Modifier
            .fillMaxWidth()
            .height(220.dp)
    )
}

fun parseLocation(location: String): LatLng? {
    return try {
        val parts = location.split(",").map { it.trim() }
        if (parts.size == 2) {
            val latitude = parts[0].toDoubleOrNull()
            val longitude = parts[1].toDoubleOrNull()
            if (latitude != null && longitude != null) {
                LatLng(latitude, longitude)
            } else null
        } else null
    } catch (e: Exception) {
        null // Handle invalid format
    }
}




