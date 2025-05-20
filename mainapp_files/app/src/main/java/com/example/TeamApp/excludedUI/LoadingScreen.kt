//package com.example.TeamApp.ui
//
//import androidx.compose.foundation.background
//import androidx.compose.foundation.layout.Box
//import androidx.compose.foundation.layout.fillMaxSize
//import androidx.compose.material3.CircularProgressIndicator
//import androidx.compose.runtime.Composable
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.graphics.Brush
//import androidx.compose.ui.graphics.Color
//
//@Composable
//fun LoadingScreen() {
//    Box(
//        modifier = Modifier
//            .fillMaxSize(),
//        contentAlignment = Alignment.Center
//    ) {
//        // Wskaźnik ładowania
//        CircularProgressIndicator()
//    }
//}
package com.example.TeamApp.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.TeamApp.R

@Composable
fun LoadingScreen() {
    // Granatowy kolor tła
    val navyColor = Color(0xFF250B40)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(navyColor),
        contentAlignment = Alignment.Center
    ) {
        // Logo TeamApp
        Image(
            painter = painterResource(id = R.drawable.ic_launcher_foreground),
            contentDescription = "Logo TeamApp",
            modifier = Modifier.size(290.dp)
        )
    }
}
