package com.example.TeamApp.profile

import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.TeamApp.auth.LoginActivity
import com.example.TeamApp.auth.LoginViewModel
import com.google.firebase.auth.FirebaseAuth
import androidx.lifecycle.MutableLiveData
import androidx.navigation.NavController
import com.example.TeamApp.auth.RegisterActivity
import com.example.TeamApp.data.Event
import com.example.TeamApp.data.User
import com.example.TeamApp.event.CreateEventActivity
import com.example.TeamApp.profile.ProfileActivity
import com.example.TeamApp.searchThrough.SearchThroughActivity
import com.example.TeamApp.settings.SettingActivity
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import java.time.LocalDate


class ProfileViewModel:ViewModel(){
    fun navigateToCreateEvent(navController: NavController) {
        navController.navigate("createEvent"){
            popUpTo("profile"){inclusive = true}
        }
    }
    fun navigateToSearchThrough(navController: NavController) {
        navController.navigate("search"){
            popUpTo("profile"){inclusive = true}
        }
    }
    fun navitagateToSettings(navController: NavController) {
        navController.navigate("settings"){
            popUpTo("profile"){inclusive = false}
        }
    }


}