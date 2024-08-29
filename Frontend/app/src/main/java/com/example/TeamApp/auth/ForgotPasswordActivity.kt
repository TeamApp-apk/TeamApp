package com.example.TeamApp.auth

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.TeamApp.event.CreateEventScreen
import com.example.TeamApp.profile.ProfileScreen
import com.example.TeamApp.searchThrough.SearchScreen
import com.example.TeamApp.settings.SettingsScreen
import com.example.TeamApp.utils.SystemUiUtils

class ForgotPasswordActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        // Make the content extend into the status bar and navigation bar areas
        WindowCompat.setDecorFitsSystemWindows(window, false)

        super.onCreate(savedInstanceState)

        // Ensure edge-to-edge content
        setEdgeToEdge()

        setContent {
            SystemUiUtils.configureSystemUi(this)
            val navController = rememberNavController()
            NavHost(navController = navController, startDestination = "changePassword") {
                composable("changePassword"){ ForgotPasswordScreen(navController) }
                composable("createEvent") { CreateEventScreen(navController) }
                composable("register") { RegisterScreen(navController) }
                composable("profile"){ ProfileScreen(navController) }
                composable("settings"){ SettingsScreen(navController) }
                //composable("search"){ SearchScreen(navController) }
            }
        }
    }

    private fun setEdgeToEdge() {
        WindowInsetsControllerCompat(window, window.decorView).apply {
            // Make status bar icons dark to contrast with light background
            isAppearanceLightStatusBars = true
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }
}
