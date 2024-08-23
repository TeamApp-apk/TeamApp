package com.example.TeamApp.searchThrough

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.TeamApp.auth.RegisterScreen
import com.example.TeamApp.event.CreateEventScreen
import com.example.TeamApp.event.CreateEventScreen1

import com.example.TeamApp.profile.ProfileScreen

import com.example.TeamApp.settings.SettingsScreen
import com.example.TeamApp.utils.SystemUiUtils
import com.example.compose.TeamAppTheme
class SearchThroughActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SystemUiUtils.configureSystemUi(this)
            val navController = rememberNavController()
            NavHost(navController = navController, startDestination = "search") {
                composable("createEvent") { CreateEventScreen(navController) }
                composable("register") { RegisterScreen(navController) }
                composable("profile"){ ProfileScreen(navController) }
                composable("settings"){ SettingsScreen(navController) }
                composable("search"){ SearchScreen(navController) }
            }
        }
    }
}

@Composable
fun Greeting2(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview2() {
    TeamAppTheme {
        Greeting2("Android")
    }
}