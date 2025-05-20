package com.example.TeamApp.scrollDownChatsList

import android.util.Log
import com.example.TeamApp.data.Event
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration

class EventRepository {
    private val firestore = FirebaseFirestore.getInstance()
    private val currUserID = FirebaseAuth.getInstance().currentUser?.uid.orEmpty()

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

                val events = snapshot.documents.mapNotNull { doc ->
                    try {
                        val lastMessage = doc.get("lastMessage") as? Map<String, Any>
                        val timestamp = (lastMessage?.get("timestamp") as? com.google.firebase.Timestamp)

                        Log.d("EventRepo", "Event ${doc.id} timestamp: ${timestamp?.toDate()}")

                        doc.toObject(Event::class.java)?.copy(
                            id = doc.id,
                            lastMessage = lastMessage
                        )
                    } catch (e: Exception) {
                        Log.e("EventRepo", "Error parsing event: ${doc.id}", e)
                        null
                    }
                }.sortedByDescending { event ->
                    (event.lastMessage?.get("timestamp") as? com.google.firebase.Timestamp)?.toDate()?.time ?: 0L
                }

                onResult(events)
            }
    }

}