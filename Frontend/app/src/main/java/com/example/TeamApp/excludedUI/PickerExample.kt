package com.example.TeamApp.excludedUI
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException
import java.time.temporal.ChronoField
import java.util.Locale


@Composable
fun PickerExample(
    selectedDate: String?,
    onDateTimeSelected: (String) -> Unit) {


    Surface(modifier = Modifier.fillMaxSize()) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.background(Color.White)
        ) {
            val currentTime = LocalTime.now()
            val currentHour = currentTime.hour
            val currentMinute = currentTime.minute

            val minutes = remember {
                val startMinute = currentMinute
                val minutesList = mutableListOf<String>()
                for (i in 0..59) {
                    val minute = (startMinute + i) % 60
                    minutesList.add(String.format("%02d", minute))
                }
                minutesList
            }
            val hours = remember {
                val startHour = currentHour
                val hoursList = mutableListOf<String>()
                for (i in 0..23) {
                    val hour = (startHour + i) % 24
                    hoursList.add(String.format("%02d", hour))
                }
                hoursList
            }

            val currentDate = LocalDate.now()
            val dayPickerState = rememberPickerState()
            val hourPickerState = rememberPickerState()
            val minutesPickerState = rememberPickerState()
            val locale = Locale("pl", "PL")
            val selecteddateFormatter = DateTimeFormatter.ofPattern("d/M/yyyy", locale)

            val days = remember {
                val currentDate = LocalDate.now()
                val locale = Locale("pl", "PL")
                val formatter = DateTimeFormatter.ofPattern("d MMM", locale)

                (0..70).map { i ->
                    val date = currentDate.plusDays(i.toLong())
                    val dayOfWeek = if (i == 0) "Dzisiaj" else date.dayOfWeek.getDisplayName(java.time.format.TextStyle.SHORT, locale)
                    val formattedDate = date.format(formatter)
                    "$dayOfWeek $formattedDate"
                }
            }


            Row(modifier = Modifier
                .fillMaxWidth())

            {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.weight(0.7f)
                ) {
                    Text(
                        text = "Dzień",
                        style = TextStyle(fontSize = 22.sp),
                    )
                    Picker(
                        state = dayPickerState,
                        items = days,
                        visibleItemsCount = 3,
                        modifier = Modifier.background(Color.White),
                        textModifier = Modifier.padding(8.dp),
                        textStyle = TextStyle(fontSize = 24.sp),
                        onScrollToIndex = { index ->
                            dayPickerState.selectedIndex = index
                        },
                        loop = false
                    )
                }

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.weight(0.3f)
                ) {
                    Text(
                        text = "godz",
                        style = TextStyle(fontSize = 22.sp),
                    )
                    Picker(
                        state = hourPickerState,
                        items = hours,
                        visibleItemsCount = 3,
                        modifier = Modifier.background(Color.White),
                        textModifier = Modifier.padding(8.dp),
                        textStyle = TextStyle(fontSize = 24.sp),
                        onScrollToIndex = { index ->
                            hourPickerState.selectedIndex = index
                        }
                    )
                }

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.weight(0.3f)
                ) {
                    Text(
                        text = "min",
                        style = TextStyle(fontSize = 22.sp),
                    )
                    Picker(
                        state = minutesPickerState,
                        items = minutes,
                        visibleItemsCount = 3,
                        modifier = Modifier.background(Color.White),
                        textModifier = Modifier.padding(8.dp),
                        textStyle = TextStyle(fontSize = 24.sp),
                                onScrollToIndex = { index ->
                            minutesPickerState.selectedIndex = index
                        }
                    )
                }
            }
            val currentYear = LocalDate.now().year
            val selectedDay = days[dayPickerState.selectedIndex]
            val selectedHour = hours[hourPickerState.selectedIndex]
            val selectedMinute = minutes[minutesPickerState.selectedIndex]


            selectedDate?.takeIf {it.isNotEmpty()}?.let {
                try {
                    Log.e("PickerExample", "new calendar value: " + it)

                    val selectedLocalDate = LocalDate.parse(it, selecteddateFormatter)
                    val formatter = DateTimeFormatter.ofPattern("d MMMM", Locale("pl"))

                    val selectedDayIndex = days.indexOfFirst { dayString ->

                        val dayWithoutWeekday = formatDate(removeDayOfWeek(dayString))
                        Log.e("PickerExample", "dayWithoutWeekday: " + dayWithoutWeekday)
                        val parsedDay = formatter.parse(dayWithoutWeekday)
                        val dayOfMonth = parsedDay.get(ChronoField.DAY_OF_MONTH)
                        val monthOfYear = parsedDay.get(ChronoField.MONTH_OF_YEAR)
                        val year = LocalDate.now().year
                        val date = LocalDate.of(year, monthOfYear, dayOfMonth)

                        selectedLocalDate.isEqual(date)
                    }


                    if (selectedDayIndex != -1) {
                        dayPickerState.selectedIndex = selectedDayIndex
                    }
                    else{}
                } catch (e: Exception) {
                    Log.e("PickerExample", "Error parsing selectedDate: $it", e)
                }
            }


            val isToday = selectedDay.startsWith("Dzisiaj")

//            val adjustedHours = if (isToday) {
//                hours.filter { it.toInt() >= currentHour }
//            } else {
//                hours
//            }
//
//            val adjustedMinutes = if (isToday && selectedHour.toInt() == currentHour) {
//                minutes.filter { it.toInt() >= currentMinute }
//            } else {
//                minutes
//            }
//
//            LaunchedEffect(isToday, selectedHour) {
//                if (isToday) {
//                    val validHourIndex = adjustedHours.indexOf(selectedHour).coerceAtLeast(0)
//                    hourPickerState.selectedIndex = (validHourIndex)
//                   // Log.e("Picker", "changing value: " + hourPickerState.selectedIndex)
//                }
//            }
//
//            LaunchedEffect(isToday, selectedHour, selectedMinute) {
//                if (isToday && selectedHour.toInt() == currentHour) {
//                    val validMinuteIndex = adjustedMinutes.indexOf(selectedMinute).coerceAtLeast(0)
//                    minutesPickerState.selectedIndex = (validMinuteIndex)
//                    // Log.e("Picker", "changing value: " + minutesPickerState.selectedIndex)
//                }
//            }

            val selectedDateTime = if (selectedDay.startsWith("Dzisiaj")) {
                val currentDateFormatted = currentDate.format(DateTimeFormatter.ofPattern("d MMMM", locale))
                "$currentDateFormatted $currentYear, $selectedHour:$selectedMinute"
            } else {
                val dateFormatted = formatDate(removeDayOfWeek(selectedDay))
                "$dateFormatted $currentYear, $selectedHour:$selectedMinute"
            }
            onDateTimeSelected(selectedDateTime)
        }
    }
}
fun formatDate(dateString: String): String {
    val inputFormat = SimpleDateFormat("d MMM", Locale("pl"))
    val outputFormat = SimpleDateFormat("d MMMM", Locale("pl"))
    val date = inputFormat.parse(dateString)
    Log.e("formatDate", "formatted date" + date)
    return outputFormat.format(date)
}

fun removeDayOfWeek(dateString: String): String {
    val parts = dateString.split(" ", limit = 2)
    return if(parts.size > 1) parts[1] else dateString
}