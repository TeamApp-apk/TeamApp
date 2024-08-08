package com.example.TeamApp.event

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import com.example.TeamApp.event.CreateEventScreen
import com.example.TeamApp.event.CreateEventViewModel
import com.example.compose.TeamAppTheme


class CreateEventActivity : ComponentActivity() {
    private val createEventViewModel: CreateEventViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContent {
//            TeamAppTheme {
//                CreateEventScreen(viewModel = createEventViewModel, context = this)
//            }
//        }
    }
}