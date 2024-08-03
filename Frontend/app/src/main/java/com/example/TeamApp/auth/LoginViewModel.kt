package com.example.TeamApp.auth

import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.IntentSenderRequest
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.TeamApp.data.User
import com.example.TeamApp.event.CreateEventActivity
import com.example.TeamApp.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task

class LoginViewModel : ViewModel() {
    lateinit var signInLauncher: ActivityResultLauncher<IntentSenderRequest>
    private val _email = MutableLiveData("")
    val email: LiveData<String> = _email

    private val _password = MutableLiveData("")
    val password: LiveData<String> = _password

    private val _username = MutableLiveData("")
    val username: LiveData<String> = _username

    private val _loginSuccess = MutableLiveData<Boolean?>(null)
    val loginSuccess: LiveData<Boolean?> = _loginSuccess

    private val _registerSuccess = MutableLiveData<Boolean?>(null)
    val registerSuccess: LiveData<Boolean?> = _registerSuccess

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    fun onEmailChange(newUsername: String) {
        _email.value = newUsername
    }

    fun onPasswordChanged(newPassword: String) {
        _password.value = newPassword
    }

    fun logout(context: Context) {
        FirebaseAuth.getInstance().signOut()
        val intent = Intent(context, LoginActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        context.startActivity(intent)
    }

    fun onLoginClick(context: Context) {
        val email = _email.value ?: return
        val password = _password.value ?: return

        Log.d("LoginAttempt", "Attempting to log in with email: $email")

        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d("Login", "Login successful")
                    _loginSuccess.value = true
                    val intent = Intent(context, CreateEventActivity::class.java).apply {
                        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    }
                    context.startActivity(intent)
                } else {
                    Log.e("Login", "Login failed: ${task.exception?.message}")
                    _loginSuccess.value = false
                }
            }
    }

    fun signInWithGoogle(context: Context) {
        val clientId = context.getString(R.string.client_id)
        Log.d("LoginViewModel", "Client ID: $clientId")

        val signInRequest = BeginSignInRequest.builder()
            .setGoogleIdTokenRequestOptions(
                BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                    .setSupported(true)
                    .setServerClientId(clientId)
                    .setFilterByAuthorizedAccounts(false) // Allow all accounts
                    .build()
            )
            .build()

        Identity.getSignInClient(context).beginSignIn(signInRequest)
            .addOnSuccessListener { result ->
                Log.d("LoginViewModel", "Google Sign-In success: ${result.pendingIntent}")
                val intent = result.pendingIntent.intentSender
                signInLauncher.launch(IntentSenderRequest.Builder(intent).build())
            }
            .addOnFailureListener { e ->
                Log.e("LoginViewModel", "Google Sign-In failed", e)
                if (e is ApiException) {
                    Log.e("LoginViewModel", "Status Code: ${e.statusCode}")
                }
            }
    }

    fun getToLoginScreen(context: Context) {
        val intent = Intent(context, LoginActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        context.startActivity(intent)
    }

    fun getToRegisterScreen(context: Context){
        val intent = Intent(context, RegisterActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        context.startActivity(intent)
    }

    fun onRegisterClick(context: Context) {
        val email = _email.value ?: return
        val password = _password.value ?: return
        // Tymczasowo, niedodana implementacja rejestracji z loginem
        val username = "xyz"

        val db = Firebase.firestore

        val user = User(name = username, email = email)

        Log.d("RegisterAttempt", "Attempting to register with email: $email")
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    db.collection("users").add(user)
                    Log.d("Register", "Registration successful")
                    _registerSuccess.value = true
                } else {
                    Log.e("Register", "Registration failed: ${task.exception?.message}")
                    _registerSuccess.value = false
                }
            }
    }

    fun resetLoginRegisterSuccess() {
        _loginSuccess.value = null
        _registerSuccess.value = null
    }
}