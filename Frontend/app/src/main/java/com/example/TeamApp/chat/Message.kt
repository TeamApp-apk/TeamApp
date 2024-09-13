package com.example.TeamApp.chat
data class Message(
    val userId: String = "",
    val message: String = "",
    val timestamp: com.google.firebase.Timestamp? = null
)