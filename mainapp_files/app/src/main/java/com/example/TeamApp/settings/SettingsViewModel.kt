package com.example.TeamApp.settings
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.content.ContextCompat.startActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.TeamApp.auth.LoginActivity
import com.example.TeamApp.auth.LoginViewModel
import com.google.firebase.auth.FirebaseAuth
import androidx.lifecycle.MutableLiveData
import androidx.navigation.NavController
import com.example.TeamApp.MainAppActivity
import com.example.TeamApp.auth.ForgotPasswordActivity
import com.example.TeamApp.auth.RegisterActivity
import com.example.TeamApp.data.Event
import com.example.TeamApp.data.User
import com.google.firebase.Firebase
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseAuthRecentLoginRequiredException
import com.google.firebase.firestore.firestore
import java.time.LocalDate

class SettingsViewModel : ViewModel() {
    private val _password = MutableLiveData("")
    val password: LiveData<String> = _password

    fun onPasswordChanged(newPassword: String) {
        _password.value = newPassword
    }


    fun navigateToForgotPasswordActivity(navController: NavController) {

    }
    fun navigateToTermsOfUse(navController: NavController) {
        navController.navigate("termsOfUse"){
            popUpTo("settings"){inclusive = false}
        }
    }

    fun navigateToPrivacySettings(navController: NavController) {
        navController.navigate("privacyPolicy"){
            popUpTo("settings"){inclusive = false}
        }

    }



        fun logout(context: Context) {
        FirebaseAuth.getInstance().signOut()
        val intent = Intent(context, RegisterActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        context.startActivity(intent)
        if (context is Activity) {
            (context as Activity).finish()
        }
    }

    fun deleteAccount(context: Context) {
        val user = FirebaseAuth.getInstance().currentUser

        if (user != null && password.value != "") {
            val credential = EmailAuthProvider.getCredential(user.email!!, password.value!!)

            user.reauthenticate(credential)
                .addOnCompleteListener { reauthTask ->
                    if (reauthTask.isSuccessful) {
                        user.delete()
                            .addOnCompleteListener { deleteTask ->
                                if (deleteTask.isSuccessful) {
                                    Log.e("Settings", "udalo sie usunac konto")
                                    // Account deleted successfully, navigate to RegisterActivity
                                    val intent = Intent(context, RegisterActivity::class.java)
                                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                    context.startActivity(intent)
                                    if (context is Activity) {
                                        (context as Activity).finish()
                                    }
                                } else {
                                    Log.e("Settings", "Account deletion failed: ${deleteTask.exception?.message}")
                                }
                            }
                    } else {
                        Log.e("Settings", "Re-authentication failed: ${reauthTask.exception?.message}")
                    }
                }
        } else {
            Log.e("Settings", "User is not signed in or empty password.")
        }
    }

}
