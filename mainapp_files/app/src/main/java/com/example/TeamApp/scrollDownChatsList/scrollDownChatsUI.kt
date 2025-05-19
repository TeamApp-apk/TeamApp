import androidx.compose.foundation.background
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.runtime.Composable
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.layout.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.TeamApp.scrollDownChatsList.EventItem

import com.example.TeamApp.scrollDownChatsList.EventListViewModelProvider

@Composable
fun ScrollDownChat(
    navController: NavController,
) {
    val viewModel = EventListViewModelProvider.eventListViewModel
    val events by viewModel.userEvents

    val listState = rememberLazyListState()

    LaunchedEffect(events.size) {
        if (events.isNotEmpty()) {
            // scroll na koniec listy (do najnowszego eventu)
            listState.animateScrollToItem(events.size - 1)
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
            .padding(horizontal = 8.dp)
    ) {
        LazyColumn(
            state = listState
        ) {
            items(events) { event ->
                EventItem(event = event) {
                    navController.navigate("chat/${event.id}")
                }
                Spacer(modifier = Modifier.height(4.dp))
            }
        }
    }
}
