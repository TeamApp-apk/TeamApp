package com.example.TeamApp.searchThrough
import BottomNavBar
import android.util.Log
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.animateScrollBy
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.TeamApp.R
import com.example.TeamApp.excludedUI.ActivityCard
import com.example.TeamApp.event.CreateEventViewModel
import com.example.TeamApp.event.ViewModelProvider
import com.example.TeamApp.event.createTomTomMapFragment
import com.example.TeamApp.event.getApiKey
import com.tomtom.sdk.map.display.MapOptions
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.map
import java.time.format.DateTimeFormatter

@Composable
fun SearchScreen(navController: NavController, onScroll: (isScrollingDown: Boolean) -> Unit) {
    val viewModel: CreateEventViewModel = ViewModelProvider.createEventViewModel
    var showEmptyMessage by remember { mutableStateOf(false) }
    val scrollState = rememberLazyListState()
    val activityList = remember { viewModel.activityList }
    val newlyCreatedEvent = viewModel.newlyCreatedEvent
    val context = LocalContext.current

    LaunchedEffect(scrollState, newlyCreatedEvent) {
        if (newlyCreatedEvent != null) {
            val index = activityList.indexOf(newlyCreatedEvent)
            if (index >= 0) {
                scrollState.scrollToItem(0)
                delay(200)
                val averageItemSize = scrollState.layoutInfo.visibleItemsInfo
                    .firstOrNull()?.size ?: 0
                val currentOffset = scrollState.firstVisibleItemIndex * averageItemSize + scrollState.firstVisibleItemScrollOffset
                val targetOffset = index * averageItemSize
                val distance = targetOffset - currentOffset
                scrollState.animateScrollBy(distance.toFloat(), animationSpec = tween(durationMillis = 500))
                delay(500)
                viewModel.clearNewlyCreatedEvent()
            }
        }
    }



    LaunchedEffect(Unit) {
        if (activityList.isEmpty()) {
            delay(1000) // Delay 2 seconds before checking
            showEmptyMessage = activityList.isEmpty()
        }
    }

    val gradientColors = listOf(
        Color(0xFFE8E8E8),
        Color(0xFF007BFF),
    )
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(brush = Brush.linearGradient(colors = gradientColors))
            .padding(horizontal = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(90.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp, Alignment.End),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                CurrentCity(value = "WARSZAWA", modifier = Modifier.weight(1f))
            }

            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f) // Ensure LazyColumn fills the remaining space
                , state = scrollState
            )  {
                when {
                    showEmptyMessage -> {
                        item {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(horizontal = 20.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                NoCurrentActivitiesBar()
                            }
                        }
                    }
                    activityList.isNotEmpty() -> {
                        items(activityList) { activity ->
                            val isNewlyCreated = activity == newlyCreatedEvent
                            ActivityCard(
                                iconResId = activity.iconResId,
                                date = activity.date,
                                activityName = activity.activityName,
                                currentParticipants = activity.currentParticipants,
                                maxParticipants = activity.maxParticipants,
                                location = activity.location,
                                isHighlighted = isNewlyCreated,
                                onClick = {
                                    navController.navigate("details/${activity.id}")
                                }
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                        }
                    }
                }
            }
        }
    }
}

//@Composable
//@Preview(showBackground = false)
//
//fun MainScreenPreview(){
//    SearchScreen(navController = NavController(LocalContext.current))
//}
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
