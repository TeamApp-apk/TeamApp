package com.example.TeamApp.event

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.TeamApp.auth.LoginScreen
import com.example.TeamApp.auth.RegisterScreen
import com.example.TeamApp.event.CreateEventScreen
import com.example.TeamApp.event.CreateEventViewModel
import com.example.TeamApp.profile.ProfileScreen
import com.example.TeamApp.profile.SearchScreen
import com.example.TeamApp.settings.SettingsScreen
import com.example.TeamApp.utils.SystemUiUtils
import com.example.compose.TeamAppTheme


class CreateEventActivity : ComponentActivity() {
    private val createEventViewModel: CreateEventViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SystemUiUtils.configureSystemUi(this)
            val navController = rememberNavController()
            NavHost(navController = navController, startDestination = "createEvent") {
                composable("createEvent") { CreateEventScreen(navController) }
                composable("register") { RegisterScreen(navController) }
                composable("profile") { ProfileScreen(navController) }
                composable("settings") { SettingsScreen(navController) }
                composable("search") { SearchScreen(navController) }
                composable("login") { LoginScreen(navController) }
            }

        }
    }
}
