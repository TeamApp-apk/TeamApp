package com.example.TeamApp.event

import android.content.Context
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.TeamApp.auth.LoginViewModel
import com.example.compose.TeamAppTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateEventScreen(navController: NavController) {
    val viewModel: CreateEventViewModel = viewModel()
    val sport by viewModel.sport.observeAsState("")
    val address by viewModel.address.observeAsState("")
    val limit by viewModel.limit.observeAsState("")
    val description by viewModel.description.observeAsState("")
    val availableSports = viewModel.getAvailableSports()

    var expanded by remember { mutableStateOf(false) }

    Box(modifier = Modifier.fillMaxSize().padding(bottom = 15.dp)) {
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
                onValueChange = { viewModel.onAddressChange(it) },
                label = { Text("Address*") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            )

            TextField(
                value = limit,
                onValueChange = { viewModel.onLimitChange(it) },
                label = { Text("Limit of participants*") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            )

            TextField(
                value = description,
                onValueChange = { viewModel.onDescriptionChange(it) },
                label = { Text("Description") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            )

            Button(
                onClick = {
                    //viewModel.createEvent(context, sport, address, limit, description)
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
            Button(onClick = {  }) {
                Text(text = "Search")
            }
            Button(onClick = {}, colors = ButtonDefaults.buttonColors(Color.Gray)) {
                Text(text = "Create")
            }
            Button(onClick = {  }) {
                Text(text = "Profile")
            }
        }

        Button(
            onClick = { },
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