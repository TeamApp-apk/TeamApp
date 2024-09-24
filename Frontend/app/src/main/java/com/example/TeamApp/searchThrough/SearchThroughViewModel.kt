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
import com.example.TeamApp.event.CreateEventViewModel
import com.example.TeamApp.event.ViewModelProvider
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import java.time.LocalDate

class SearchThroughViewModel : ViewModel(){

    private val _filteredEvents = MutableLiveData<List<Event>>()
    val filteredEvents: LiveData<List<Event>> = _filteredEvents

    private val _sex = MutableLiveData("")
    val sex: LiveData<String> = _sex

    private val _distance = MutableLiveData<Int>(75)
    val distance: LiveData<Int> = _distance

    private val _minAge = MutableLiveData<Int>(0)
    val minAge: LiveData<Int> = _minAge

    private val _maxAge = MutableLiveData<Int>(100)
    val maxAge: LiveData<Int> = _maxAge

    private val _filtersOn = MutableLiveData<Boolean?>(null)
    val filtersOn: LiveData<Boolean?> = _filtersOn

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
    }

    fun onFilterAccept()
    {
        val otherViewModel: CreateEventViewModel = ViewModelProvider.createEventViewModel
            otherViewModel.fetchEvents()

    }



}
