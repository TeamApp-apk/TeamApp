package com.example.TeamApp.profile

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
import com.example.compose.TeamAppTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(navController: NavController) {
    val viewModel: ProfileViewModel = viewModel()
    Box(modifier = Modifier.fillMaxSize().padding(bottom = 60.dp)) {
        Row(
            horizontalArrangement = Arrangement.SpaceAround,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .padding(10.dp)
        ) {
            Button(onClick = { viewModel.navigateToSearchThrough(navController) }) {
                Text(text = "Search")
            }
            Button(onClick = { viewModel.navigateToCreateEvent(navController) }) {
                Text(text = "Create")
            }
            Button(onClick = { }, colors = ButtonDefaults.buttonColors(Color.Gray)) {
                Text(text = "Profile")
            }
        }
        Spacer(modifier = Modifier.height(50.dp)) // Adjust the height as needed
        Column(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(15.dp)
        ) {
            Spacer(modifier = Modifier.height(50.dp)) // Adjust the height as needed
            Button(
                onClick = { viewModel.navitagateToSettings(navController) },
            ) {
                Text(text = "Ustawienia")
            }
        }
    }
}