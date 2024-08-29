package com.example.TeamApp.profile

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.TeamApp.auth.RegisterScreen
import com.example.TeamApp.event.CreateEventScreen
import com.example.TeamApp.searchThrough.SearchScreen
import com.example.TeamApp.settings.SettingsScreen
import com.example.TeamApp.utils.SystemUiUtils
import com.example.compose.TeamAppTheme
import com.google.accompanist.navigation.animation.composable

class ProfileActivity : ComponentActivity() {
    @OptIn(ExperimentalAnimationApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SystemUiUtils.configureSystemUi(this)
            val navController = rememberNavController()
            NavHost(navController = navController, startDestination = "profile") {
                composable("createEvent") { CreateEventScreen(navController) }
                composable("register") { RegisterScreen(navController) }
                composable("profile"){ProfileScreen(navController)}
                composable("settings"){ SettingsScreen(navController) }
                composable("settings"){ SettingsScreen(navController) }
                }
            }
        }
    }
