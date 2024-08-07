package com.example.TeamApp.auth

import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.activity.result.ActivityResultLauncher
import android.widget.Toast
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
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import android.app.Activity

class LoginViewModel : ViewModel() {
    lateinit var signInLauncher: ActivityResultLauncher<IntentSenderRequest>
    private val _email = MutableLiveData("")
    val email: LiveData<String> = _email

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
        if (email.isNotEmpty() && password.isNotEmpty())
        {
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
        else if(email.isEmpty())
        {
            Log.d("LoginAttempt", "Login failed: empty email field")
        }
        else{
            Log.d("LoginAttempt", "Login failed: empty password field")
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

    fun signInWithFacebook(context: Context) {
        val callbackManager = CallbackManager.Factory.create()
        LoginManager.getInstance().logOut()
        LoginManager.getInstance().logInWithReadPermissions(
            context as Activity,
            listOf("public_profile", "email")
        )

        LoginManager.getInstance().registerCallback(callbackManager, object : FacebookCallback<LoginResult> {
            override fun onSuccess(loginResult: LoginResult) {
                //Handle successful login
                Log.d("FacebookLogin", "Success: ${loginResult.accessToken}")
                val accessToken = loginResult.accessToken
                // Use the access token to retrieve user data or perform other actions
            }

            override fun onCancel() {
                // Handle login cancellation
                Log.d("FacebookLogin", "Cancelled")
            }

            override fun onError(error: FacebookException) {
                // Handle login error
                Log.d("FacebookLogin", "Error: ${error.message}")
            }
        })
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
    fun getToChangePasswordScreen(context: Context){
        val intent = Intent(context, ForgotPasswordActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        context.startActivity(intent)
    }

    fun onRegisterClick(context: Context) {
        val email = _email.value ?: return
        val password = _password.value ?: return
        val confirmPassword = _confirmPassword.value ?: return
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
                    if (task.isSuccessful) {
                        db.collection("users").add(user)
                        Log.d("Register", "Registration successful")
                        _registerSuccess.value = true
                        val intent = Intent(context, CreateEventActivity::class.java).apply {
                            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        }
                        context.startActivity(intent)
                    } else {
                        Log.e("Register", "Registration failed: ${task.exception?.message}")
                        _registerSuccess.value = false
                    }
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