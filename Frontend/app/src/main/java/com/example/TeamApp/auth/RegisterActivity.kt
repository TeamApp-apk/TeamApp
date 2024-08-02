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


class RegisterActivity : ComponentActivity() {
    private val loginViewModel: LoginViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FirebaseApp.initializeApp(this)
        Firebase.initialize(this)
        //Jesli jeden raz zalogowalismy sie na urzadzeniu, to po zamknieciu aplikacji
        //nie chcemy znowu się logować.
        val auth = FirebaseAuth.getInstance()
        if (auth.currentUser != null) {
            // User is logged in, redirect to CreateEventActivity
            val intent = Intent(this, CreateEventActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
            return
        }
        enableEdgeToEdge()
        setContent {
            TeamAppTheme {
                RegisterScreen()
            }
        }
    }
}
