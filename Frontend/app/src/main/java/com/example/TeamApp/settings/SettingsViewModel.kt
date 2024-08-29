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
import com.example.TeamApp.profile.ProfileActivity
import com.example.TeamApp.searchThrough.SearchThroughActivity
import com.example.TeamApp.settings.SettingActivity
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import java.time.LocalDate

class SettingsViewModel : ViewModel() {
    fun navigateToForgotPasswordActivity(navController: NavController) {

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
}
