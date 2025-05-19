package com.example.TeamApp.scrollDownChatsList

import androidx.compose.animation.core.snap
import com.example.TeamApp.data.Event
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject

class EventRepository {
    private val firestore = FirebaseFirestore.getInstance()
    private val currUserID = FirebaseAuth.getInstance().currentUser?.uid.orEmpty()

    fun getUserEvents(onResult: (List<Event>) -> Unit) {
        firestore.collection("events")
            .whereArrayContains("participants", currUserID)
            .get()
            .addOnSuccessListener { snapshot ->
                val events = snapshot.documents.mapNotNull { doc ->
                    val event = doc.toObject(Event::class.java)
                    event?.copy(id = doc.id)
                }
                onResult(events)
            }
            .addOnFailureListener {
                onResult(emptyList())
            }
    }
}