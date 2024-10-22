package com.example.TeamApp.data

import UserViewModel
import com.example.TeamApp.event.CreateEventViewModel

object UserViewModelProvider {
    val userViewModel: UserViewModel by lazy {
        UserViewModel()
    }
}