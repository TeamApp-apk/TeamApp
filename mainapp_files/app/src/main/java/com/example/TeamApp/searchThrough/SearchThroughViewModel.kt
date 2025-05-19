package com.example.TeamApp.searchThrough

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.MutableLiveData
import com.example.TeamApp.data.Event
import com.example.TeamApp.event.CreateEventViewModel
import com.example.TeamApp.event.CreateEventViewModelProvider

class SearchThroughViewModel : ViewModel(){
    val otherViewModel: CreateEventViewModel = CreateEventViewModelProvider.createEventViewModel

    private val _filteredEvents = MutableLiveData<List<Event>>()
    val filteredEvents: LiveData<List<Event>> = _filteredEvents

    val availableSports = otherViewModel.getAvailableSports()

    private val _selectedSports = MutableLiveData<List<String>>(availableSports.toList())
    val selectedSports: LiveData<List<String>> = _selectedSports

    private val _sex = MutableLiveData("")
    val sex: LiveData<String> = _sex

    private val _distance = MutableLiveData<Int>(75)
    val distance: LiveData<Int> = _distance

    private val _minAge = MutableLiveData<Int>(0)
    val minAge: LiveData<Int> = _minAge

    private val _maxAge = MutableLiveData<Int>(100)
    val maxAge: LiveData<Int> = _maxAge

    private val _filtersOn = MutableLiveData(false)
    val filtersOn: LiveData<Boolean> = _filtersOn

    fun onSexChange(newValue: String) {
        _sex.value = newValue
    }

    fun onDistanceChange(newValue: Int) {
        _distance.value = newValue
    }

    fun onMinAgeChange(newValue: Int) {
        _minAge.value = newValue
    }

    fun onMaxAgeChange(newValue: Int) {
        _maxAge.value = newValue
    }

    fun onFilterReset()
    {
        _sex.value = ""
        _distance.value = 75
        _minAge.value = 0
        _maxAge.value = 100
        _filtersOn.value = false
        Log.d("SearchThrough", "OnFilterReset ${_minAge.value}")
        otherViewModel.isDataFetched = false
        otherViewModel.fetchEvents()
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
        val size = selectedSports.value?.size
        val size_ = _selectedSports.value?.size
        Log.d("SearchThroughViewModel", "size: $size, size_: $size_ filtersOn = ${_filtersOn.value}")
        val selectedSports = _selectedSports.value ?: listOf()
        otherViewModel.fetchFilteredEvents(selectedSports)
    }

}
