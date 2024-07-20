package com.example.TeamApp

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth

class LoginViewModel : ViewModel() {

    private val _username = MutableLiveData("")
    val username: LiveData<String> = _username

    private val _password = MutableLiveData("")
    val password: LiveData<String> = _password

    private val _loginSuccess = MutableLiveData<Boolean>()
    val loginSuccess: LiveData<Boolean> = _loginSuccess

    private val _registerSuccess = MutableLiveData<Boolean>()
    val registerSuccess: LiveData<Boolean> = _registerSuccess

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    fun onUsernameChanged(newUsername: String) {
        _username.value = newUsername
    }

    fun onPasswordChanged(newPassword: String) {
        _password.value = newPassword
    }

    fun onLoginClick() {
        val email = _username.value ?: return
        val password = _password.value ?: return

        Log.d("LoginAttempt", "Attempting to log in with email: $email")

        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d("Login", "Login successful")
                    _loginSuccess.value = true
                } else {
                    Log.e("Login", "Login failed: ${task.exception?.message}")
                    _loginSuccess.value = false
                }
            }
    }

    fun onRegisterClick() {
        val email = _username.value ?: return
        val password = _password.value ?: return

        Log.d("RegisterAttempt", "Attempting to register with email: $email")
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d("Register", "Registration successful")
                    _registerSuccess.value = true
                } else {
                    Log.e("Register", "Registration failed: ${task.exception?.message}")
                    _registerSuccess.value = false
                }
            }
    }

}
