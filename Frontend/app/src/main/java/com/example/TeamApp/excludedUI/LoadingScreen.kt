package com.example.TeamApp.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

@Composable
fun LoadingScreen() {
    // Definiujemy gradientowe kolory tła
    val gradientColors = listOf(
        Color(0xFFE8E8E8), // Jasny szary kolor
        Color(0xFF007BFF)  // Niebieski kolor
    )

    // Ustawiamy tło gradientowe
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(brush = Brush.linearGradient(colors = gradientColors)), // Gradientowe tło
        contentAlignment = Alignment.Center
    ) {
        // Wskaźnik ładowania
        CircularProgressIndicator()
    }
}
