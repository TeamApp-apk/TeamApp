package com.example.TeamApp.settings

import android.content.Context
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.compose.TeamAppTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(navController: NavController) {
    val viewModel: SettingsViewModel = viewModel()
    val context = LocalContext.current
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Settings",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        SettingItem(
            title = "Change Password",
            onClick = { viewModel.navigateToForgotPasswordActivity(navController) }
        )

        SettingItem(
            title = "Notification Settings",
            onClick = {}
        )

        SettingItem(
            title = "Privacy Policy",
            onClick = {}
        )

        SettingItem(
            title = "Terms of Service",
            onClick = { /* Handle terms of service click */ }
        )

        SettingItem(
            title = "Log out",
            onClick = {viewModel.logout(context)}
        )
    }
}

@Composable
fun SettingItem(title: String, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = Color.LightGray)
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(16.dp)
        )
    }
}

//@Preview(showBackground = true)
//@Composable
//fun SettingsScreenPreview() {
//    TeamAppTheme {
//        SettingsScreen(viewModel = SettingsViewModel(), context = LocalContext.current)
//    }
//}