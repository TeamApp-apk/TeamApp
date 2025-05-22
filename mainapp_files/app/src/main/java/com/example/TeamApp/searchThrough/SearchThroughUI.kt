package com.example.TeamApp.searchThrough
import android.util.Log
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.animateScrollBy
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.navigation.NavController
import com.example.TeamApp.R
import com.example.TeamApp.excludedUI.ActivityCard
import com.example.TeamApp.event.CreateEventViewModel
import com.example.TeamApp.event.CreateEventViewModelProvider
import kotlinx.coroutines.delay



@OptIn(ExperimentalMaterialApi::class)
@Composable
fun SearchScreen(navController: NavController, onScroll: (isScrollingDown: Boolean) -> Unit) {
    val viewModel: CreateEventViewModel = CreateEventViewModelProvider.createEventViewModel
    val SearchViewModel: SearchThroughViewModel = SearchViewModelProvider.searchThroughViewModel
    var showEmptyMessage by remember { mutableStateOf(false) }
    val scrollState = rememberLazyListState()
    val activityList = remember { viewModel.activityList }
    val newlyCreatedEvent = viewModel.newlyCreatedEvent
    val context = LocalContext.current
    var isRefreshing by remember { mutableStateOf(false) }
    val pullRefreshState = rememberPullRefreshState(isRefreshing, onRefresh = {
        isRefreshing = true
    })
    val filtersOn by SearchViewModel.filtersOn.observeAsState(false)



    LaunchedEffect(isRefreshing) {
        if (isRefreshing) {
            viewModel.isDataFetched = false

            Log.d("SearchScreen", "isRefreshing = $isRefreshing, filtersOn = $filtersOn")
            if (filtersOn) {

                val selectedSports = SearchViewModel.selectedSports.value ?: listOf()
                Log.d("SearchScreen", "Fetching filtered events with sports: $selectedSports")
                viewModel.fetchFilteredEvents(selectedSports)
            } else {

                Log.d("SearchScreen", "Fetching all events")
                viewModel.fetchEvents()
            }
            delay(300)
            isRefreshing = false
        }
    }

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
        delay(1000)
        if (activityList.isEmpty()) {
            showEmptyMessage = activityList.isEmpty()
        }
    }

//    LaunchedEffect(Unit){
//        delay(700)
//        Log.d("CreateEventScreen", "Initializing map")
//        viewModel.initializeMapIfNeeded(context)
//    }

    val gradientColors = listOf(
        Color(0xFFE8E8E8),
        Color(0xFF007BFF),
    )

    ConstraintLayout(
        modifier = Modifier
            .fillMaxSize()
    ) {
        val(topAppBar, box) = createRefs()
        val configuration = LocalConfiguration.current
        val screenHeightDp = configuration.screenHeightDp.dp
        CustomAppTopBar(
            modifier = Modifier.constrainAs(topAppBar)
            {
                top.linkTo(parent.top)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
                height = Dimension.value(screenHeightDp * 0.11f)
            },
            navController = navController,
            currentCityName = "WARSZAWA",
            onCityClick = {
                // Handle the click action for "WARSZAWA"
                // For example, navigate to a city selection screen or show a dialog
                println("WARSZAWA clicked!")
            }
            // screenHeightDp = screenHeight // Pass if you use dynamic height

        )

        Box(
            modifier = Modifier
                .constrainAs(box)
                {
                    top.linkTo(topAppBar.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    bottom.linkTo(parent.bottom) // Make sure it extends to the bottom
                    width = Dimension.fillToConstraints // Fill available width
                    height = Dimension.fillToConstraints // Fill available height
                }
                //.fillMaxSize()
                .background(brush = Brush.linearGradient(colors = gradientColors))
                .padding(horizontal = 8.dp)
        ) {

            Column(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                ) {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .pullRefresh(pullRefreshState),
                        state = scrollState
                    ) {
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
                                        pinIconResId = activity.pinIconResId,
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

                    PullRefreshIndicator(
                        refreshing = isRefreshing,
                        state = pullRefreshState,
                        modifier = Modifier.align(Alignment.TopCenter),
                    )
                }
            }
        }
    }
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
            text = "Brak aktywności",
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
fun FilterButton(navController: NavController, modifier: Modifier = Modifier) {
    val hapticFeedback = LocalHapticFeedback.current
    val configuration = LocalConfiguration.current
    val height = configuration.screenHeightDp.dp

    IconButton(
        onClick = {
            navController.navigate("filterScreen")
            hapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)
        },
        modifier = Modifier
            .size(36.dp)

    ) {
        Icon(
            painter = painterResource(id = R.drawable.sliders),
            contentDescription = "search",
            tint = Color(0xFF003366),
            modifier = Modifier.fillMaxSize()
        )
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomAppTopBar(
    modifier: Modifier = Modifier,
    navController: NavController, // For FilterButton
    currentCityName: String,
    onCityClick: () -> Unit, // Action when the city name is clicked
    // screenHeightDp: Dp // Include if dynamic height based on screen is strictly needed
) {
    // val topAppBarHeight = screenHeightDp * 0.11f // If using screen-dependent height
    val topAppBarHeight = 90.dp // To match the original Row's height

    TopAppBar(
        modifier = Modifier
            .fillMaxWidth()
            .height(topAppBarHeight), // Set the height
        title = {
            // The title slot is often centered. By placing an empty Box that fills the width,
            // we allow the navigationIcon and actions to be pushed to the sides,
            // mimicking Arrangement.SpaceBetween.
            Box(modifier = Modifier.fillMaxWidth())
        },
        navigationIcon = {
            // This Box will contain the FilterButton and apply similar padding
            // as the original Column wrapper for FilterButton.
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .padding(start = 16.dp), // Original Column had padding(16.dp)
                contentAlignment = Alignment.CenterStart // Original Column was Alignment.Start
            ) {
                FilterButton(
                    navController = navController,
                    modifier = Modifier // FilterButton can define its own size/internal padding
                )
            }
        },
        actions = {
            // This Box will contain the CurrentCity, make it clickable, and apply padding.
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .clickable(onClick = onCityClick) // Makes the city area clickable
                    .padding(end = 16.dp), // Provide padding from the end, similar to FilterButton's start padding
                contentAlignment = Alignment.CenterEnd // Original Column was Alignment.End
            ) {
                CurrentCity(
                    value = currentCityName,
                    // The CurrentCity composable itself defines its text appearance.
                    // The .clickable modifier is on the parent Box to preserve text look.
                    modifier = Modifier
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color.White, // As per your example TopAppBar
            // These are default content colors; components might override them.
            navigationIconContentColor = Color(0xFF003366),
            titleContentColor = Color(0xFF003366),
            actionIconContentColor = Color(0xFF003366)
        )
    )
}

//            Row(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .height(90.dp),
//                horizontalArrangement = Arrangement.SpaceBetween,
//                verticalAlignment = Alignment.CenterVertically
//            ) {
//                Column(
//                    modifier = Modifier
//                        .weight(1f)
//                        .padding(16.dp),
//                    horizontalAlignment = Alignment.Start,
//                    verticalArrangement = Arrangement.Center
//                ) {
//                    FilterButton(
//                        navController = navController,
//                        modifier = Modifier
//                    )
//                }
//
//                Column(
//                    modifier = Modifier.weight(1f),
//                    horizontalAlignment = Alignment.End,
//                    verticalArrangement = Arrangement.Center
//                ) {
//                    CurrentCity(
//                        value = "WARSZAWA",
//                        modifier = Modifier
//                    )
//                }
//            }