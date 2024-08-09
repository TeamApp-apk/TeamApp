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
import com.example.TeamApp.searchThrough.SearchThroughViewModel
import com.example.compose.TeamAppTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(navController: NavController) {
    val viewModel: SearchThroughViewModel = viewModel()
    Box(modifier = Modifier.fillMaxSize().padding(bottom = 60.dp)){
        Row(
            horizontalArrangement = Arrangement.SpaceAround,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Button(onClick = {}, colors = ButtonDefaults.buttonColors(Color.Gray)) {
                Text(text = "Search")
            }
            Button(onClick = { viewModel.navigateToCreateEvent(navController) }) {
                Text(text = "Create")
            }
            Button(onClick = { viewModel.navigateToProfile(navController)}) {
                Text(text = "Profile")
            }
        }

    }



}