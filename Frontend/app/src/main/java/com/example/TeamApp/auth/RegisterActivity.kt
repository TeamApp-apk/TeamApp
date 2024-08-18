package com.example.TeamApp.auth

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalConfiguration
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.background
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.TeamApp.data.User
import com.example.TeamApp.event.CreateEventActivity
import com.example.TeamApp.event.CreateEventScreen
import com.example.TeamApp.ui.LoadingScreen
import com.example.TeamApp.utils.SystemUiUtils
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.android.gms.common.api.ApiException
import com.google.firebase.Firebase
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.initialize
import kotlinx.coroutines.delay


class RegisterActivity : ComponentActivity() {
    private val loginViewModel: LoginViewModel by viewModels()
    private lateinit var oneTapClient: SignInClient
    private var isFirstLaunch by mutableStateOf(true)

    @OptIn(ExperimentalAnimationApi::class)
    @SuppressLint("UnrememberedMutableState")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.auto(android.graphics.Color.TRANSPARENT, android.graphics.Color.TRANSPARENT),
            SystemBarStyle.auto(android.graphics.Color.TRANSPARENT, android.graphics.Color.TRANSPARENT)
        )
        FirebaseApp.initializeApp(this)
        Firebase.initialize(this)
        oneTapClient = Identity.getSignInClient(this)
        val auth = FirebaseAuth.getInstance()
        if (auth.currentUser != null) {
            val intent = Intent(this, CreateEventActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
            return
        }
        enableEdgeToEdge()
        setContent {
            val gradientColors = listOf(
                Color(0xFFE8E8E8),
                Color(0xFF007BFF)
            )
            SystemUiUtils.configureSystemUi(this)
            var isLoading by remember { mutableStateOf(true) }
            var showMainContent by remember { mutableStateOf(false) }

            LaunchedEffect(Unit) {
                delay(400) // Simulate loading time
                isLoading = false

                delay(850) // Ensure the loading screen transitions out fully before showing main content
                showMainContent = true
            }

            val navController = rememberAnimatedNavController()

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(brush = Brush.linearGradient(colors = gradientColors))
            ) {
                // Loading screen
                AnimatedVisibility(
                    visible = isLoading,
                    enter = slideInVertically(
                        initialOffsetY = { it },
                        animationSpec = tween(
                            durationMillis = 1000,
                            easing = FastOutSlowInEasing
                        )
                    ) + fadeIn(animationSpec = tween(durationMillis = 1000)),
                    exit = slideOutVertically(
                        targetOffsetY = { it },
                        animationSpec = tween(
                            durationMillis = 1000,
                            easing = FastOutSlowInEasing
                        )
                    ) + fadeOut(animationSpec = tween(durationMillis = 1000))
                ) {
                    LoadingScreen()
                }
                // Main content
                val configuration = LocalConfiguration.current
                val screenHeight = configuration.screenHeightDp
                AnimatedVisibility(
                    visible = showMainContent,
                    enter = slideInVertically(
                        initialOffsetY = { 2000 },
                        animationSpec = tween(
                            durationMillis = 800,
                            easing = FastOutSlowInEasing
                        )
                    ) + fadeIn(animationSpec = tween(durationMillis = 1200)),
                    exit = slideOutVertically(
                        targetOffsetY = { -1000 },
                        animationSpec = tween(
                            durationMillis = 1500,
                            easing = FastOutSlowInEasing
                        )
                    ) + fadeOut(animationSpec = tween(durationMillis = 1200))
                ) {
                    AnimatedNavHost(
                        navController = navController,
                        startDestination = "register",
                        enterTransition = {
                            slideInHorizontally(
                                initialOffsetX = { -1000 },
                                animationSpec = tween(
                                    durationMillis = 800,
                                    easing = FastOutSlowInEasing
                                )
                            ) + fadeIn(animationSpec = tween(durationMillis = 800))
                        },
                        exitTransition = {
                            slideOutHorizontally(
                                targetOffsetX = { -1000 },
                                animationSpec = tween(
                                    durationMillis = 800,
                                    easing = FastOutSlowInEasing
                                )
                            ) + fadeOut(animationSpec = tween(durationMillis = 800))
                        }
                    ) {
                        composable("register") { RegisterScreen(navController) }
                        composable("login") { LoginScreen(navController) }
                        composable("createEvent") { CreateEventScreen(navController) }
                        composable("forgotPassword") { ForgotPasswordScreen(navController) }
                    }
                }
            }
        }
    }



    fun handleSignInResult(data: Intent?, navController: NavController) {
        try {
            val credential = oneTapClient.getSignInCredentialFromIntent(data)
            val idToken = credential.googleIdToken
            if (idToken != null) {
                val firebaseCredential = GoogleAuthProvider.getCredential(idToken, null)
                FirebaseAuth.getInstance().signInWithCredential(firebaseCredential)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            Log.d("RegisterActivity", "signInWithCredential:success")
                            val user = FirebaseAuth.getInstance().currentUser
                            updateUI(user, navController)
                            val db = com.google.firebase.ktx.Firebase.firestore
                            user?.let { firebaseUser ->
                                val email = firebaseUser.email
                                if (email != null) {
                                    db.collection("users")
                                        .whereEqualTo("email", email)
                                        .get()
                                        .addOnSuccessListener { documents ->
                                            if (documents.isEmpty) {
                                                db.collection("users").add(User(name = "xyz", email = email))
                                            } else {
                                                Log.d("LoginActivity", "Email already exists in the database.")
                                            }
                                        }
                                        .addOnFailureListener { exception ->
                                            Log.e("LoginActivity", "Error checking email in the database.", exception)
                                        }
                                } else {
                                    Log.w("LoginActivity", "User email is null")
                                }
                            }
                        } else {
                            Log.w("LoginActivity", "signInWithCredential:failure", task.exception)
                        }
                    }
            } else {
                Log.d("LoginActivity", "No ID token!")
            }
        } catch (e: ApiException) {
            Log.e("LoginActivity", "Google Sign-In failed", e)
        }
    }

    private fun updateUI(user: FirebaseUser?, navController: NavController) {
        if (user != null) {
            // Użyj NavController do nawigacji
            navController.navigate("createEvent")
        } else {
            Log.e("LoginActivity", "Sign-in failed")
        }
    }
}
