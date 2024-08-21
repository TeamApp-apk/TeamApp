package com.example.TeamApp.event

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import com.example.TeamApp.auth.LoginScreen
import com.example.TeamApp.auth.RegisterScreen
import com.example.TeamApp.profile.ProfileScreen
import com.example.TeamApp.searchThrough.SearchScreen
import com.example.TeamApp.settings.SettingsScreen
import com.example.TeamApp.utils.SystemUiUtils

class CreateEventActivity : ComponentActivity() {
    private val createEventViewModel: CreateEventViewModel by viewModels()

    @OptIn(ExperimentalAnimationApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SystemUiUtils.configureSystemUi(this)
            val navController = rememberAnimatedNavController()
            AnimatedNavHost(
                navController = navController,
                startDestination = "createEvent",
                enterTransition = {
                    slideInHorizontally(
                        initialOffsetX = { 1000 },
                        animationSpec = tween(1000)
                    ) + fadeIn(animationSpec = tween(1000))
                },
                exitTransition = {
                    slideOutHorizontally(
                        targetOffsetX = { -1000 },
                        animationSpec = tween(1000)
                    ) + fadeOut(animationSpec = tween(1000))
                }
            ) {
                composable("createEvent") { CreateEventScreen(navController) }
                composable("register") { RegisterScreen(navController) }
                composable("profile") { ProfileScreen(navController) }
                composable("settings") { SettingsScreen(navController) }

                // Custom animation for transition from CreateEvent to SearchScreen
                composable(
                    "search",
                    enterTransition = {
                        slideInHorizontally(
                            initialOffsetX = { 1000 },
                            animationSpec = tween(2000)
                        ) + fadeIn(animationSpec = tween(2000))
                    },
                    exitTransition = {
                        slideOutHorizontally(
                            targetOffsetX = { -1000 },
                            animationSpec = tween(2000)
                        ) + fadeOut(animationSpec = tween(2000))
                    }
                ) {
                    SearchScreen(navController)
                }

                composable("login") { LoginScreen(navController) }
            }
        }
    }
}