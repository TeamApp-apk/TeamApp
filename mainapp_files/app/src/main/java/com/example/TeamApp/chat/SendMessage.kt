package com.example.TeamApp.chat

import android.util.Log
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions

fun sendMessage(eventId: String, userId: String, messageText: String) {
    val db = FirebaseFirestore.getInstance()

    // Fetch the user's name from the users collection
    db.collection("users").document(userId).get()
        .addOnSuccessListener { document ->
            val userName = document.getString("name") ?: "Unknown User"

            // Create the message data for the messages subcollection
            val message = hashMapOf(
                "userId" to userId,
                "userName" to userName, // Add userName to the message
                "message" to messageText,
                "timestamp" to FieldValue.serverTimestamp()
            )

            // Add the message to the messages subcollection
            db.collection("events").document(eventId).collection("messages")
                .add(message)
                .addOnSuccessListener { documentReference ->
                    Log.d("Chat", "Message sent successfully with ID: ${documentReference.id}")

                    // Update the lastMessage field in the event document
                    val lastMessageData = hashMapOf(
                        "lastMessage" to hashMapOf(
                            "userId" to userId,
                            "userName" to userName, // Add userName to lastMessage
                            "message" to messageText,
                            "timestamp" to FieldValue.serverTimestamp()
                        )
                    )

                    db.collection("events").document(eventId)
                        .set(lastMessageData, SetOptions.merge())
                        .addOnSuccessListener {
                            Log.d("Chat", "Last message updated successfully in event document")
                        }
                        .addOnFailureListener { e ->
                            Log.e("Chat", "Error updating last message in event document", e)
                        }
                }
                .addOnFailureListener { e ->
                    Log.e("Chat", "Error sending message", e)
                }
        }
        .addOnFailureListener { e ->
            Log.e("Chat", "Error fetching user name", e)
            // Fallback: Send message without userName if user document fetch fails
            val message = hashMapOf(
                "userId" to userId,
                "userName" to "Unknown User",
                "message" to messageText,
                "timestamp" to FieldValue.serverTimestamp()
            )

            db.collection("events").document(eventId).collection("messages")
                .add(message)
                .addOnSuccessListener { documentReference ->
                    Log.d("Chat", "Message sent with fallback userName, ID: ${documentReference.id}")

                    val lastMessageData = hashMapOf(
                        "lastMessage" to hashMapOf(
                            "userId" to userId,
                            "userName" to "Unknown User",
                            "message" to messageText,
                            "timestamp" to FieldValue.serverTimestamp()
                        )
                    )

                    db.collection("events").document(eventId)
                        .set(lastMessageData, SetOptions.merge())
                        .addOnSuccessListener {
                            Log.d("Chat", "Last message updated with fallback userName")
                        }
                        .addOnFailureListener { e ->
                            Log.e("Chat", "Error updating last message with fallback", e)
                        }
                }
                .addOnFailureListener { e ->
                    Log.e("Chat", "Error sending message with fallback", e)
                }
        }
}