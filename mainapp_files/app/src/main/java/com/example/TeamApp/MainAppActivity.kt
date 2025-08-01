package com.example.TeamApp

import BottomNavBar
import ChatScreen
import ScrollDownChat

import UserViewModel
import android.os.Bundle
import android.util.Log
import coil.request.ImageRequest
import coil.ImageLoader
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.navArgument
import coil.imageLoader
import coil.request.CachePolicy
import com.android.volley.Request
import com.example.TeamApp.auth.ForgotPasswordScreen
import com.example.TeamApp.event.CreateEventScreen
import com.example.TeamApp.event.CreateEventViewModel
import com.example.TeamApp.event.DetailsScreen
//import com.example.TeamApp.event.MapScreen
import com.example.TeamApp.event.CreateEventViewModelProvider
import com.example.TeamApp.profile.ProfileScreen


import com.example.TeamApp.searchThrough.SearchScreen
import com.example.TeamApp.searchThrough.FiltersScreen
import com.example.TeamApp.settings.PrivacyPolicyScreen
import com.example.TeamApp.settings.SendMessageScreen
import com.example.TeamApp.settings.SettingsScreenv2
import com.example.TeamApp.settings.TermsOfUse
import com.example.TeamApp.settings.YourEventsScreen
import com.example.TeamApp.ui.LoadingScreen
import com.example.TeamApp.utils.SystemUiUtils
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.delay

class MainAppActivity : AppCompatActivity() {
    @OptIn(ExperimentalAnimationApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Log.d("MainAppActivity", "onCreate called")
            SystemUiUtils.configureSystemUi(this)

            val gradientColors = listOf(
                Color(0xFFE8E8E8),
                Color(0xFF007BFF)
            )
            val navController = rememberAnimatedNavController()
            var isBottomBarVisible by remember { mutableStateOf(true) }
            var isLoading by remember { mutableStateOf(true) }
            var showMainContent by remember { mutableStateOf(false) }
            var isRefreshing by remember { mutableStateOf(false) }

            val userViewModel: UserViewModel = viewModel()
            val user by userViewModel.user.observeAsState()
            val viewModel: CreateEventViewModel = CreateEventViewModelProvider.createEventViewModel
            LaunchedEffect(navController) {
//                navController.addOnDestinationChangedListener { _, destination, _ ->
//                    isBottomBarVisible = when (destination.route) {
//                        "details/{activityId}" -> false
//                        else -> true
//                    }
//                }
                isBottomBarVisible = true
            }
            //to do remain this screen to wait
            val context = LocalContext.current
            LaunchedEffect(Unit) {
                Log.d("LaunchedEffect", "LaunchedEffect(Unit) called - Initiating user fetch")
                val firebaseUser = FirebaseAuth.getInstance().currentUser
                firebaseUser?.email?.let { email ->
                    userViewModel.fetchUserFromFirestore(email)
                    Log.d("UserViewModel", "fetchUserFromFirestore called for email: $email")
                }
            }
            LaunchedEffect(user) {
                Log.d("LaunchedEffect", "LaunchedEffect(user) called - User state changed: $user")
                if (user != null) {
                    Log.d("LaunchedEffect1", "User data available: $user")

                    user?.avatar?.let { avatarUrl ->
                        val imageLoader = context.imageLoader
                        val request = ImageRequest.Builder(context)
                            .data(avatarUrl)
                            .memoryCachePolicy(CachePolicy.ENABLED)
                            .diskCachePolicy(CachePolicy.ENABLED)
                            .build()
                        imageLoader.enqueue(request)
                        Log.d("Avatar", "Prefetching avatar to shared cache: $avatarUrl")
                    }

                    // Inne akcje zależne od załadowania użytkownika
                    viewModel.fetchEvents() // Zakładając, że 'viewModel' to Twój MainViewModel
                    viewModel.initializeMapIfNeeded(context)
                    delay(1000) // Opcjonalne opóźnienie dla płynniejszego przejścia
                    isLoading = false
                    delay(500)  // Opcjonalne opóźnienie
                    showMainContent = true
                    Log.d("LaunchedEffect", "Main content will be shown. isLoading: $isLoading, showMainContent: $showMainContent")

                } else {
                    Log.d("LaunchedEffect1", "User is null. Waiting for user data or login.")
                }
            }

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Brush.linearGradient(colors = gradientColors))
            ) {

                AnimatedVisibility(
                    visible = isLoading,
                    enter = fadeIn(animationSpec = tween(400)) + scaleIn(initialScale = 1.0f, animationSpec = tween(400)),
                    exit = fadeOut(animationSpec = tween(400)) + scaleOut(targetScale = 0.5f, animationSpec = tween(400))
                ) {
                    LoadingScreen()
                }

                AnimatedVisibility(
                    visible = showMainContent,
                    enter = fadeIn(animationSpec = tween(900)),
                    exit = fadeOut(animationSpec = tween(900))
                ) {
                    Scaffold(
                        modifier = Modifier.navigationBarsPadding(),
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
                                    enterTransition = { fadeIn(animationSpec = tween(250)) },
                                    exitTransition = { fadeOut(animationSpec = tween(250)) },
                                    popEnterTransition = { fadeIn(animationSpec = tween(250)) },
                                    popExitTransition = { fadeOut(animationSpec = tween(250)) }
                                ) { CreateEventScreen(navController,userViewModel) }

                                composable(
                                    route = "search",
                                    enterTransition = { fadeIn(animationSpec = tween(250)) },
                                    exitTransition = { fadeOut(animationSpec = tween(250)) },
                                    popEnterTransition = { fadeIn(animationSpec = tween(250)) },
                                    popExitTransition = { fadeOut(animationSpec = tween(250)) }
                                ) { SearchScreen(navController) { isScrollingDown ->
                                    isBottomBarVisible = !isScrollingDown
                                } }

                                composable(
                                    route = "profile",
                                    enterTransition = { fadeIn(animationSpec = tween(250)) },
                                    exitTransition = { fadeOut(animationSpec = tween(250)) },
                                    popEnterTransition = { fadeIn(animationSpec = tween(250)) },
                                    popExitTransition = { fadeOut(animationSpec = tween(250)) }
                                ) { ProfileScreen(navController, userViewModel) }
                                composable(
                                    route = "chatList",
                                    enterTransition = { fadeIn(animationSpec = tween(300)) },
                                    exitTransition = { fadeOut(animationSpec = tween(300)) },
                                    popEnterTransition = { fadeIn(animationSpec = tween(300)) },
                                    popExitTransition = { fadeOut(animationSpec = tween(300)) }
                                ) {
                                    ScrollDownChat(navController)
                                }


                                composable(
                                    route = "settings",
                                    enterTransition = { fadeIn(animationSpec = tween(250)) },
                                    exitTransition = { fadeOut(animationSpec = tween(250)) },
                                    popEnterTransition = { fadeIn(animationSpec = tween(250)) },
                                    popExitTransition = { fadeOut(animationSpec = tween(250)) }
                                ) { SettingsScreenv2(navController) }

                                composable(
                                    route = "filterScreen",
                                    enterTransition = { fadeIn(animationSpec = tween(250)) },
                                    exitTransition = { fadeOut(animationSpec = tween(0)) },
                                    popEnterTransition = { fadeIn(animationSpec = tween(250)) },
                                    popExitTransition = { fadeOut(animationSpec = tween(0)) }
                                ) { FiltersScreen(navController) }

                                composable(
                                    route = "details/{activityId}",
                                    arguments = listOf(navArgument("activityId") { type = NavType.StringType }),
                                    enterTransition = {
                                        slideInHorizontally(
                                            initialOffsetX = { fullWidth -> fullWidth },
                                            animationSpec = tween(300)
                                        ) + fadeIn(animationSpec = tween(300))
                                    },
                                    exitTransition = {
                                        slideOutHorizontally(
                                            targetOffsetX = { fullWidth -> -fullWidth },
                                            animationSpec = tween(300)
                                        ) + fadeOut(animationSpec = tween(300))
                                    },
                                    popEnterTransition = {
                                        slideInHorizontally(
                                            initialOffsetX = { fullWidth -> -fullWidth },
                                            animationSpec = tween(300)
                                        ) + fadeIn(animationSpec = tween(300))
                                    },
                                    popExitTransition = {
                                        slideOutHorizontally(
                                            targetOffsetX = { fullWidth -> fullWidth },
                                            animationSpec = tween(300)
                                        ) + fadeOut(animationSpec = tween(300))
                                    }
                                ) { backStackEntry ->
                                    val activityId = backStackEntry.arguments?.getString("activityId") ?: return@composable
                                    DetailsScreen(navController, activityId, userViewModel)
                                }

                                composable(
                                    route = "yourEvents",
                                    enterTransition = { fadeIn(animationSpec = tween(300)) },
                                    exitTransition = { fadeOut(animationSpec = tween(300)) },
                                    popEnterTransition = { fadeIn(animationSpec = tween(300)) },
                                    popExitTransition = { fadeOut(animationSpec = tween(300)) }
                                ) { YourEventsScreen(navController) }

                                composable(
                                    route = "termsOfUse",
                                    enterTransition = { fadeIn(animationSpec = tween(300)) },
                                    exitTransition = { fadeOut(animationSpec = tween(300)) },
                                    popEnterTransition = { fadeIn(animationSpec = tween(300)) },
                                    popExitTransition = { fadeOut(animationSpec = tween(300)) }
                                ) { TermsOfUse(navController) }

                                composable(
                                    route = "privacyPolicy",
                                    enterTransition = { fadeIn(animationSpec = tween(300)) },
                                    exitTransition = { fadeOut(animationSpec = tween(300)) },
                                    popEnterTransition = { fadeIn(animationSpec = tween(300)) },
                                    popExitTransition = { fadeOut(animationSpec = tween(300)) }
                                ) { PrivacyPolicyScreen(navController)}

                                composable(
                                    route = "sendUsMessage",
                                    enterTransition = { fadeIn(animationSpec = tween(300)) },
                                    exitTransition = { fadeOut(animationSpec = tween(300)) },
                                    popEnterTransition = { fadeIn(animationSpec = tween(300)) },
                                    popExitTransition = { fadeOut(animationSpec = tween(300)) }
                                ) { SendMessageScreen() }

                                composable(
                                    route = "ForgotPassword",
                                    arguments = listOf(navArgument("activityId") { type = NavType.StringType }),
                                    enterTransition = { fadeIn(animationSpec = tween(300)) },
                                    exitTransition = { fadeOut(animationSpec = tween(300)) },
                                    popEnterTransition = { fadeIn(animationSpec = tween(300)) },
                                    popExitTransition = { fadeOut(animationSpec = tween(300)) }
                                ) { ForgotPasswordScreen(navController) }
                                composable(
                                    route = "chat/{activityId}",
                                    enterTransition = { fadeIn(animationSpec = tween(300)) },
                                    exitTransition = { fadeOut(animationSpec = tween(300)) },
                                    popEnterTransition = { fadeIn(animationSpec = tween(300)) },
                                    popExitTransition = { fadeOut(animationSpec = tween(300)) }
                                ) {
                                    backStackEntry ->
                                    val activityId = backStackEntry.arguments?.getString("activityId") ?: return@composable
                                    ChatScreen(navController, activityId, userViewModel)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
