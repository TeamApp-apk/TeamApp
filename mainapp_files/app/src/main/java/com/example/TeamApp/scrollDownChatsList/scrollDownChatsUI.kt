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
import androidx.compose.material.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.navigation.NavController
import com.example.TeamApp.R
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
        ConstraintLayout {
            val (titleRef,lazyColumnRef) = createRefs()
            Text(
                text = "Twoje Czaty",
                style = TextStyle(
                    fontSize = 27.sp,
                    fontFamily = FontFamily(Font(R.font.proximanovabold)),
                    fontWeight = FontWeight(900),
                    color = Color(0xFF003366),
                    textAlign = TextAlign.Center,
                    lineHeight = 25.sp,
                ),
                modifier = Modifier.fillMaxWidth().constrainAs(titleRef) {

                    top.linkTo(parent.top, margin = 60.dp)

                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
            )
            LazyColumn(



                modifier = Modifier
                    .constrainAs(lazyColumnRef) {
                        top.linkTo(titleRef.bottom, margin = 30.dp)
                        bottom.linkTo(parent.bottom)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    }
                    .padding(horizontal = 5.dp),
                state = listState
            ) {
                items(events) { event ->
                    EventItem(event = event) {
                        navController.navigate("chat/${event.id}")
                    }
                    Spacer(modifier = Modifier.height(2.dp))
                }
            }
        }
    }
}
