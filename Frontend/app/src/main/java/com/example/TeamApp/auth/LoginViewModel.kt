package com.example.TeamApp.auth

import android.content.Context
import android.content.Intent
import android.os.Looper
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
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.common.api.ApiException
import androidx.navigation.NavController

class LoginViewModel : ViewModel() {
    lateinit var signInLauncher: ActivityResultLauncher<IntentSenderRequest>
    private val _email = MutableLiveData("")
    private val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean> = _isLoading
    val email: LiveData<String> = _email

    fun setLoading(loading: Boolean) {
        Log.d("LoginViewModel", "setLoading: $loading")
        _isLoading.value = loading
    }
    fun mySetSignInLauncher(launcher: ActivityResultLauncher<IntentSenderRequest>) {
        signInLauncher = launcher
        if (!::signInLauncher.isInitialized) {
            Log.e("LoginViewModel", "signInLauncher has not been initialized")
        }
        else{
            Log.d("LoginViewModel", "signInLauncher has been initialized")
        }
    }

    private val _password = MutableLiveData("")
    val password: LiveData<String> = _password

    private val _username = MutableLiveData("")
    val username: LiveData<String> = _username

    private val _confirmPassword = MutableLiveData<String>()
    val confirmPassword: LiveData<String> = _confirmPassword

    private val _loginSuccess = MutableLiveData<Boolean?>(null)
    val loginSuccess: LiveData<Boolean?> = _loginSuccess

    fun onConfirmPasswordChanged(newConfirmPassword: String) {
        _confirmPassword.value = newConfirmPassword
    }

    private val _registerSuccess = MutableLiveData<Boolean?>(null)
    val registerSuccess: LiveData<Boolean?> = _registerSuccess

    private val _emailSent = MutableLiveData<Boolean?>(null)
    val emailSent: LiveData<Boolean?> = _emailSent

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    fun onEmailChange(newUsername: String) {
        _email.value = newUsername
    }

    fun onPasswordChanged(newPassword: String) {
        _password.value = newPassword
    }

    fun logout(navController: NavController) {
        FirebaseAuth.getInstance().signOut()
        navController.navigate("register"){
            popUpTo("createEvent") { inclusive = true }
        }
    }

    fun onLoginClick(navController: NavController) {
        val email = _email.value ?: return
        val password = _password.value ?: return
        setLoading(true)
        Log.d("LoginAttempt", "Attempting to log in with email: $email")

        if (email.isNotEmpty() && password.isNotEmpty()) {
            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    // Add a delay to simulate loading
                    android.os.Handler(Looper.getMainLooper()).postDelayed({
                        setLoading(false)
                        if (task.isSuccessful) {
                            Log.d("Login", "Login successful")
                            _loginSuccess.value = true
                            navController.navigate("createEvent") {
                                popUpTo("login") { inclusive = true }
                            }
                        } else {
                            Log.e("Login", "Login failed: ${task.exception?.message}")
                            _loginSuccess.value = false
                        }
                    }, 200) // delay
                }
        } else {
            Log.d("LoginAttempt", "Login failed: empty email or password field")
            setLoading(false)
        }
    }


    fun signInWithGoogle(context: Context) {
        if (!::signInLauncher.isInitialized) {
            Log.e("LoginViewModel", "signInLauncher has not been initialized")
            return
        }
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


    fun getToLoginScreen(navController: NavController) {
        navController.navigate("login")
    }

    fun getToRegisterScreen(navController: NavController) {
        navController.navigate("register")
    }
    fun getToChangePasswordScreen(navController: NavController){
        navController.navigate("forgotPassword") {
            popUpTo("login") { inclusive = true }
        }
    }
    fun onRegisterClick(navController: NavController) {
        val email = _email.value ?: return
        val password = _password.value ?: return
        val confirmPassword = _confirmPassword.value ?: return
        setLoading(true)
        // Tymczasowo, niedodana implementacja rejestracji z loginem
        val username = "xyz"

        val db = Firebase.firestore

        val user = User(name = username, email = email)

        if (password != confirmPassword) {
            Log.d("RegisterAttempt", "Register failed: passwords do not match")
            return
        }

        if (password.isNotEmpty() && email.isNotEmpty()) {
            Log.d("RegisterAttempt", "Attempting to register with email: $email")
            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    android.os.Handler(Looper.getMainLooper()).postDelayed({
                        setLoading(false)

                    if (task.isSuccessful) {
                        db.collection("users").add(user)
                        Log.d("Register", "Registration successful")
                        _registerSuccess.value = true
                      navController.navigate("CreateEvent") {
                          popUpTo("register") { inclusive = true }
                      }
                    } else {
                        Log.e("Register", "Registration failed: ${task.exception?.message}")
                        _registerSuccess.value = false
                    }
                }, 200) // delay
            }
        } else if (email.isEmpty()) {
            Log.d("RegisterAttempt", "Register failed: empty email field")
        }
    }


    fun resetSuccess() {
        _loginSuccess.postValue(null)
        _registerSuccess.postValue(null)
        _emailSent.postValue(null)
    }

    fun onForgotPasswordClick() {
        val email = _email.value ?: return
        if (email.isNullOrEmpty()) {
            Log.e("LoginViewModel", "Email is empty")
            _emailSent.value = false // Indicate that the operation was unsuccessful
            return
        }

        Log.d("LoginViewModel", "Checking if email exists in Firestore: $email")
        val db = Firebase.firestore
        db.collection("users")
            .whereEqualTo("email", email)
            .get()
            .addOnSuccessListener { documents ->
                if (!documents.isEmpty) {
                    // Email exists in the database, proceed with password reset
                    auth.sendPasswordResetEmail(email)
                        .addOnCompleteListener { sendTask ->
                            if (sendTask.isSuccessful) {
                                _emailSent.value = true
                                Log.d("LoginViewModel", "Password reset email sent.")
                            } else {
                                _emailSent.value = false
                                Log.e("LoginViewModel", "Failed to send password reset email.", sendTask.exception)
                            }
                        }
                } else {
                    // Email does not exist in the database
                    _emailSent.value = false
                    Log.e("LoginViewModel", "Email not found in the database.")
                }
            }
            .addOnFailureListener { exception ->
                _emailSent.value = false
                Log.e("LoginViewModel", "Error checking email in the database.", exception)
            }
    }


}