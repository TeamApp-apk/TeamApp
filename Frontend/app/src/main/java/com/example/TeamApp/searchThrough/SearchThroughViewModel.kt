package com.example.TeamApp.searchThrough

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.View
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
import com.example.TeamApp.profile.ProfileActivity
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import java.time.LocalDate

class SearchThroughViewModel : ViewModel(){

    fun navigateToProfile(navController: NavController){
        navController.navigate("profile"){
            popUpTo("searchThrough"){inclusive = true}
        }
    }
    fun navigateToCreateEvent(navController: NavController){
        navController.navigate("createEvent"){
            popUpTo("searchThrough"){inclusive = true}
        }
    }


}
