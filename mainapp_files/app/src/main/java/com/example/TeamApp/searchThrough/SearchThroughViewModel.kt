package com.example.TeamApp.searchThrough

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.MutableLiveData
import com.example.TeamApp.data.Event
import com.example.TeamApp.event.CreateEventViewModel
import com.example.TeamApp.event.CreateEventViewModelProvider
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class SearchThroughViewModel : ViewModel(){
    val otherViewModel: CreateEventViewModel = CreateEventViewModelProvider.createEventViewModel

    private val _filteredEvents = MutableLiveData<List<Event>>()
    val filteredEvents: LiveData<List<Event>> = _filteredEvents



    val availableSports = otherViewModel.getAvailableSports()

    private val _selectedSports = MutableLiveData<List<String>>(availableSports.toList())
    val selectedSports: LiveData<List<String>> = _selectedSports


    val priceFilterOptionsList = listOf("Dowolna", "0zł", "<20zł", "<50zł") // As requested
    private val _selectedPriceOptionUi = MutableStateFlow(priceFilterOptionsList.first()) // Default to "Dowolna"
    val selectedPriceOptionUi: StateFlow<String> = _selectedPriceOptionUi.asStateFlow()

    val levelFilterOptionsList = listOf("Dowolny", "Początkujący", "Średniozaawansowany", "Zaawansowany", "Ekspert") // As requested
    private val _selectedLevelOptionUi = MutableStateFlow(levelFilterOptionsList.first()) // Default to "Dowolna"
    val selectedLevelOptionUi: StateFlow<String> = _selectedLevelOptionUi.asStateFlow()

    fun onPriceOptionUiSelected(option: String) {
        if (priceFilterOptionsList.contains(option)) { // Ensure valid option
            _selectedPriceOptionUi.value = option
        }
    }
    fun onLevelOptionUiSelected(option: String) {
        if (levelFilterOptionsList.contains(option)) { // Ensure valid option
            _selectedLevelOptionUi.value = option
        }
    }

    private val _distance = MutableLiveData<Int>(75)
    val distance: LiveData<Int> = _distance

    private val _filtersOn = MutableLiveData(false)
    val filtersOn: LiveData<Boolean> = _filtersOn


    fun onDistanceChange(newValue: Int) {
        _distance.value = newValue
    }

    fun toggleAllSports(selected: Boolean) {
        _selectedSports.value = if (selected) {
            availableSports.toMutableList()
        } else {
            mutableListOf()
        }
    }

    fun updateSportSelection(sport: String) {
        val currentList = _selectedSports.value?.toMutableList() ?: mutableListOf()
        if (currentList.contains(sport)) {
            currentList.remove(sport)
        } else {
            currentList.add(sport)
        }
        _selectedSports.value = currentList
    }

    fun onFilterAccept()
    {
        _filtersOn.value = true
        val selectedSports = _selectedSports.value ?: listOf()
        otherViewModel.fetchFilteredEvents(
            sports = selectedSports,
            maxDistance = _distance.value,
            priceOption = _selectedPriceOptionUi.value,
            levelOption = _selectedLevelOptionUi.value,
            startDateMillis = _selectedStartDateMillis.value,
            endDateMillis = _selectedEndDateMillis.value,
            city = _city.value
        )
    }

    fun onFilterReset()
    {
        _distance.value = 75
        toggleAllSports(true) // Resets sports to all selected
        _selectedPriceOptionUi.value = priceFilterOptionsList.first() // Reset price to "Dowolna"
        _selectedLevelOptionUi.value = levelFilterOptionsList.first() // Reset level to "Dowolny"
        _selectedStartDateMillis.value = null // Reset start date
        _selectedEndDateMillis.value = null // Reset end date
        _filtersOn.value = false
        // otherViewModel.isDataFetched = false // This might be handled within fetchEvents or based on your app logic
        otherViewModel.fetchEvents() // Fetch all events without filters
        Log.d("SearchThroughViewModel", "Filters Reset")
    }


    private val _city = MutableLiveData<String>("Warszawa")
    val city: LiveData<String> get()= _city
    fun getAvailableCities():List<String>{
        return listOf("Warszawa", "Kraków", "Tarnów", "Gdańsk", "Wrocław")
    }
    fun updateCurrentCity(newCity: String) {
        _city.value = newCity
        Log.d("SearchViewModel", "City updated to: $newCity")
    }


    private val _selectedStartDateMillis = MutableStateFlow<Long?>(null)
    val selectedStartDateMillis: StateFlow<Long?> = _selectedStartDateMillis.asStateFlow()

    private val _selectedEndDateMillis = MutableStateFlow<Long?>(null)
    val selectedEndDateMillis: StateFlow<Long?> = _selectedEndDateMillis.asStateFlow()

    fun onDateRangeSelected(startDate: Long?, endDate: Long?) {
        _selectedStartDateMillis.value = startDate
        _selectedEndDateMillis.value = endDate
        Log.d("SearchThroughViewModel", "Date range selected: Start: $startDate, End: $endDate")
    }


}
