package com.example.TeamApp.auth

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.Dimension
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.navOptions
import com.example.TeamApp.excludedUI.AvatarSelection
import com.example.ui.theme.textInUpperBoxForgotPassword

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun AvatarSelectionScreen() {
    val viewModel: LoginViewModel = viewModel()
    val gradientColors = listOf(
        Color(0xFFE8E8E8),
        Color(0xFF007BFF),
    )
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.background(brush = Brush.linearGradient(colors = gradientColors)).fillMaxSize()

    ) {
        Text(
            textAlign = TextAlign.Center,
            style = textInUpperBoxForgotPassword,
            text = "Wybierz swój awatar ${viewModel.user.value!!.name}",
            modifier = Modifier.padding(16.dp)

        )
        AvatarSelection()
    }
}

@Preview
@Composable
fun AvatarSelectionScreenPreview() {
    AvatarSelectionScreen()
}