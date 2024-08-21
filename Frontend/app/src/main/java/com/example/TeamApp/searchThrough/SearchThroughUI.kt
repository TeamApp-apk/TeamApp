package com.example.TeamApp.searchThrough
import android.util.Log
import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.TeamApp.R
import com.example.TeamApp.excludedUI.ActivityCard
import com.example.TeamApp.event.CreateEventViewModel
import kotlinx.coroutines.delay

@Composable
fun SearchScreen(navController: NavController) {
    val viewModel: CreateEventViewModel = viewModel()
    val activityList = viewModel.activityList
    var showEmptyMessage by remember { mutableStateOf(false) }

    LaunchedEffect(activityList) {
        Log.d("ScreenThrough", "weszlo")
        viewModel.fetchEvents()
        if (activityList.isEmpty()) {
            delay(2000) // Opóźnienie 2 sekundy przed sprawdzeniem
            showEmptyMessage = activityList.isEmpty()
        }

    }

    val gradientColors = listOf(
        Color(0xFFE8E8E8),
        Color(0xFF007BFF)
    )

    Surface(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(brush = Brush.linearGradient(colors = gradientColors))
                .padding(horizontal = 20.dp)
                .padding(vertical = 16.dp)
        ) {
            Row(
                modifier = Modifier
                    .width(320.dp)
                    .height(90.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp, Alignment.End),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                CurrentCity(value = "WARSZAWA", modifier = Modifier.weight(1f))
            }

            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(600.dp),  // You can adjust the height to your preference
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                when {
                    showEmptyMessage -> {
                        item {
                            Box(
                                modifier = Modifier.fillParentMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                NoCurrentActivitiesBar()
                            }
                        }
                    }
                    activityList.isNotEmpty() -> {
                        items(activityList) { activity ->
                            ActivityCard(
                                iconResId = activity.iconResId,
                                date = activity.date.toString(),
                                activityName = activity.activityName,
                                currentParticipants = activity.currentParticipants,
                                maxParticipants = activity.maxParticipants,
                                location = activity.location
                            ) {
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Box(
                contentAlignment = Alignment.BottomCenter,
                modifier = Modifier.fillMaxWidth() // Upewnij się, że pasek jest na dole i zajmuje całą szerokość
            ) {
                BarOnTheBottom(navController)
            }
        }
    }
}

@Composable
@Preview(showBackground = false)

fun MainScreenPreview(){
    SearchScreen(navController = NavController(LocalContext.current))
}
@Composable
fun CurrentCity(value: String, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.CenterEnd
    ) {
        Text(
            text = value,
            style = TextStyle(
                fontSize = 26.sp,
                fontFamily = FontFamily(Font(R.font.robotoblackitalic)),
                fontWeight = FontWeight.ExtraBold,
                fontStyle = FontStyle.Italic,
                color = Color(0xFF003366),
            )
        )
    }
}
@Composable
fun NoCurrentActivitiesBar(){
    Box(contentAlignment = Alignment.Center,modifier = Modifier
        .width(193.dp)
        .height(40.dp)
        .background(color = Color(0xFFF2F2F2), shape = RoundedCornerShape(size = 16.dp))){
        Text(
            text = "Brak aktywnośći",
            style = TextStyle(
                fontSize = 15.sp,
                fontFamily = FontFamily(Font(R.font.robotoitalic)),
                fontWeight = FontWeight(900),
                fontStyle = FontStyle.Italic,
                color = Color.Black,

                textAlign = TextAlign.Center,

                )
        )


    }
}
@Composable
fun BarOnTheBottom(navController: NavController) {
    Row(
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp)
            .background(color = Color(0xFFF2F2F2), shape = RoundedCornerShape(size = 73.dp))
            .padding(horizontal = 40.dp, vertical = 8.dp)
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .height(24.dp)
        ) {
            IconButton(
                onClick = { /*TODO*/ },
                modifier = Modifier.size(24.dp)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.search),
                    contentDescription = "search",
                    modifier = Modifier.fillMaxSize()
                )
            }

            // Ikona 'pluscircle' (zamieniona miejscami z 'search')
            IconButton(
                onClick = { navController.navigate("createEvent") },
                modifier = Modifier.size(24.dp)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.pluscircle),
                    contentDescription = "circle",
                    modifier = Modifier.fillMaxSize()
                )
            }

            IconButton(
                onClick = { navController.navigate("profile") },
                modifier = Modifier.size(24.dp)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.user),
                    contentDescription = "user",
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
    }
}
