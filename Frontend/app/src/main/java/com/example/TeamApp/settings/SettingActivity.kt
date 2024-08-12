package com.example.TeamApp.settings

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.TeamApp.auth.ForgotPasswordScreen
import com.example.TeamApp.auth.RegisterScreen
import com.example.TeamApp.event.CreateEventScreen
import com.example.TeamApp.profile.ProfileScreen
import com.example.TeamApp.profile.SearchScreen
import com.example.TeamApp.utils.SystemUiUtils


class SettingActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SystemUiUtils.configureSystemUi(this)
            val navController = rememberNavController()
            NavHost(navController = navController, startDestination = "settings") {
                composable("createEvent") { CreateEventScreen(navController) }
                composable("register") { RegisterScreen(navController) }
                composable("profile"){ ProfileScreen(navController) }
                composable("settings"){ SettingsScreen(navController) }
                composable("search"){ SearchScreen(navController) }
                composable("changePassword"){ForgotPasswordScreen(navController)}
            }
        }
    }
}
