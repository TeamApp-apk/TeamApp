package com.example.TeamApp.settings

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.TeamApp.R
import com.example.compose.primaryLight
import com.example.compose.secondaryLight
import com.example.ui.theme.fontFamily
import com.example.TeamApp.excludedUI.CustomSnackbar
import com.example.TeamApp.scrollDownChatsList.EventListViewModelProvider
import com.example.TeamApp.scrollDownChatsList.EventRepository
import com.google.firebase.Timestamp
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

@Composable
fun YourEventsScreen(navController: NavController) {
    val gradientColors = listOf(
        Color(0xFFE8E8E8),
        Color(0xFF007BFF)
    )
    val viewModel = EventListViewModelProvider.eventListViewModel
    val events by viewModel.userEvents
    val polishFormatter = DateTimeFormatter.ofPattern("d MMMM yyyy, HH:mm", Locale("pl"))

    val sortedEvents = events.sortedByDescending { event ->
        try {
            LocalDateTime.parse(event.date, polishFormatter)
        } catch (e: Exception) {
            LocalDateTime.MIN
        }
    }

    val userId = FirebaseAuth.getInstance().currentUser?.uid ?: ""

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(brush = Brush.linearGradient(colors = gradientColors))
            .padding(start = 6.dp, end = 6.dp, top = 28.dp, bottom = 72.dp)
    ) {

        Column(
            modifier = Modifier
                .align(Alignment.Center)
                .fillMaxSize()
                .background(color = Color.White, shape = RoundedCornerShape(16.dp))
                .padding(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(5.dp)
            ) {
                IconButton(
                    onClick = { navController.popBackStack() },
                    modifier = Modifier.size(26.dp)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.arrowleft),
                        contentDescription = "Back Icon"
                    )
                }
                Text(
                    text = "Twoje wydarzenia",
                    style = TextStyle(
                        fontSize = 24.sp,
                        fontFamily = FontFamily(Font(R.font.proximanovabold)),
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF003366),
                        textAlign = TextAlign.Center,
                    ),
                    modifier = Modifier.weight(1f)
                )
                Spacer(modifier = Modifier.width(26.dp))
            }


            LazyColumn(
                modifier = Modifier.fillMaxSize()
            ) {
                items(sortedEvents) { event ->

                    EventItem(event = event) {
                        navController.navigate("details/${event.id}")

                    }

                    Spacer(modifier = Modifier.height(4.dp))
                }
            }
        }
    }
}
