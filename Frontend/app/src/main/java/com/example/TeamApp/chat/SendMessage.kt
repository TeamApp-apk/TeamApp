package com.example.TeamApp.chat

import android.util.Log
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore

fun sendMessage(eventId: String, userId: String, messageText: String) {
    val db = FirebaseFirestore.getInstance()
    val message = hashMapOf(
        "userId" to userId,
        "message" to messageText,
        "timestamp" to FieldValue.serverTimestamp()
    )

    db.collection("events").document(eventId).collection("messages")
        .add(message)
        .addOnSuccessListener { Log.d("Chat", "Message sent successfully!") }
        .addOnFailureListener { e -> Log.e("Chat", "Error sending message", e) }
}