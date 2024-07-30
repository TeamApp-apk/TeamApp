package com.example.TeamApp.auth

import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.TeamApp.data.User
import com.example.TeamApp.event.CreateEventActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class LoginViewModel : ViewModel() {

    private val _email = MutableLiveData("")
    val email: LiveData<String> = _email

    private val _password = MutableLiveData("")
    val password: LiveData<String> = _password

    private val _username = MutableLiveData("")
    val username: LiveData<String> = _username

    private val _loginSuccess = MutableLiveData<Boolean>()
    val loginSuccess: LiveData<Boolean> = _loginSuccess

    private val _registerSuccess = MutableLiveData<Boolean>()
    val registerSuccess: LiveData<Boolean> = _registerSuccess

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    fun onUsernameChanged(newUsername: String) {
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
                    val intent = Intent(context, CreateEventActivity::class.java).apply{
                        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    }
                    context.startActivity(intent)
                } else {
                    Log.e("Login", "Login failed: ${task.exception?.message}")
                    _loginSuccess.value = false
                }
            }
    }
    fun onRegisterClick(context: Context) {
        val email = _email.value ?: return
        val password = _password.value ?: return
        //Tymczasowo, niedodana implementacja rejestracji z loginem
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
}