package com.example.TeamApp.event

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
import com.example.TeamApp.profile.ProfileActivity
import com.example.TeamApp.searchThrough.SearchThroughActivity
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import java.time.LocalDate


class CreateEventViewModel : ViewModel() {
    private val _sport = MutableLiveData<String>()
    val sport: LiveData<String> get()= _sport

    private val _address = MutableLiveData<String>()
    val address: LiveData<String> get() = _address


    private val _description = MutableLiveData<String>()
    val description: LiveData<String> get() = _description

    private val _limit = MutableLiveData<String>()
    val limit: LiveData<String> get() = _limit

    fun navigateToProfile(navController: NavController){
        navController.navigate("profile"){
            popUpTo("createEvent"){inclusive = true}
        }
    }
    fun navigateToSearchThrough(navController: NavController){
        navController.navigate("search"){
            popUpTo("createEvent"){inclusive = true}
        }
    }

    fun createEvent(context: Context, sport: String, address: String, limit: String, description: String) {
        if (sport.isEmpty() || address.isEmpty() || limit.isEmpty() || description.isEmpty()) {
            Log.e("CreateEventViewModel", "One or more fields are empty") //do debugu
            return
        }

        Log.d("CreateEventViewModel", "createEvent called with sport: $sport, address: $address, limit: $limit, description: $description")
        val event = Event(
            location = address,
            date = LocalDate.now().toString(), // Tymczasowo
            time = "12:00", // default time
            sportDiscipline = Event.SportDiscipline.valueOf(sport), // Assuming sport is a valid enum value
            maxParticipants = limit.toInt(),
            currentParticipants = 0, // Poczatkowo 0 uczestnikow
            description = description,
            creator = "creatorId", // Jakis placeholder
            endDate = LocalDate.now().plusDays(1).toString() // Tymczasowo
        )
        val db = Firebase.firestore
        db.collection("events").add(event)
            .addOnSuccessListener { Log.d("CreateEventViewModel", "Event successfully created") }
            .addOnFailureListener { e -> Log.w("CreateEventViewModel", "Error creating event", e) }
    }

    //temporary here
    fun logout(navController: NavController) {
        FirebaseAuth.getInstance().signOut()
        navController.navigate("register") {
            popUpTo("createEvent") { inclusive = true }
        }
    }

    fun onSportChange(newSport: String) {
        _sport.value = newSport
    }

    fun onAddressChange(newAddress: String) {
        _address.value = newAddress
    }

    fun onLimitChange(newLimit: String) {
        _limit.value = newLimit
    }

    fun onDescriptionChange(newDescription: String) {
        _description.value = newDescription
    }

    fun getAvailableSports():List<String>{
        return Event.SportDiscipline.entries.map { it.name }
    }

}