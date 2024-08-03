package com.example.TeamApp.auth

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.example.compose.TeamAppTheme
import com.google.firebase.Firebase
import com.google.firebase.FirebaseApp
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.initialize
import android.content.Intent
import com.example.TeamApp.event.CreateEventActivity


class LoginActivity : ComponentActivity() {
    private val loginViewModel: LoginViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FirebaseApp.initializeApp(this)
        Firebase.initialize(this)
        //Jesli jeden raz zalogowalismy sie na urzadzeniu, to po zamknieciu aplikacji
        //nie chcemy znowu się logować.

        enableEdgeToEdge()
        setContent {
            TeamAppTheme {
                TeamApp(loginViewModel)
            }
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        // Przekierowanie z powrotem do ekranu rejestracji
        val intent = Intent(this, RegisterActivity::class.java)
        startActivity(intent)
        finish() // Opcjonalne, aby zamknąć `LoginActivity` i zapobiec wracaniu do niej po naciśnięciu "Back"
    }
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun TeamApp(loginViewModel: LoginViewModel) {
    val navController = rememberNavController()
    Scaffold(
        content = {
            Box(modifier = Modifier.fillMaxSize()) {
                LoginScreen()
            }
        }
    )
}

fun checkNetworkConnectivity(context: Context) {
    val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val networkCapabilities = connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
    val isConnected = networkCapabilities != null && networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)

    if (isConnected) {
        Toast.makeText(context, "Network is available", Toast.LENGTH_SHORT).show()
    } else {
        Toast.makeText(context, "Network is not available", Toast.LENGTH_SHORT).show()
    }
}

@Preview
@Composable
fun MainPreview() {
    val loginViewModel: LoginViewModel = viewModel()
    TeamAppTheme {
        TeamApp(loginViewModel)
    }
}
