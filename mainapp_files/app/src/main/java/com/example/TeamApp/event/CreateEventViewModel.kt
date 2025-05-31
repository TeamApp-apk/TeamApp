package com.example.TeamApp.event

import android.content.Context
import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.example.TeamApp.data.Coordinates
import com.example.TeamApp.data.Event
import com.example.TeamApp.data.User
import com.google.firebase.Firebase
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import com.tomtom.sdk.location.GeoPoint
import com.tomtom.sdk.location.poi.StandardCategoryId.Companion.Locale
import com.tomtom.sdk.map.display.MapOptions
import com.tomtom.sdk.map.display.ui.MapFragment
import com.tomtom.sdk.search.autocomplete.AutocompleteOptions
import com.tomtom.sdk.search.online.OnlineSearch
import kotlinx.coroutines.Delay
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.util.Calendar
import java.util.Properties
import com.google.firebase.firestore.Query
import java.util.Date
import java.util.Locale


class CreateEventViewModel : ViewModel() {
    private val _sport = MutableLiveData<String>()

    private val _eventName = MutableLiveData<String>()
    val eventName: LiveData<String> get() = _eventName

    private val _price = MutableLiveData<String>()
    val price: LiveData<String> get() = _price

    private val _skillLevel = MutableLiveData<String>()
    val skillLevel: LiveData<String> get() = _skillLevel
    val availableSkillLevels = listOf("Rekreacyjny", "Rekreacyjny/Średni", "Średni", "Zaawansowany", "Profesjonalny")


    val activityList =   mutableStateListOf<Event>()
    val sport: LiveData<String> get()= _sport
    var isDataFetched = false
    var newlyCreatedEvent by mutableStateOf<Event?>(null)
    private val _location = MutableLiveData<String>()
    val location: LiveData<String> get() = _location

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()
    fun loadStuff(){
        viewModelScope.launch {
            _isLoading.value = true
            delay(1000)
            _isLoading.value = false

        }
    }
    private var _mapFragment: MutableState<MapFragment?> = mutableStateOf(null)
    val mapFragment: MapFragment?
        get() = _mapFragment.value
    fun setMapFragment(fragment: MapFragment) {
        _mapFragment.value = fragment
    }
    var isMapInitialized = false
    fun initializeMapIfNeeded(context: Context) {
        Log.d("CreateEventViewModel", "initializeMapIfNeeded: $isMapInitialized")
        if (!isMapInitialized) {
            isMapInitialized = true
            val fragmentManager = (context as FragmentActivity).supportFragmentManager
            val mapOptions = MapOptions(
                mapKey = getApiKey(context),
                // add more options
            )
            createTomTomMapFragment(fragmentManager, mapOptions) { fragment ->
                Log.d("CreateEventViewModel", "Map fragment created")
                setMapFragment(fragment)
            }
        }
    }

    private val _locationID = MutableLiveData<Map<String, Coordinates>>()
    val locationID: LiveData<Map<String, Coordinates>> get() = _locationID

    fun setLocationID(newLocationID: Map<String, Coordinates>) {
        _locationID.value = newLocationID
    }
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
    fun createEvent(event: Event, userID:String, callback: (String?) -> Unit) {
        val db = Firebase.firestore
        Log.d("CreateEventViewModel", "createEvent: $event")
        Log.d("CreateEventViewModel", "userID: $userID")
        val userRef = db.collection("users").document(userID)
        val eventRef = db.collection("events").document()
        val eventWithId = event.copy(id = eventRef.id)
        eventRef.set(eventWithId)
            .addOnSuccessListener {
                Log.d("CreateEventViewModel", "Event successfully created with ID: ${eventRef.id}")
                callback(null)
            }
            .addOnFailureListener { e ->
                Log.w("CreateEventViewModel", "Error creating event", e)
                callback("Błąd, nie dodano Eventu")
            }
        userRef.update("attendedEvents", FieldValue.arrayUnion(eventRef.id))
            .addOnSuccessListener {
                Log.d("CreateEventViewModel", "Event ID successfully added to user's attendedEvents")
            }
            .addOnFailureListener { e ->
                Log.w("CreateEventViewModel", "Error updating attendedEvents", e)
            }


        activityList.add(0, eventWithId)
        newlyCreatedEvent = eventWithId
    }
    fun clearNewlyCreatedEvent(){
        newlyCreatedEvent = null
    }
    fun fetchEvents() {
        Log.d("CreateEventViewModel", "fetchEvents: $isDataFetched")
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

    fun fetchFilteredEvents(
        sports: List<String>?,
        maxDistance: Int?, // Skipped for now
        priceOption: String?,
        levelOption: String?,
        startDateMillis: Long?,
        endDateMillis: Long?,
        city: String? // Skipped for now
    ) {
        Log.d(
            "CreateEventViewModel",
            "Fetching filtered events. Sports: $sports, Price: $priceOption, Level: $levelOption, StartDate: $startDateMillis, EndDate: $endDateMillis"
        )

        val db = Firebase.firestore // Using ktx extension for conciseness
        var query: Query = db.collection("events")

        if (!sports.isNullOrEmpty()) {
            query = query.whereIn("activityName", sports)
        } else {
            Log.d("CreateEventViewModel", "No sports selected, not applying sports filter to query.")
        }

        if (priceOption != null && priceOption != "Dowolna") {
            when (priceOption) {
                "0zł" -> query = query.whereEqualTo("price", "0zł")
                else -> Log.d("CreateEventViewModel", "Price option '$priceOption' requires client-side filtering or numeric storage.")
            }
        }

        if (levelOption != null && levelOption != "Dowolny") {
            query = query.whereEqualTo("skillLevel", levelOption)
        }

        query.get()
            .addOnSuccessListener { result -> // No explicit type needed here usually, but good practice if error persists
                val fetchedEvents = mutableListOf<Event>()
                for (documentSnapshot in result.documents) { // Iterate over DocumentSnapshot
                    try {
                        val event = documentSnapshot.toObject(Event::class.java)
                        if (event != null) { // Check for null if conversion can fail
                            fetchedEvents.add(event)
                        } else {
                            Log.w("CreateEventViewModel", "Failed to convert document to Event: ${documentSnapshot.id}")
                        }
                    } catch (e: Exception) {
                        Log.e("CreateEventViewModel", "Error converting document ${documentSnapshot.id} to Event", e)
                    }
                }
                Log.d("CreateEventViewModel", "Fetched ${fetchedEvents.size} events from Firestore based on server-side filters.")

                var clientFilteredEvents = fetchedEvents

                if (priceOption != null && priceOption != "Dowolna" && priceOption != "0zł") {
                    clientFilteredEvents = clientFilteredEvents.filter { event ->
                        val eventPrice = event.price?.replace(Regex("[^0-9]"), "")?.toIntOrNull() // More robust parsing
                        if (eventPrice == null && priceOption != "0zł") return@filter false

                        when (priceOption) {
                            "<20zł" -> eventPrice != null && eventPrice < 20
                            "<50zł" -> eventPrice != null && eventPrice < 50
                            else -> true
                        }
                    }.toMutableList()
                    Log.d("CreateEventViewModel", "After client-side price filter ('$priceOption'): ${clientFilteredEvents.size} events.")
                }

                if (startDateMillis != null || endDateMillis != null) {
                    val sdf = SimpleDateFormat("dd/MM/yyyy", java.util.Locale.getDefault()) // Correct usage of Locale.getDefault()

                    clientFilteredEvents = clientFilteredEvents.filter { event ->
                        try {
                            val eventDateString = event.date.split(" ")[0]
                            val eventDateCalendar = Calendar.getInstance().apply { // Correct usage of Calendar
                                time = sdf.parse(eventDateString) ?: return@filter false
                                set(Calendar.HOUR_OF_DAY, 0)
                                set(Calendar.MINUTE, 0)
                                set(Calendar.SECOND, 0)
                                set(Calendar.MILLISECOND, 0)
                            }
                            val eventTime = eventDateCalendar.timeInMillis

                            val startCheck = startDateMillis == null || eventTime >= startDateMillis
                            val endCheck = endDateMillis == null || eventTime <= endDateMillis

                            startCheck && endCheck
                        } catch (e: Exception) {
                            Log.e("CreateEventViewModel", "Error parsing event date: ${event.date}", e)
                            false
                        }
                    }.toMutableList()
                    Log.d("CreateEventViewModel", "After client-side date filter: ${clientFilteredEvents.size} events.")
                }

                activityList.clear()
                activityList.addAll(clientFilteredEvents)

                if (clientFilteredEvents.isNotEmpty()) {
                    Log.d("CreateEventViewModel", "Final filtered events count: ${clientFilteredEvents.size}")
                } else {
                    Log.d("CreateEventViewModel", "No events matched all filter criteria.")
                }
            }
            .addOnFailureListener { e: Exception -> // Explicitly type 'e' as Exception
                Log.w("CreateEventViewModel", "Error fetching events with query", e)
                // Example if activityList is a MutableLiveData:
                // _activityList.value = emptyList()
                // If activityList is the direct mutable list:
                activityList.clear()
            }
    }

    fun getEventById(id: String): Event? {
        return activityList.find { it.id == id }
    }
    fun resetFields() {
        _sport.value = ""
        _location.value = ""
        _limit.value = ""
        _description.value = ""
        _dateTime.value = ""
        _eventName.value = ""
        _skillLevel.value = ""
        _price.value = ""
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
    fun onPriceChange(newPrice: String) {
        _price.value = newPrice
    }
    fun onSkillLevelChange(newSkillLevel: String) {
        _skillLevel.value = newSkillLevel
    }
    fun onActivityNameChange(newActivityName: String) {
        _eventName.value = newActivityName
    }
    fun onEventNameChange(newEventName: String) {
        _eventName.value = newEventName
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

