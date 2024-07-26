package com.example.TeamApp

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.example.compose.TeamAppTheme
import com.google.firebase.Firebase
import com.google.firebase.FirebaseApp
import com.google.firebase.firestore.ktx.firestore
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.Logger
import com.google.firebase.database.ktx.database
import com.google.firebase.firestore.firestore
import com.google.firebase.initialize
import com.google.firebase.ktx.initialize

class MainActivity : ComponentActivity() {
    private val loginViewModel: LoginViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FirebaseApp.initializeApp(this)
        Firebase.initialize(this)
//        val db = Firebase.firestore
//        // Write a message to the database
//        val user = hashMapOf(
//            "first" to "User2",
//            "last" to "user2",
//            "born" to 1815
//        )
//        db.collection("users")
//            .add(user)
//            .addOnSuccessListener { documentReference ->
//                Log.d("TAG", "DocumentSnapshot added with ID: ${documentReference.id}")
//            }
//            .addOnFailureListener { e ->
//                Log.w("TAG", "Error adding document", e)
//            }
        enableEdgeToEdge()
        setContent {
            TeamAppTheme {
                TeamApp(loginViewModel)
            }
        }
    }

    companion object {
        private const val TAG = "MainActivity"
    }
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun TeamApp(loginViewModel: LoginViewModel) {
    val navController = rememberNavController()
    Scaffold(
        content = {
            Box(modifier = Modifier.fillMaxSize()) {
                LoginScreen(loginViewModel)
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
