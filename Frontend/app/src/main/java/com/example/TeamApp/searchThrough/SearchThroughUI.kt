package com.example.TeamApp.searchThrough
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.animateScrollBy
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.TeamApp.R
import com.example.TeamApp.excludedUI.ActivityCard
import com.example.TeamApp.event.CreateEventViewModel
import com.example.TeamApp.event.ViewModelProvider
import kotlinx.coroutines.delay

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
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(90.dp)
                .padding(8.dp),
            ) {
                SearchBar(navController = navController)
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
@Composable
fun SearchBar(navController: NavController, modifier: Modifier = Modifier) {
    val viewModel: CreateEventViewModel = ViewModelProvider.createEventViewModel
    val hapticFeedback = LocalHapticFeedback.current
    val configuration = LocalConfiguration.current
    val height = configuration.screenHeightDp.dp

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .background(
                Color.White,
                shape = RoundedCornerShape(16.dp)
            )
            .clickable {
                navController.navigate("filterScreen")
                hapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)
            } // Kliknięcie otwiera popup
            .padding(16.dp)
            .height(28.dp )
            .heightIn(min = 28.dp),
        contentAlignment = Alignment.Center

    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            Icon(
                painter = painterResource(id = R.drawable.search),
                contentDescription = "search",
                tint = Color.Gray,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Szukaj",
                modifier = Modifier
                    .weight(1f),
                    //.wrapContentSize(Alignment.Center),
                style = TextStyle(
                    fontSize = 16.sp,
                    fontFamily =  FontFamily(Font(R.font.proximanovaregular)) ,
                    fontWeight =  FontWeight.Medium,
                    color = Color.Gray,
                    textAlign = TextAlign.Start
                ),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun SearchBar(navController: NavController, modifier: Modifier = Modifier) {
//
//    val viewModel: CreateEventViewModel = ViewModelProvider.createEventViewModel
//    val hapticFeedback = LocalHapticFeedback.current
//    val configuration = LocalConfiguration.current
//    val height = configuration.screenHeightDp.dp
//
//    val textValue = remember { mutableStateOf("") }
//    val width = configuration.screenWidthDp.dp
//    val focusManager = LocalFocusManager.current
//
//
//    TextField(
//        modifier = Modifier.fillMaxWidth()
//            .height(height * 0.00625f * 8 * density )
//            ,
//        label = { Text("Szukaj") },
//        value = textValue.value,
//        onValueChange = {newText ->
//            val allowedCharsRegex = Regex("^[0-9\\sa-zA-Z!@#\$%^&*()_+=\\-{}\\[\\]:\";'<>?,./]*\$")
//            if (allowedCharsRegex.matches(newText)) {
//                textValue.value = newText
//            }
//
//        },
//        keyboardOptions = KeyboardOptions.Default.copy(
//            imeAction = ImeAction.Done,
//            keyboardType = KeyboardType.Password
//        ),
//        keyboardActions = KeyboardActions(
//            onDone = {
//                focusManager.clearFocus()
//            }
//        ),
//        colors = TextFieldDefaults.textFieldColors(
//            focusedIndicatorColor = Color.Transparent,
//            unfocusedIndicatorColor = Color.Transparent,
//            containerColor = Color.White
//
//        ),
//        leadingIcon = {
//            Icon(
//                painter = painterResource(id = R.drawable.search),
//                contentDescription = "search",
//                tint = Color.Gray,
//                modifier = Modifier.size(24.dp)
//            )
//        },
//        shape = MaterialTheme.shapes.medium.copy(all = CornerSize(height * 0.023f))
//    )
//}