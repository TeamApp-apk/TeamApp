package com.example.TeamApp.auth

import SignInLauncher
import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.IntentSenderRequest
import androidx.activity.viewModels
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavController
import androidx.navigation.compose.composable
import com.example.TeamApp.MainAppActivity
import com.example.TeamApp.data.User
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.android.gms.common.api.ApiException
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.initialize
import kotlinx.coroutines.CoroutineExceptionHandler
import com.example.TeamApp.utils.SystemUiUtils
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.rememberAnimatedNavController


class LoginActivity : ComponentActivity(), SignInLauncher {
    val handler = CoroutineExceptionHandler { _, throwable ->
        // process the Throwable
        Log.e(TAG, "There has been an issue: ", throwable)
    }
    private val loginViewModel: LoginViewModel by viewModels()
    private lateinit var oneTapClient: SignInClient

    @OptIn(ExperimentalAnimationApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FirebaseApp.initializeApp(this)
        Firebase.initialize(this)

        // No need to initialize signInLauncher here

        if (FirebaseAuth.getInstance().currentUser != null) {
            startActivity(Intent(this, MainAppActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            })
            finish()
            return
        }

        enableEdgeToEdge()
        setContent {
            SystemUiUtils.configureSystemUi(this)
            val gradientColors = listOf(
                Color(0xFFE8E8E8),
                Color(0xFF007BFF)
            )
            val navController = rememberAnimatedNavController()
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(brush = Brush.linearGradient(colors = gradientColors))
            ) {
                ///to check, animations seems not to be overwritten
                AnimatedNavHost(
                    navController = navController,
                    startDestination = "login",
                    enterTransition = {
                        slideInHorizontally(
                            initialOffsetX = { it },
                            animationSpec = tween(durationMillis = 800, easing = FastOutSlowInEasing)
                        ) + fadeIn(animationSpec = tween(durationMillis = 800))
                    },
                    exitTransition = {
                        slideOutHorizontally(
                            targetOffsetX = { -it },
                            animationSpec = tween(durationMillis = 800, easing = FastOutSlowInEasing)
                        ) + fadeOut(animationSpec = tween(durationMillis = 800))
                    }
                ){
                    composable("register") { RegisterScreen(navController) }
                    composable("login") { LoginScreen(navController) }
                    composable("forgotPassword") { ForgotPasswordScreen(navController) }
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
                            Log.d("LoginActivity", "signInWithCredential:success")
                            val user = FirebaseAuth.getInstance().currentUser
                            val db = com.google.firebase.ktx.Firebase.firestore
                            user?.let { firebaseUser ->
                                val email = firebaseUser.email
                                val id = firebaseUser.uid // Firebase user ID
                                if (email != null) {
                                    db.collection("users").document(id).get()
                                        .addOnSuccessListener { document ->
                                            if (document.exists()) {
                                                Log.d("LoginActivity", "User already exists in the database.")
                                                goToMainAppActivity()
                                            } else {
                                                navController.navigate("sexName")
                                                val newUser = User(name = "xyz", email = email).apply {
                                                    this.userID = id
                                                    this.attendedEvents = mutableListOf()
                                                }
                                                db.collection("users").document(id)
                                                    .set(newUser)
                                                    .addOnSuccessListener {
                                                        Log.d("LoginActivity", "User successfully added to Firestore.")
                                                    }
                                                    .addOnFailureListener { exception ->
                                                        Log.e("LoginActivity", "Error adding user to Firestore.", exception)
                                                    }
                                            }
                                        }
                                        .addOnFailureListener { exception ->
                                            Log.e("LoginActivity", "Error checking user in the database.", exception)
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

    private fun goToMainAppActivity() {
        val intent = Intent(this, MainAppActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }

    override fun launchSignIn(intent: IntentSenderRequest) {
        loginViewModel.signInLauncher.launch(intent)
    }


}