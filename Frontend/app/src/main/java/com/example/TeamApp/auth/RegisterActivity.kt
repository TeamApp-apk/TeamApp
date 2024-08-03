package com.example.TeamApp.auth

import SignInLauncher
import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.compose.TeamAppTheme
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.android.gms.common.api.ApiException
import com.example.TeamApp.event.CreateEventActivity
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.initialize

class RegisterActivity : ComponentActivity(), SignInLauncher {
    private val loginViewModel: LoginViewModel by viewModels()
    private lateinit var oneTapClient: SignInClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
        setContent {
            TeamAppTheme {
                RegisterScreen()
            }
        }

        loginViewModel.signInLauncher = registerForActivityResult(ActivityResultContracts.StartIntentSenderForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                val data = result.data
                handleSignInResult(data)
            } else {
                Log.e("RegisterActivity", "Google Sign-In failed")
            }
        }
    }

    private fun handleSignInResult(data: Intent?) {
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
                            updateUI(user)
                        } else {
                            Log.w(
                                "RegisterActivity",
                                "signInWithCredential:failure",
                                task.exception
                            )
                            updateUI(null)
                        }
                    }
            } else {
                Log.d("RegisterActivity", "No ID token!")
            }
        } catch (e: ApiException) {
            Log.e("RegisterActivity", "Google Sign-In failed", e)
        }
    }

    private fun updateUI(user: FirebaseUser?) {
        if (user != null) {
            val intent = Intent(this, CreateEventActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        } else {
            Log.e("RegisterActivity", "Sign-in failed")
        }
    }

    override fun launchSignIn(intent: IntentSenderRequest) {
        loginViewModel.signInLauncher.launch(intent)
    }

    companion object {
        private const val RC_SIGN_IN = 9001
    }
}