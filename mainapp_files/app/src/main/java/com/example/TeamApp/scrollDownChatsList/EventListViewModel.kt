package com.example.TeamApp.scrollDownChatsList

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.TeamApp.data.Event

class EventListViewModel : ViewModel() {
    private val repository = EventRepository()

    private var _userEvents = mutableStateOf<List<Event>>(emptyList())
    val userEvents: State<List<Event>> = _userEvents

    init {
        loadUserEvents()
    }

    private fun loadUserEvents() {
        repository.getUserEvents { events ->
            _userEvents.value = events
        }
    }
}