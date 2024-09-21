package com.example.TeamApp.auth

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.TeamApp.excludedUI.AvatarSelection

@Composable
fun AvatarSelectionScreen(viewModel: LoginViewModel) {
    AvatarSelection(viewModel = viewModel)
}

@Preview
@Composable
fun AvatarSelectionScreenPreview() {
    AvatarSelectionScreen(viewModel = LoginViewModel())
}