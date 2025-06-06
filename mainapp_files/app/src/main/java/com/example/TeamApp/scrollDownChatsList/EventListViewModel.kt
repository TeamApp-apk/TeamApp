package com.example.TeamApp.scrollDownChatsList

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.TeamApp.data.Event
import com.google.firebase.firestore.ListenerRegistration



class EventListViewModel : ViewModel() {
    private val repository = EventRepository.getInstance()

    private var listenerRegistration: ListenerRegistration? = null
    private var _userEvents = mutableStateOf<List<Event>>(emptyList())
    val userEvents: State<List<Event>> = _userEvents

    init {
        listenerRegistration = repository.getUserEvents { events ->
            Log.d("ViewModel", "Received events: ${events.size}")
            _userEvents.value = events
        }
    }

    override fun onCleared() {
        super.onCleared()
        listenerRegistration?.remove()
    }
}