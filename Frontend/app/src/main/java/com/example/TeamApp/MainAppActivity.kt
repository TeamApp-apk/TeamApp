package com.example.TeamApp

import BottomNavBar
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.core.view.WindowCompat
import com.example.TeamApp.auth.RegisterScreen
import com.example.TeamApp.event.CreateEventScreen
import com.example.TeamApp.profile.ProfileScreen
import com.example.TeamApp.searchThrough.SearchScreen
import com.example.TeamApp.settings.SettingsScreen
import com.example.TeamApp.utils.SystemUiUtils
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.rememberAnimatedNavController

class MainAppActivity : ComponentActivity() {
    @OptIn(ExperimentalAnimationApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        setContent {
            Log.d("MainAppActivity", "onCreate called")
            SystemUiUtils.configureSystemUi(this)
            val navController = rememberAnimatedNavController()
            val gradientColors = listOf(
                Color(0xFFE8E8E8),
                Color(0xFF007BFF)
            )
            var isBottomBarVisible by remember { mutableStateOf(true) }
            Scaffold(
                bottomBar = {
                    if (isBottomBarVisible) {
                        BottomNavBar(navController)
                    }
                }
            ) { innerPadding ->
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Brush.linearGradient(colors = gradientColors))
                        .padding(innerPadding)
                ) {
                    AnimatedNavHost(
                        navController = navController,
                        startDestination = "createEvent"
                    ) {
                        composable(
                            route = "createEvent",
                            enterTransition = { fadeIn(animationSpec = tween(300)) },
                            exitTransition = { fadeOut(animationSpec = tween(300)) },
                            popEnterTransition = { fadeIn(animationSpec = tween(300)) },
                            popExitTransition = { fadeOut(animationSpec = tween(300)) }
                        ) { CreateEventScreen(navController) }

                        composable(
                            route = "search",
                            enterTransition = { fadeIn(animationSpec = tween(300)) },
                            exitTransition = { fadeOut(animationSpec = tween(300)) },
                            popEnterTransition = { fadeIn(animationSpec = tween(300)) },
                            popExitTransition = { fadeOut(animationSpec = tween(300)) }
                        ) { SearchScreen(navController) { isScrollingDown ->
                            isBottomBarVisible = !isScrollingDown
                        } }
                        composable(
                            route = "profile",
                            enterTransition = { fadeIn(animationSpec = tween(300)) },
                            exitTransition = { fadeOut(animationSpec = tween(300)) },
                            popEnterTransition = { fadeIn(animationSpec = tween(300)) },
                            popExitTransition = { fadeOut(animationSpec = tween(300)) }
                        ) { ProfileScreen(navController) }
                        composable(
                            route = "settings",
                            enterTransition = { fadeIn(animationSpec = tween(300)) },
                            exitTransition = { fadeOut(animationSpec = tween(300)) },
                            popEnterTransition = { fadeIn(animationSpec = tween(300)) },
                            popExitTransition = { fadeOut(animationSpec = tween(300)) }
                        ) { SettingsScreen(navController) }

                        // Add other destinations here
                    }
                }
            }
        }
    }
}