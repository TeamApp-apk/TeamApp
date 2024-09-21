package com.example.TeamApp.auth

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.navOptions
import com.example.TeamApp.excludedUI.AvatarSelection

@Composable
fun AvatarSelectionScreen() {
    AvatarSelection()
}

@Preview
@Composable
fun AvatarSelectionScreenPreview() {
    AvatarSelectionScreen()
}