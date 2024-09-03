package com.example.TeamApp.event

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import androidx.lifecycle.MutableLiveData
import androidx.navigation.NavController
import com.example.TeamApp.data.Event
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import com.tomtom.sdk.location.GeoPoint
import com.tomtom.sdk.location.poi.StandardCategoryId.Companion.Locale
import com.tomtom.sdk.search.autocomplete.AutocompleteOptions
import com.tomtom.sdk.search.online.OnlineSearch
import kotlinx.coroutines.Delay
import kotlinx.coroutines.InternalCoroutinesApi
import java.time.LocalDateTime


class CreateEventViewModel : ViewModel() {
    private val _sport = MutableLiveData<String>()
    val activityList =   mutableStateListOf<Event>()
    val sport: LiveData<String> get()= _sport
    private var isDataFetched = false
    var newlyCreatedEvent by mutableStateOf<Event?>(null)

    private val _location = MutableLiveData<String>()
    val location: LiveData<String> get() = _location

    private val _dateTime = MutableLiveData<String>()
    val dateTime: LiveData<String> = _dateTime

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

    fun createEvent(event : Event, callback: (String?) -> Unit){
        val db = Firebase.firestore
        db.collection("events").add(event)
            .addOnSuccessListener { Log.d("CreateEventViewModel", "Event successfully created"); callback(null) }
            .addOnFailureListener { e -> Log.w("CreateEventViewModel", "Error creating event", e); callback("Błąd, nie dodano Eventu") }
        activityList.add(0,event)
        newlyCreatedEvent = event
    }
    fun clearNewlyCreatedEvent(){
        newlyCreatedEvent = null
    }


    fun fetchEvents() {
        if (isDataFetched) return
        Log.d("CreateEventViewModel", "fetchEvents")
        val db = Firebase.firestore
        db.collection("events").get()
            .addOnSuccessListener { result ->
                activityList.clear()
                for (document in result) {
                    val event = document.toObject(Event::class.java)
                    activityList.add(event)
                }
                if (!activityList.isEmpty()) {
                    Log.d("CreateEventViewModel", "events found")
                }
                Log.d("CreateEventViewModel", "Events fetched successfully")
                isDataFetched = true
            }
            .addOnFailureListener { e ->
                Log.w("CreateEventViewModel", "Error fetching events", e)
            }
    }


    fun resetFields() {
        _sport.value = ""
        _location.value = ""
        _limit.value = ""
        _description.value = ""
        _dateTime.value = ""
    }

    //temporary here
    fun logout(navController: NavController) {
        FirebaseAuth.getInstance().signOut()
        navController.navigate("register") {
            popUpTo("createEvent") { inclusive = true }
        }
    }

    fun onSportChange(newSport: String) {
        _sport.value = newSport.toString()
    }

    fun onAddressChange(newAddress: String) {
        _location.value = newAddress
    }

    fun onDateChange(newDate: String) {
        _dateTime.value = newDate
    }

    fun onLimitChange(newLimit: String) {
        _limit.value = newLimit
    }

    fun onDescriptionChange(newDescription: String) {
        _description.value = newDescription
    }

    fun getAvailableSports():List<String>{
        return Event.sportIcons.keys.toList()
    }


}