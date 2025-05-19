package com.example.TeamApp.chat

import com.example.TeamApp.auth.user

data class Message(
    val userId: String = "",
    val message: String = "",
    val timestamp: com.google.firebase.Timestamp? = null,
    val userName: String = ""
)