package com.example.TeamApp.excludedUI
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

@Composable
fun rememberPickerState(initialIndex: Int = 0) = remember { PickerState(initialIndex) }

class PickerState(initialIndex: Int) {
    var selectedIndex by mutableStateOf(initialIndex)
}