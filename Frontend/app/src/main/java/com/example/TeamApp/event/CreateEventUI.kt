package com.example.TeamApp.event

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.TeamApp.data.Event

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateEventScreen(navController: NavController) {
    val viewModel: CreateEventViewModel = viewModel()
    val sport by viewModel.sport.observeAsState("")
    val address by viewModel.location.observeAsState("")
    val limit by viewModel.limit.observeAsState("")
    val description by viewModel.description.observeAsState("")
    val availableSports = viewModel.getAvailableSports()
    val allowedCharsRegex = Regex("^[0-9\\sa-zA-Z!@#\$%^&*()_+=\\-{}\\[\\]:\";'<>?,./]*\$")
    val location by viewModel.location.observeAsState("")
    val date by viewModel.date.observeAsState("")

    var showSnackbar by remember { mutableStateOf(false) }
    var snackbarMessage by remember { mutableStateOf("") }
    var expanded by remember { mutableStateOf(false) }

    Box(modifier = Modifier
        .fillMaxSize()
        .padding(bottom = 15.dp)) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .align(Alignment.Center)
                .padding(16.dp)
        ) {
            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { expanded = !expanded }
            ) {
                TextField(
                    value = sport,
                    onValueChange = { viewModel.onSportChange(it) },
                    label = { Text("Sport*") },
                    readOnly = true,
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                        .menuAnchor()
                )
                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    availableSports.forEach { sport ->
                        DropdownMenuItem(
                            text = { Text(sport) },
                            onClick = {
                                viewModel.onSportChange(sport)
                                expanded = false
                            }
                        )
                    }
                }
            }

            TextField(
                value = address,
                onValueChange = { newText ->
                    if (allowedCharsRegex.matches(newText)) {
                        viewModel.onAddressChange(newText)
                    }
                },
                keyboardOptions = KeyboardOptions.Default.copy(
                    imeAction = ImeAction.Done,
                    keyboardType = KeyboardType.Password
                ),
                label = { Text("Address*") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            )

            TextField(
                value = limit,
                onValueChange = { newText ->
                    val numberRegex = Regex("^[0-9]*\$")
                    if (numberRegex.matches(newText)) {
                        viewModel.onLimitChange(newText)
                    }
                },
                keyboardOptions = KeyboardOptions.Default.copy(
                    imeAction = ImeAction.Done,
                    keyboardType = KeyboardType.Text
                ),
                label = { Text("Limit of participants*") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            )
            TextField(
                value = description,
                onValueChange = { newText ->
                    if (allowedCharsRegex.matches(newText)) {
                        viewModel.onDescriptionChange(newText)
                    } },
                label = { Text("Description") },
                keyboardOptions = KeyboardOptions.Default.copy(
                    imeAction = ImeAction.Done,
                    keyboardType = KeyboardType.Text
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            )

            Button(
                onClick = {
                    Log.d("CreateEventScreen", "Submit button clicked")
                    val participantLimit = limit.toIntOrNull()
                    Log.e("CreateEventScreen", "Participant limit: $participantLimit")
                    Log.e("CreateEventScreen", "Sport: $sport")
                    Log.e("CreateEventScreen", "Address: $address")
                    Log.e("CreateEventScreen", "Date: $date")
                    Log.e("CreateEventScreen", "Location: $location")
                    if (sport.isNotEmpty() && address.isNotEmpty()  && location.isNotEmpty()) {
                        Log.d("CreateEventScreen", "Valid input")
                        val newEvent = Event(
                            iconResId = Event.sportIcons[sport] ?: "",
                            date = "23 PAŹDZIERNIKA 12:45",
                            activityName = sport,
                            currentParticipants = 0,
                            maxParticipants = limit.toInt(),
                            location = location
                        )
                        viewModel.createEvent(newEvent)
                        snackbarMessage = "Event created successfully!"
                        showSnackbar = true
                    } else {
                        snackbarMessage = "Please fill all required fields correctly."
                        showSnackbar = true
                    }
                },
                modifier = Modifier.padding(vertical = 16.dp)
            ) {
                Text(text = "Submit")
            }
        }

        Row(
            horizontalArrangement = Arrangement.SpaceAround,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Button(onClick = { navController.navigate("search") }) {
                Text(text = "Search")
            }
            Button(onClick = {}, colors = ButtonDefaults.buttonColors(Color.Gray)) {
                Text(text = "Create")
            }
            Button(onClick = { viewModel.navigateToProfile(navController) }) {
                Text(text = "Profile")
            }
        }

        Button(
            onClick = { viewModel.logout(navController) },
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(16.dp)
        ) {
            Text(text = "Logout")
        }
    }
}

//@Preview(showBackground = true)
//@Composable
//fun DefaultPreview() {
//    TeamAppTheme {
//        Surface(color = MaterialTheme.colorScheme.background) {
//            CreateEventScreen(viewModel = viewModel(), context = LocalContext.current)
//        }
//    }
//}

@Composable
fun CustomSnackbar(success: Boolean) {
    Snackbar(
        modifier = Modifier
            .padding(80.dp)
            .wrapContentSize(Alignment.Center),
        shape = RoundedCornerShape(60.dp),
        containerColor = if (success) Color(0xFF4CAF50) else Color(0xFFF44336),
        contentColor = Color.White,
        action = {
        }
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Success",
                style = MaterialTheme.typography.bodyLarge.copy(color = Color.White)
            )
        }
    }
}