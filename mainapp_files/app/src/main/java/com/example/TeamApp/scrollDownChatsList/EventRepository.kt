package com.example.TeamApp.scrollDownChatsList

import android.util.Log
import androidx.compose.animation.core.snap
import com.example.TeamApp.data.Event
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.toObject


class EventRepository {
    private val firestore = FirebaseFirestore.getInstance()
    private val currUserID = FirebaseAuth.getInstance().currentUser?.uid.orEmpty()

    // Zwracamy ListenerRegistration, żeby móc go potem odpiąć
    fun getUserEvents(onResult: (List<Event>) -> Unit): ListenerRegistration {
        return firestore.collection("events")
            .whereArrayContains("participants", currUserID)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    Log.e("EventRepo", "Listener error: $error")
                    onResult(emptyList())
                    return@addSnapshotListener
                }
                if (snapshot == null) {
                    Log.e("EventRepo", "Snapshot null")
                    onResult(emptyList())
                    return@addSnapshotListener
                }
                Log.d("EventRepo", "Snapshot size: ${snapshot.size()}")
                val events = snapshot.documents.mapNotNull { doc ->
                    doc.toObject(Event::class.java)?.copy(id = doc.id)
                }
                onResult(events)
            }
    }

}
