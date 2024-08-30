package com.example.TeamApp.excludedUI
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.runtime.remember
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale


@Composable
fun PickerExample() {
    Surface(modifier = Modifier.fillMaxSize()) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxSize()
        ) {

            val minutes = remember { (0..59).map { String.format("%02d", it) } }
            val hour = remember { (0 ..  23).map { it.toString() } }
            val valuesPickerState = rememberPickerState()
            val units = remember { listOf("seconds", "minutes", "hours") }
            val unitsPickerState = rememberPickerState()

            val days = remember {
                val currentDate = LocalDate.now()
                val locale = Locale("pl", "PL")
                val formatter = DateTimeFormatter.ofPattern("d MMM", locale)

                (0..30).map { i ->
                    val date = currentDate.plusDays(i.toLong())
                    val dayOfWeek = if (i == 0) "Dzisiaj" else date.dayOfWeek.getDisplayName(java.time.format.TextStyle.SHORT, locale)
                    val formattedDate = date.format(formatter)
                    "$dayOfWeek $formattedDate"
                }
            }




            Row(modifier = Modifier.fillMaxWidth()) {
                Picker(
                    state = valuesPickerState,
                    items = days,
                    visibleItemsCount = 3,
                    modifier = Modifier.weight(0.7f),
                    textModifier = Modifier.padding(8.dp),
                    textStyle = TextStyle(fontSize = 24.sp)
                )
                Picker(
                    state = unitsPickerState,
                    items = hour,
                    visibleItemsCount = 3,
                    modifier = Modifier.weight(0.3f),
                    textModifier = Modifier.padding(8.dp),
                    textStyle = TextStyle(fontSize = 24.sp)
                )
                Picker(
                    state = unitsPickerState,
                    items = minutes,
                    visibleItemsCount = 3,
                    modifier = Modifier.weight(0.3f),
                    textModifier = Modifier.padding(8.dp),
                    textStyle = TextStyle(fontSize = 24.sp)
                )
            }
        }
    }
}