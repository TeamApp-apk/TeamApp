package com.example.TeamApp.scrollDownChatsList

import android.util.Log
import com.example.TeamApp.data.Event
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration

class EventRepository private constructor() {

    companion object {

        @Volatile
        private var INSTANCE: EventRepository? = null

        fun getInstance(): EventRepository {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: EventRepository().also { INSTANCE = it }
            }
        }
    }


    private val firestore = FirebaseFirestore.getInstance()
    private val currUserID: String
        get() = FirebaseAuth.getInstance().currentUser?.uid.orEmpty()

    fun getUserEvents(onResult: (List<Event>) -> Unit): ListenerRegistration {
        return firestore.collection("events")
            .whereArrayContains("participants", currUserID)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {

                    onResult(emptyList())
                    return@addSnapshotListener
                }

                if (snapshot == null) {

                    onResult(emptyList())
                    return@addSnapshotListener
                }

                val events = snapshot.documents.mapNotNull { doc ->
                    try {
                        val lastMessage = doc.get("lastMessage") as? Map<String, Any>
                        val timestamp = (lastMessage?.get("timestamp") as? com.google.firebase.Timestamp)



                        doc.toObject(Event::class.java)?.copy(
                            id = doc.id,
                            lastMessage = lastMessage
                        )
                    } catch (e: Exception) {

                        null
                    }
                }.sortedByDescending { event ->
                    (event.lastMessage?.get("timestamp") as? com.google.firebase.Timestamp)?.toDate()?.time ?: 0L
                }

                onResult(events)
            }
    }
}

