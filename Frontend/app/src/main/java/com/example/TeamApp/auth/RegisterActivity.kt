package com.example.TeamApp.auth

import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.TeamApp.data.User
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.android.gms.common.api.ApiException
import com.example.TeamApp.event.CreateEventActivity
import com.example.TeamApp.event.CreateEventScreen
import com.example.TeamApp.ui.LoadingScreen
import com.example.TeamApp.utils.SystemUiUtils
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.initialize
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.delay

class RegisterActivity : ComponentActivity() {
    private val loginViewModel: LoginViewModel by viewModels()
    private lateinit var oneTapClient: SignInClient
    private var isFirstLaunch by mutableStateOf(true)

    @SuppressLint("UnrememberedMutableState")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.auto(Color.TRANSPARENT, Color.TRANSPARENT),
            SystemBarStyle.auto(Color.TRANSPARENT, Color.TRANSPARENT)
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
            SystemUiUtils.configureSystemUi(this)
            var isLoading by remember { mutableStateOf(true) }
            LaunchedEffect(Unit) {
                delay(1000)
                isLoading = false
            }
            val navController = rememberNavController() as NavHostController

            AnimatedVisibility(
                visible = isLoading,
                enter = slideInVertically(initialOffsetY = { it }) + fadeIn(),
                exit = slideOutVertically(targetOffsetY = { it }) + fadeOut()
            ) {
                LoadingScreen()
            }

            AnimatedVisibility(
                visible = !isLoading,
                enter = slideInVertically(initialOffsetY = { it }) + fadeIn(),
                exit = slideOutVertically(targetOffsetY = { it }) + fadeOut()
            ) {
                NavHost(navController = navController, startDestination = "register") {
                    composable("register") { RegisterScreen(navController) }
                    composable("login") { LoginScreen(navController) }
                    composable("createEvent") { CreateEventScreen(navController) }
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
