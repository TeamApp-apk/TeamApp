@file:OptIn(ExperimentalMaterial3Api::class)
package com.example.TeamApp.event

//import androidx.hilt.navigation.compose.hiltViewModel
import UserViewModel
import android.annotation.SuppressLint
import androidx.compose.ui.text.input.KeyboardType
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.util.Log
import androidx.activity.result.launch
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.animateTo
import androidx.compose.animation.core.tween
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.animation.core.updateTransition
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
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Divider
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.constraintlayout.motion.widget.Animatable
import androidx.navigation.NavController
import com.airbnb.lottie.compose.*
import com.example.TeamApp.R
import com.example.TeamApp.data.Coordinates
import com.example.TeamApp.data.Event
import com.example.TeamApp.data.Suggestion
import com.example.TeamApp.excludedUI.EventButton
import com.example.TeamApp.excludedUI.PickerExample
import com.google.firebase.auth.FirebaseAuth
import com.tomtom.sdk.search.SearchCallback
import com.tomtom.sdk.search.SearchOptions
import com.tomtom.sdk.search.SearchResponse
import com.tomtom.sdk.search.common.error.SearchFailure
import com.tomtom.sdk.search.online.OnlineSearch
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Locale
import java.util.Properties
import androidx.compose.animation.core.tween
import kotlinx.coroutines.launch

@Composable
fun CreateEventScreen(navController: NavController, userViewModel: UserViewModel) {
    val user by userViewModel.user.observeAsState()
    val viewModel: CreateEventViewModel = CreateEventViewModelProvider.createEventViewModel

    val lazyListState = rememberLazyListState()
    var shouldScrollAfterParticipantsSelection by remember { mutableStateOf(false) }

    val sport by viewModel.sport.observeAsState("")

    val eventName by viewModel.eventName.observeAsState("")
    val price by viewModel.price.observeAsState("")
    val skillLevel by viewModel.skillLevel.observeAsState("")

    val address by viewModel.location.observeAsState("")
    val limit by viewModel.limit.observeAsState("")
    val description by viewModel.description.observeAsState("")
    val locationID by viewModel.locationID.observeAsState(emptyMap())
    val availableSports = viewModel.getAvailableSports()
    val allowedCharsRegex = Regex("^[0-9\\sa-zA-Z!@#\$%^&*()_+=\\-{}\\[\\]:\";'<>?,./]*\$")
    val location by viewModel.location.observeAsState("")
    val dateTime by viewModel.dateTime.observeAsState("")
    val context = LocalContext.current
    var isLoading by remember { mutableStateOf(false) }
    var loadingComplete by remember { mutableStateOf(false) }
    var showTick by remember { mutableStateOf(false) } // Dodany stan do zarządzania animacją ticka
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.loading_to_tick))
    var isPlaying by remember { mutableStateOf(false) }
    val progress by animateLottieCompositionAsState(
        composition,
        isPlaying = isPlaying,
        iterations = 1,
        speed = 1f,
        restartOnPlay = false
    )
    val coroutineScope = rememberCoroutineScope()
    LaunchedEffect(limit) { // This lambda block (blockA) IS a CoroutineScope
        if (limit.isNotEmpty() && shouldScrollAfterParticipantsSelection) {
            if (lazyListState.layoutInfo.totalItemsCount > 7) {

                val currentScrollOffset = lazyListState.firstVisibleItemScrollOffset
                val itemSize = lazyListState.layoutInfo.visibleItemsInfo.firstOrNull()?.size ?: 0
                val estimatedTargetItemOffset = (7 - lazyListState.firstVisibleItemIndex) * itemSize +
                        (lazyListState.layoutInfo.visibleItemsInfo.find { it.index == 7 }?.offset ?: 0)

                val scrollAnimatable = androidx.compose.animation.core.Animatable(initialValue = currentScrollOffset.toFloat())

                val job = this.launch {
                    scrollAnimatable.animateTo(
                        targetValue = estimatedTargetItemOffset.toFloat(),
                        animationSpec = androidx.compose.animation.core.tween(durationMillis = 800)
                    ) {
                        val delta = this.value - lazyListState.firstVisibleItemScrollOffset.toFloat()
                        lazyListState.dispatchRawDelta(delta)
                    }

                    lazyListState.animateScrollToItem(index = 7, scrollOffset = 0)
                }

            }
            shouldScrollAfterParticipantsSelection = false
        }
    }

    var showSnackbar by remember { mutableStateOf(false) }
    var snackbarMessage by remember { mutableStateOf("") }
    var snackbarSuccess by remember { mutableStateOf(false) }

    val transition = updateTransition(targetState = showTick, label = "Tick Transition")

    val buttonScale by transition.animateFloat(
        transitionSpec = { tween(durationMillis = 300) },
        label = "Button Scale"
    ) { show ->
        if (show) 1f else 0f
    }

    val tickAlpha by animateFloatAsState(
        targetValue = if (showTick) 0f else 1f,
        animationSpec = tween(durationMillis = 300)
    )

    val gradientColors = listOf(
        Color(0xFFE8E8E8),
        Color(0xFF007BFF)
    )
    var showDialog by remember { mutableStateOf(false) }

    LaunchedEffect(progress) {
        if(user==null){
            val firebaseUser = FirebaseAuth.getInstance().currentUser
            firebaseUser?.email?.let { email ->
                userViewModel.fetchUserFromFirestore(email)
            }
        }
        if (progress == 1.0f) {
            isPlaying = false
            showTick = true
            delay(150  )
            showTick = false
            isLoading = false
            loadingComplete = false
            navController.navigate("search"){
                popUpTo("search"){inclusive = true}
            }

            viewModel.resetFields()
        }
    }
//    LaunchedEffect(Unit){
//        delay(700)
//        Log.d("CreateEventScreen", "Initializing map")
//        viewModel.initializeMapIfNeeded(context)
//    }

    Surface( modifier = Modifier.fillMaxSize() ) {
        Box(modifier = Modifier
            .fillMaxSize()
            .background(brush = Brush.linearGradient(colors = gradientColors)) ){}

        ConstraintLayout(modifier = Modifier.fillMaxSize()) {
            val (topText, menu) = createRefs()
            val topTextStart = createGuidelineFromStart(0.1f)
            val topTextEnd = createGuidelineFromStart(0.9f)
            val topTextTop = createGuidelineFromTop(0.07f)
            val topTextBottom = createGuidelineFromTop(0.14f)
            Text(
                text = "Stwórz wydarzenie",
                style = TextStyle(
                    fontSize = 27.sp,
                    fontFamily = FontFamily(Font(R.font.proximanovabold)),
                    fontWeight = FontWeight(900),
                    color = Color(0xFF003366),
                    textAlign = TextAlign.Center,
                    lineHeight = 25.sp,
                ),
                modifier = Modifier
                    .constrainAs(topText) {
                        top.linkTo(topTextTop)
                        bottom.linkTo(topTextBottom)
                        start.linkTo(topTextStart)
                        end.linkTo(topTextEnd)
                        height = Dimension.fillToConstraints
                        width = Dimension.fillToConstraints
                        centerHorizontallyTo(parent)
                    }
            )

            val menuStart = createGuidelineFromStart(0.05f)
            val menuEnd = createGuidelineFromStart(0.95f)
            val menuTop = createGuidelineFromTop(0.17f)
            val menuBottom = createGuidelineFromTop(0.9f)
            Box (
                modifier = Modifier
                    .constrainAs(menu) {
                        top.linkTo(menuTop)
                        bottom.linkTo(menuBottom)
                        start.linkTo(menuStart)
                        end.linkTo(menuEnd)
                        height = Dimension.fillToConstraints
                        width = Dimension.fillToConstraints
                    }
                    .background(color = Color(0xFFF2F2F2), shape = RoundedCornerShape(size = 30.dp))
            ) {
                Column(modifier = Modifier.fillMaxSize()) {
                    // Formularz z polami w LazyColumn
                    LazyColumn(
                        state = lazyListState,
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                            .padding(horizontal = 16.dp, vertical = 8.dp)
                    ) {
                        item {
                            Spacer(modifier = Modifier.height(16.dp))
                            EventNameButton(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(60.dp)
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                        }


                        item {
                            DescriptionInputField(
                                description = description,
                                onEditClick = { showDialog = true },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(60.dp)
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                        }

                        item {
                            SearchStreetField(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(60.dp)
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                        }

                        item {
                            SportPopupButton(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(60.dp)
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                        }

                        item {
                            ParticipantsPopupButton(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(60.dp),
                                onParticipantsSelected = {
                                    shouldScrollAfterParticipantsSelection = true
                                }
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                        }

                        item {
                            MyDateTimePickerv2(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(60.dp)
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                        }

                        item{
                            PriceButton(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(60.dp)
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                        }
                        item{
                            SkillLevelButton(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(60.dp)
                            )
                        }

                    }
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                            .height(100.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        if (isLoading) {
                            LottieAnimation(
                                composition = composition,
                                progress = progress,
                                modifier = Modifier
                                    .size(100.dp)
                                    .align(Alignment.Center)
                            )
                            LaunchedEffect(Unit) {
                                isPlaying = true
                            }
                        } else if (showTick) {
                            Icon(
                                imageVector = Icons.Default.Check,
                                contentDescription = "Success",
                                tint = Color.Green,
                                modifier = Modifier
                                    .size(40.dp)
                                    .alpha(tickAlpha)
                                    .align(Alignment.Center)
                            )
                        } else {
                            EventButton(
                                text = "Stwórz",
                                onClick = {
                                    Log.d("CreateEventScreen", "Submit button clicked")
                                    val participantLimit = limit.toIntOrNull()
                                    Log.d("CreateEventScreen", "Address: $address")
                                    if (sport.isNotEmpty() && address.isNotEmpty() && location.isNotEmpty()) {
                                        isLoading = true
                                        isPlaying = true
                                        Log.d("CreateEventScreen", "Valid input")
                                        //Log.d("CreateEventScreen", "Valid input")
                                        val creatorID = user?.userID ?: ""
                                        val participantsList =
                                            if (creatorID.isNotEmpty()) mutableListOf<Any>(creatorID) else mutableListOf()
                                        val newEvent = Event(
                                            participants = participantsList,
                                            creatorID = creatorID.takeIf { it.isNotEmpty() },
                                            iconResId = Event.sportIcons[sport] ?: "",
                                            pinIconResId = Event.sportPinIcons[sport] ?: "",
                                            date = dateTime,
                                            activityName = sport,
                                            eventName = eventName,
                                            price = price,
                                            skillLevel = skillLevel,
                                            currentParticipants = participantsList.size,
                                            maxParticipants = participantLimit ?: 0,
                                            location = address,
                                            description = description,
                                            locationID = locationID
                                        )

                                        viewModel.createEvent(newEvent, creatorID.toString()) { result ->
                                            if (result == null) {
                                                snackbarMessage = "Utworzono Event"
                                                snackbarSuccess = true
                                            } else {
                                                snackbarMessage = result
                                                snackbarSuccess = false
                                            }
                                            showSnackbar = true
                                        }
                                    } else {
                                        snackbarMessage = "Uzupełnij wszystkie pola"
                                        snackbarSuccess = false
                                        showSnackbar = true
                                    }
                                },
                            )
                        }
                    }
                }
            }

        }
    }
    if (showDialog) {
        DescriptionDialog(
            initialText = description,
            onDismiss = { showDialog = false },
            onSave = { newText ->
                viewModel.onDescriptionChange(newText)
                showDialog = false
            }
        )
    }

}
@Composable
fun DescriptionInputField(
    description: String,
    onEditClick: () -> Unit,
    modifier: Modifier
) {
    val hapticFeedback = LocalHapticFeedback.current
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .background(
                color = Color.White,
                shape = RoundedCornerShape(16.dp)
            )
            .clickable {
                hapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)
                onEditClick()
            },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = if (description.isEmpty()) "Opis" else description,
            textAlign = TextAlign.Center,
            style = TextStyle(
                color = if (description.isEmpty()) Color.Gray else Color.Black,
                fontSize = 16.sp
            ),
            fontFamily = if(description.isEmpty()) FontFamily(Font(R.font.proximanovaregular)) else FontFamily(Font(R.font.proximanovabold)),
            fontWeight = if(description.isEmpty()) FontWeight.Medium else FontWeight.Bold,
            color = if(description.isEmpty()) Color.Gray else Color(0xFF003366),
            modifier = Modifier.padding(horizontal = 50.dp),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DescriptionDialog(
    initialText: String,
    onDismiss: () -> Unit,
    onSave: (String) -> Unit
) {
    val allowedCharsRegex = Regex("^[0-9\\sa-zA-Z!@#\$%^&*()_+=\\-{}\\[\\]:\";'<>?,./]*\$")
    var text by remember { mutableStateOf(initialText) }
    val viewModel: CreateEventViewModel = CreateEventViewModelProvider.createEventViewModel

    val focusRequester = remember { FocusRequester() }

    val keyboardController = LocalSoftwareKeyboardController.current
    val hapticFeedback = LocalHapticFeedback.current
    Dialog(onDismissRequest = onDismiss) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White, shape = RoundedCornerShape(16.dp))
                .padding(16.dp)
        ) {
            Column {
                Text(
                    text = "Opisz pokrótce szczegóły wydarzenia",
                    style = TextStyle(
                        fontSize = 18.sp,
                        fontFamily = FontFamily(Font(R.font.proximanovabold)),
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center  // Wyśrodkowanie samego tekstu
                    ),
                    modifier = Modifier
                        .padding(bottom = 8.dp)
                        .fillMaxWidth()
                        .align(Alignment.CenterHorizontally)  // Wyśrodkowanie całego komponentu
                )

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            Color.White,
                            shape = RoundedCornerShape(8.dp)
                        ) // Ensure the background is white
                        .padding(horizontal = 8.dp) // Add some padding inside the TextField
                ) {
                    TextField(
                        textStyle = TextStyle(
                            fontSize = 16.sp,
                            fontFamily = FontFamily(Font(R.font.proximanovaregular)),
                            fontWeight = FontWeight.Normal
                        ),
                        value = text,
                        onValueChange = { newText -> text = newText },
                        modifier = Modifier
                            .fillMaxWidth()
                            .focusRequester(focusRequester),
                        shape = RoundedCornerShape(16.dp),
                        colors = TextFieldDefaults.textFieldColors(
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent
                        )
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)

                ) {
                    Text(
                        text = "Anuluj",
                        fontSize = 22.sp,
                        fontFamily = FontFamily(Font(R.font.proximanovabold)),
                        fontWeight = FontWeight.Bold,
                        color = Color.Black,
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .clickable(
                                interactionSource = remember { MutableInteractionSource() },
                                indication = rememberRipple() // Efekt "fal" przy kliknięciu
                            ) {
                                hapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)
                                onDismiss()
                            }
                            .padding(8.dp)
                    )
                    Divider(
                        color = Color.Black,
                        thickness = 6.dp,
                        modifier = Modifier
                            .height(20.dp)
                            .width(1.dp)
                    )
                    Text(
                        text = "Gotowe",
                        fontSize = 22.sp,
                        color = Color.Black,
                        fontFamily = FontFamily(Font(R.font.proximanovabold)),
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .clickable(
                                interactionSource = remember { MutableInteractionSource() },
                                indication = rememberRipple() // Efekt "fal" przy kliknięciu
                            ) {
                                viewModel.onDescriptionChange(text)
                                hapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)
                                onSave(text)
                            }
                            .padding(8.dp)
                    )
                }

            }
        }
    }
    // Automatically focus the TextField and show the keyboard when the dialog is opened
    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
        keyboardController?.show()
    }
}


fun getApiKey(context: Context): String {
    val properties = Properties()
    context.assets.open("tomtomApi_key.properties").use { inputStream ->
        properties.load(inputStream)
    }
    return properties.getProperty("API_KEY")
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchStreetField( modifier : Modifier ) {
    var query by remember { mutableStateOf("") }
    var suggestions by remember { mutableStateOf(listOf<Suggestion>()) }
    var expanded by remember { mutableStateOf(false) }
    var isDialogOpen by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current
    val focusManager = LocalFocusManager.current
    val hapticFeedback = LocalHapticFeedback.current
    val viewModel: CreateEventViewModel = CreateEventViewModelProvider.createEventViewModel
    val focusRequester = remember { FocusRequester() }
    val searchApi = remember { OnlineSearch.create(context, getApiKey(context)) }

    fun performSearch(query: String) {
        if (query.isNotEmpty()) {
            val options = SearchOptions(
                query = query,
                limit = 4,
                countryCodes = setOf("POL"),
            )

            searchApi.search(
                options,
                object : SearchCallback {
                    override fun onSuccess(result: SearchResponse) {
                        Log.d("SearchStreetField", "Search Success: ${result.results}")

                        val newSuggestions = result.results
                            .filter { it.place != null }
                            .mapNotNull {
                                val address = it.place?.address?.let { address ->
                                    listOfNotNull(
                                        address.streetNameAndNumber,
                                        address.freeformAddress,
                                    ).joinToString(" ")
                                }
                                val coordinates = it.place.coordinate.let { coord ->
                                    Coordinates(coord.latitude, coord.longitude)
                                }
                                if (address != null) {
                                    Suggestion(address, coordinates)
                                } else {
                                    null
                                }
                            }
                        Log.d("SearchStreetField", "Extracted Suggestions: $newSuggestions")
                        suggestions = newSuggestions
                        expanded = true
                    }

                    override fun onFailure(failure: SearchFailure) {
                        Log.e("SearchStreetField", "Search Failure: ${failure.message}")
                        suggestions = emptyList()
                        expanded = false
                    }
                }
            )
        } else {
            suggestions = emptyList()
            expanded = false
        }
    }

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .clip(RoundedCornerShape(16.dp))
                .background(Color.White, RoundedCornerShape(16.dp))
                .clickable {
                    isDialogOpen = true
                    hapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)
                },
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = if (query.isEmpty()) "Lokalizacja" else query,
                style = TextStyle(
                    fontSize = 16.sp,
                    fontFamily = if (query.isEmpty()) FontFamily(Font(R.font.proximanovaregular)) else FontFamily(Font(R.font.proximanovabold)),
                    fontWeight = if(query.isEmpty()) FontWeight.Medium else FontWeight.Bold,
                    color = if (query.isEmpty()) Color.Gray else Color(0xFF003366),
                    textAlign = TextAlign.Center
                )
            )
        }

        if (isDialogOpen) {
            AlertDialog(
                containerColor = Color.White,
                onDismissRequest = { isDialogOpen = false },
                title = {
                    Text(
                        text = "Wpisz lokalizację",
                        style = TextStyle(
                            fontFamily = FontFamily(Font(R.font.proximanovabold)),
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                    )
                },
                text = {
                    Column {
                        LaunchedEffect(Unit) {
                            kotlinx.coroutines.delay(100)
                            focusRequester.requestFocus()
                        }
                        TextField(
                            value = query,
                            onValueChange = { newText ->
                                query = newText
                                Log.d("SearchStreetField", "Query: $newText")
                                viewModel.onAddressChange(newText)
                                performSearch(newText)
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp)
                                .height(56.dp)
                                .focusRequester(focusRequester),
                            shape = RoundedCornerShape(16.dp),
                            colors = TextFieldDefaults.textFieldColors(
                                focusedIndicatorColor = Color.Transparent,
                                unfocusedIndicatorColor = Color.Transparent
                            ),
                            textStyle = TextStyle(fontSize = 16.sp),
                            keyboardOptions = KeyboardOptions.Default.copy(
                                imeAction = ImeAction.Done
                            ),
                            keyboardActions = KeyboardActions(
                                onDone = {
                                    focusManager.clearFocus()
                                }
                            ),
                        )

                        if (suggestions.isNotEmpty()) {
                            LazyColumn(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(8.dp)
                            ) {
                                items(suggestions) { suggestion ->
                                    Column(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .clickable {
                                                Log.d(
                                                    "SearchStreetField",
                                                    "Suggestion clicked: ${suggestion.address}"
                                                )
                                                query = suggestion.address
                                                viewModel.onAddressChange(query)
                                                suggestions = emptyList()
                                                expanded = false
                                                hapticFeedback.performHapticFeedback(
                                                    HapticFeedbackType.LongPress
                                                )
                                                focusManager.clearFocus()
                                                suggestion.coordinates?.let { coords ->
                                                    viewModel.setLocationID(mapOf(query to coords))
                                                }
                                            }
                                            .padding(8.dp)
                                    ) {
                                        Text(
                                            text = suggestion.address,
                                            style = TextStyle(
                                                fontSize = 16.sp,
                                                color = Color.Black
                                            )
                                        )
                                        Divider(color = Color.Gray, thickness = 1.dp)
                                    }
                                }
                            }
                        }
                    }
                },
                confirmButton = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp)
                    ) {
                        Text(
                            text = "Anuluj",
                            fontFamily = FontFamily(Font(R.font.proximanovabold)),
                            fontSize = 22.sp,
                            color = Color.Black,
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .clickable(
                                    interactionSource = remember { MutableInteractionSource() },
                                    indication = rememberRipple()
                                ) {
                                    hapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)
                                    isDialogOpen = false
                                }
                                .padding(8.dp)
                        )
                        Divider(
                            color = Color.Black,
                            thickness = 6.dp,
                            modifier = Modifier
                                .height(20.dp)
                                .width(1.dp)
                        )
                        Text(
                            text = "Gotowe",
                            color = Color.Black,
                            fontFamily = FontFamily(Font(R.font.proximanovabold)),
                            fontSize = 22.sp,
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .clickable(
                                    interactionSource = remember { MutableInteractionSource() },
                                    indication = rememberRipple()
                                ) {
                                    hapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)
                                    isDialogOpen = false
                                }
                                .padding(8.dp)
                        )
                    }
                }
            )
        }
    }
}




@SuppressLint("DefaultLocale")
@Composable
fun MyDateTimePickerv2 ( modifier : Modifier ) {
    val viewModel: CreateEventViewModel = CreateEventViewModelProvider.createEventViewModel
    var selectedDateTime by remember { mutableStateOf("") }
    var selectedDate by remember { mutableStateOf("") }
    val calendar = Calendar.getInstance()
    val year = calendar.get(Calendar.YEAR)
    val month = calendar.get(Calendar.MONTH)
    val day = calendar.get(Calendar.DAY_OF_MONTH)
    val hour = calendar.get(Calendar.HOUR_OF_DAY)
    val minute = calendar.get(Calendar.MINUTE)
    val date by viewModel.dateTime.observeAsState("")
    var showDialog by remember { mutableStateOf(false) }
    val originalFormat = DateTimeFormatter.ofPattern("d MMMM yyyy, HH:mm", Locale("pl", "PL"))
    val hapticFeedback = LocalHapticFeedback.current


    val datePickerDialog = DatePickerDialog(
        LocalContext.current,
        R.style.CustomTimePickerTheme,
        { _, pickedYear, pickedMonth, pickedDayOfMonth ->
            calendar.set(pickedYear, pickedMonth, pickedDayOfMonth)
            selectedDate = "$pickedDayOfMonth/${pickedMonth + 1}/$pickedYear"
        },
        year, month, day
    ).apply {
        datePicker.minDate = calendar.timeInMillis
        calendar.add(Calendar.DAY_OF_YEAR, 70)
        datePicker.maxDate = calendar.timeInMillis
    }

    // Zastępuje Button -> Box
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .background(color = Color.White)
            .clickable { showDialog = true },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = if (date.isEmpty()) "Data i godzina" else date,
            style = TextStyle(
                fontSize = 16.sp,
                lineHeight = 25.sp,
                textAlign = TextAlign.Center,
                fontFamily = if (date.isEmpty()) FontFamily(Font(R.font.proximanovaregular)) else FontFamily(Font(R.font.proximanovabold)),
                fontWeight = if (date.isEmpty()) FontWeight.Medium else FontWeight.Bold,
                color = if (date.isEmpty()) Color.Gray else Color(0xFF003366),
            ),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }

    if (showDialog) {
        Dialog(onDismissRequest = { showDialog = false }) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White, shape = RoundedCornerShape(16.dp))
            ) {
                Column {
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "Wybierz datę i godzinę",
                        style = TextStyle(
                            fontSize = 20.sp,
                            fontFamily = FontFamily(Font(R.font.proximanovabold)),
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center
                        ),
                        modifier = Modifier.padding(horizontal = 20.dp)
                    )

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp)
                            .background(
                                Color.White,
                                shape = RoundedCornerShape(8.dp)
                            )
                            .padding(horizontal = 8.dp)
                    ) {
                        val hapticFeedback = LocalHapticFeedback.current
                        PickerExample(
                            selectedDate = selectedDate,
                            onDateTimeSelected = { selectedDate ->
                                val localDateTime = LocalDateTime.parse(selectedDate, originalFormat)

                                val currentDateTime = LocalDateTime.now()

                                val finalDateTime = if (localDateTime.isBefore(currentDateTime)) {
                                    currentDateTime
                                } else {
                                    localDateTime
                                }
                                val formatter = DateTimeFormatter.ofPattern("d MMMM yyyy, HH:mm", Locale("pl"))
                                selectedDateTime = finalDateTime.format(formatter)
                                hapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)
                            }
                        )
                    }

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp)
                            .offset(y = (-20).dp)
                    ) {
                        Text(
                            text = "Kalendarz",
                            fontSize = 20.sp,
                            color = Color.Black,
                            fontFamily = FontFamily(Font(R.font.proximanovabold)),
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .clickable(
                                    interactionSource = remember { MutableInteractionSource() },
                                    indication = rememberRipple()
                                ) {
                                    hapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)
                                    datePickerDialog.show()
                                }
                                .padding(8.dp)
                        )
                        Divider(
                            color = Color.Black,
                            thickness = 6.dp,
                            modifier = Modifier
                                .height(20.dp)
                                .width(1.dp)
                        )
                        Text(
                            text = "Anuluj",
                            fontSize = 20.sp,
                            fontFamily = FontFamily(Font(R.font.proximanovabold)),
                            fontWeight = FontWeight.Bold,
                            color = Color.Black,
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .clickable(
                                    interactionSource = remember { MutableInteractionSource() },
                                    indication = rememberRipple()
                                ) {
                                    hapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)
                                    showDialog = false
                                }
                                .padding(8.dp)
                        )
                        Divider(
                            color = Color.Black,
                            thickness = 6.dp,
                            modifier = Modifier
                                .height(20.dp)
                                .width(1.dp)
                        )
                        Text(
                            text = "Gotowe",
                            fontSize = 20.sp,
                            color = Color.Black,
                            fontFamily = FontFamily(Font(R.font.proximanovabold)),
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .clickable(
                                    interactionSource = remember { MutableInteractionSource() },
                                    indication = rememberRipple()
                                ) {
                                    viewModel.onDateChange(selectedDateTime)
                                    hapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)
                                    showDialog = false
                                }
                                .padding(8.dp)
                        )
                    }
                }
            }
        }
    }
}



@SuppressLint("DefaultLocale")
@Composable
fun MyDateTimePicker(onDateChange: (String) -> Unit,
                     modifier : Modifier) {
    val viewModel: CreateEventViewModel = CreateEventViewModelProvider.createEventViewModel
    var selectedDateTime by remember { mutableStateOf("") }
    val calendar = Calendar.getInstance()
    val year = calendar.get(Calendar.YEAR)
    val month = calendar.get(Calendar.MONTH)
    val day = calendar.get(Calendar.DAY_OF_MONTH)
    val hour = calendar.get(Calendar.HOUR_OF_DAY)
    val minute = calendar.get(Calendar.MINUTE)
    val date by viewModel.dateTime.observeAsState("")

    val timePickerDialog = TimePickerDialog(
        LocalContext.current,
        R.style.CustomDatePickerTheme,
        { _, pickedHour, pickedMinute ->
            val pickedDateTime = LocalDateTime.of(
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH) + 1,
                calendar.get(Calendar.DAY_OF_MONTH),
                pickedHour,
                pickedMinute
            )
            val formatter = DateTimeFormatter.ofPattern("d MMMM yyyy, HH:mm", Locale("pl"))
            selectedDateTime = pickedDateTime.format(formatter)
            onDateChange(selectedDateTime) // Pass formatted string to onDateChange
        },
        hour, minute, true
    )

    val datePickerDialog = DatePickerDialog(
        LocalContext.current,
        R.style.CustomTimePickerTheme,
        { _, pickedYear, pickedMonth, pickedDayOfMonth ->
            calendar.set(pickedYear, pickedMonth, pickedDayOfMonth)
            selectedDateTime = "$pickedDayOfMonth/${pickedMonth + 1}/$pickedYear"
            timePickerDialog.show()
        },
        year, month, day
    )

    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Box(
            modifier = modifier
                .clip(RoundedCornerShape(16.dp))
                .background(color = Color.White, shape = RoundedCornerShape(size = 16.dp))
                .clickable { datePickerDialog.show() },
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = if (date.isEmpty()) "Data i godzina" else date,
                style = TextStyle(
                    fontSize = 16.sp,
                    lineHeight = 25.sp,
                    fontFamily = if(date.isEmpty()) FontFamily(Font(R.font.proximanovaregular)) else FontFamily(Font(R.font.proximanovabold)),
                    fontWeight = if(date.isEmpty()) FontWeight.Medium else FontWeight.Bold,
                    color = if(date.isEmpty()) Color.Gray else Color(0xFF003366),
                )
            )
        }
    }
}

@Composable
fun SportPopupButton(modifier: Modifier) {
    val viewModel: CreateEventViewModel = CreateEventViewModelProvider.createEventViewModel
    val availableSports = viewModel.getAvailableSports()
    var showDialog by remember { mutableStateOf(false) }
    val selectedSport by viewModel.sport.observeAsState("")
    val hapticFeedback = LocalHapticFeedback.current
    // Przycisk, który otwiera popup
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .background(
                Color.White,
                shape = RoundedCornerShape(16.dp)
            )
            .clickable {
                showDialog = true
                hapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)
            } // Kliknięcie otwiera popup
            .padding(16.dp)
            .heightIn(min = 28.dp),
        contentAlignment = Alignment.Center

    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = if (selectedSport.isEmpty()) "Dyscyplina" else selectedSport,
                modifier = Modifier
                    .weight(1f)
                    .wrapContentSize(Alignment.Center),
                style = TextStyle(
                    fontSize = 16.sp,
                    fontFamily = if (selectedSport.isEmpty()) FontFamily(Font(R.font.proximanovaregular)) else FontFamily(Font(R.font.proximanovabold)),
                    fontWeight = if(selectedSport.isEmpty()) FontWeight.Medium else FontWeight.Bold,
                    color = if (selectedSport.isEmpty()) Color.Gray else Color(0xFF003366),
                    textAlign = TextAlign.Center
                ),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }

    // Popup dialog z przewijalną listą
    if (showDialog) {
        Dialog(onDismissRequest = { showDialog = false }) {
            Surface(
                shape = RoundedCornerShape(16.dp),
                color = Color.White,
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "Wybierz dyscyplinę",
                        style = TextStyle(
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily(Font(R.font.proximanovabold)),
                            color = Color.Black
                        ),
                        modifier = Modifier
                            .padding(bottom = 16.dp)
                            .align(Alignment.CenterHorizontally)
                    )

                    // Lista przewijalna
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(max = 300.dp) // Ograniczenie wysokości popupu
                    ) {
                        items(availableSports) { sport ->
                            Text(
                                text = sport,
                                modifier = Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .fillMaxWidth()
                                    .clickable {
                                        viewModel.onSportChange(sport)
                                        hapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)
                                        showDialog = false // Zamknięcie popupu po wybraniu opcji
                                    }

                                    .padding(12.dp),
                                style = TextStyle(
                                    fontSize = 16.sp,
                                    fontFamily = FontFamily(Font(R.font.proximanovalight)),
                                    color = Color.Black
                                )
                            )
                        }
                    }
                }
            }
        }
    }
}


@Composable
fun PriceButton(modifier: Modifier) {
    var showDialog by remember { mutableStateOf(false) }
    val viewModel: CreateEventViewModel = CreateEventViewModelProvider.createEventViewModel
    var inputPrice by remember { mutableStateOf("") }
    val price by viewModel.price.observeAsState("")
    val hapticFeedback = LocalHapticFeedback.current
    val focusRequester = remember { FocusRequester() }
    val keyboardController = LocalSoftwareKeyboardController.current

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .background(
                Color.White,
                shape = RoundedCornerShape(16.dp)
            )
            .clickable {
                hapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)
                showDialog = true
            }
            .padding(16.dp)
            .heightIn(min = 28.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = if (price.isEmpty()) "Cena" else "$price zł",
            style = TextStyle(
                fontSize = 16.sp,
                fontFamily = if (price.isEmpty()) FontFamily(Font(R.font.proximanovaregular)) else FontFamily(Font(R.font.proximanovabold)),
                fontWeight = if (price.isEmpty()) FontWeight.Medium else FontWeight.Bold,
                color = if (price.isEmpty()) Color.Gray else Color(0xFF003366),
                textAlign = TextAlign.Center
            ),
            modifier = Modifier.fillMaxWidth()
        )
    }

    // Dialog do wprowadzenia ceny
    if (showDialog) {
        Dialog(onDismissRequest = { showDialog = false }) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White, shape = RoundedCornerShape(16.dp))
                    .padding(16.dp)
            ) {
                Column {
                    Text(
                        text = "Podaj cenę (zł)",
                        style = TextStyle(
                            fontSize = 18.sp,
                            fontFamily = FontFamily(Font(R.font.proximanovabold)),
                            fontWeight = FontWeight.Bold
                        ),
                        modifier = Modifier
                            .padding(bottom = 16.dp)
                            .align(Alignment.CenterHorizontally)
                    )

                    TextField(
                        value = inputPrice,
                        onValueChange = { newValue ->
                            if (newValue.isEmpty() || newValue.matches(Regex("^\\d*(\\.\\d{0,2})?$"))) {
                                inputPrice = newValue
                            }
                        },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine = true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .focusRequester(focusRequester),
                        textStyle = TextStyle(
                            fontSize = 16.sp,
                            fontFamily = FontFamily(Font(R.font.proximanovaregular))
                        ),
                        colors = TextFieldDefaults.textFieldColors(
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent
                        ),
                        shape = RoundedCornerShape(8.dp)
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp)
                    ) {
                        Text(
                            text = "Anuluj",
                            fontFamily = FontFamily(Font(R.font.proximanovabold)),
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black,
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .clickable(
                                    interactionSource = remember { MutableInteractionSource() },
                                    indication = rememberRipple()
                                ) {
                                    hapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)
                                    showDialog = false
                                }
                                .padding(8.dp)
                        )
                        Divider(
                            color = Color.Black,
                            thickness = 6.dp,
                            modifier = Modifier
                                .height(20.dp)
                                .width(1.dp)
                        )
                        Text(
                            text = "Gotowe",
                            fontSize = 22.sp,
                            fontFamily = FontFamily(Font(R.font.proximanovabold)),
                            fontWeight = FontWeight.Bold,
                            color = Color.Black,
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .clickable(
                                    interactionSource = remember { MutableInteractionSource() },
                                    indication = rememberRipple()
                                ) {
                                    hapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)
                                    viewModel.onPriceChange(inputPrice)
                                    showDialog = false
                                }
                                .padding(8.dp)
                        )
                    }
                }
            }
        }
    }

    // Automatycznie ustawiaj focus na pole tekstowe i pokazuj klawiaturę
    if (showDialog) {
        LaunchedEffect(Unit) {
            delay(100)
            focusRequester.requestFocus()
            keyboardController?.show()
        }
    }
}

@Composable
fun SkillLevelButton(modifier: Modifier) {
    var showDialog by remember { mutableStateOf(false) }
    val viewModel: CreateEventViewModel = CreateEventViewModelProvider.createEventViewModel
    val skillLevel by viewModel.skillLevel.observeAsState("")
    val availableSkillLevels = viewModel.availableSkillLevels
    val hapticFeedback = LocalHapticFeedback.current

    // Przycisk pokazujący aktualny wybór
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .background(
                Color.White,
                shape = RoundedCornerShape(16.dp)
            )
            .clickable {
                hapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)
                showDialog = true
            }
            .padding(16.dp)
            .heightIn(min = 28.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = if (skillLevel.isEmpty()) "Poziom umiejętności" else skillLevel,
            style = TextStyle(
                fontSize = 16.sp,
                fontFamily = if (skillLevel.isEmpty()) FontFamily(Font(R.font.proximanovaregular)) else FontFamily(Font(R.font.proximanovabold)),
                fontWeight = if (skillLevel.isEmpty()) FontWeight.Medium else FontWeight.Bold,
                color = if (skillLevel.isEmpty()) Color.Gray else Color(0xFF003366),
                textAlign = TextAlign.Center
            ),
            modifier = Modifier.fillMaxWidth()
        )
    }

    // Dialog z listą poziomów umiejętności
    if (showDialog) {
        Dialog(onDismissRequest = { showDialog = false }) {
            Surface(
                shape = RoundedCornerShape(16.dp),
                color = Color.White,
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "Wybierz poziom umiejętności",
                        style = TextStyle(
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily(Font(R.font.proximanovabold)),
                            color = Color.Black
                        ),
                        modifier = Modifier
                            .padding(bottom = 16.dp)
                            .align(Alignment.CenterHorizontally)
                    )

                    // Lista poziomów umiejętności
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(max = 300.dp)
                    ) {
                        items(availableSkillLevels) { level ->
                            Text(
                                text = level,
                                modifier = Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .fillMaxWidth()
                                    .clickable {
                                        viewModel.onSkillLevelChange(level)
                                        hapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)
                                        showDialog = false
                                    }
                                    .padding(12.dp),
                                style = TextStyle(
                                    fontSize = 16.sp,
                                    fontFamily = FontFamily(Font(R.font.proximanovalight)),
                                    color = Color.Black
                                )
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun EventNameButton(modifier: Modifier) {
    var showDialog by remember { mutableStateOf(false) }
    var inputName by remember { mutableStateOf("") }
    val viewModel: CreateEventViewModel = CreateEventViewModelProvider.createEventViewModel
    val eventName by viewModel.eventName.observeAsState("")
    val hapticFeedback = LocalHapticFeedback.current
    val focusRequester = remember { FocusRequester() }
    val keyboardController = LocalSoftwareKeyboardController.current

    // Przycisk otwierający dialog
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .background(
                Color.White,
                shape = RoundedCornerShape(16.dp)
            )
            .clickable {
                hapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)
                showDialog = true
            }
            .padding(16.dp)
            .heightIn(min = 28.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = if (eventName.isEmpty()) "Nazwa wydarzenia" else eventName,
            style = TextStyle(
                fontSize = 16.sp,
                fontFamily = if (eventName.isEmpty()) FontFamily(Font(R.font.proximanovaregular)) else FontFamily(Font(R.font.proximanovabold)),
                fontWeight = if (eventName.isEmpty()) FontWeight.Medium else FontWeight.Bold,
                color = if (eventName.isEmpty()) Color.Gray else Color(0xFF003366),
                textAlign = TextAlign.Center
            ),
            modifier = Modifier.fillMaxWidth()
        )
    }

    // Dialog do wprowadzenia nazwy wydarzenia
    if (showDialog) {
        Dialog(onDismissRequest = { showDialog = false }) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White, shape = RoundedCornerShape(16.dp))
                    .padding(16.dp)
            ) {
                Column {
                    Text(
                        text = "Podaj nazwę wydarzenia",
                        style = TextStyle(
                            fontSize = 18.sp,
                            fontFamily = FontFamily(Font(R.font.proximanovabold)),
                            fontWeight = FontWeight.Bold
                        ),
                        modifier = Modifier
                            .padding(bottom = 16.dp)
                            .align(Alignment.CenterHorizontally)
                    )

                    TextField(
                        value = inputName,
                        onValueChange = { inputName = it },
                        singleLine = true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .focusRequester(focusRequester),
                        textStyle = TextStyle(
                            fontSize = 16.sp,
                            fontFamily = FontFamily(Font(R.font.proximanovaregular))
                        ),
                        colors = TextFieldDefaults.textFieldColors(
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent
                        ),
                        shape = RoundedCornerShape(8.dp)
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp)
                    ) {
                        Text(
                            text = "Anuluj",
                            fontFamily = FontFamily(Font(R.font.proximanovabold)),
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black,
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .clickable(
                                    interactionSource = remember { MutableInteractionSource() },
                                    indication = rememberRipple()
                                ) {
                                    hapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)
                                    showDialog = false
                                }
                                .padding(8.dp)
                        )
                        Divider(
                            color = Color.Black,
                            thickness = 6.dp,
                            modifier = Modifier
                                .height(20.dp)
                                .width(1.dp)
                        )
                        Text(
                            text = "Gotowe",
                            fontSize = 22.sp,
                            fontFamily = FontFamily(Font(R.font.proximanovabold)),
                            fontWeight = FontWeight.Bold,
                            color = Color.Black,
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .clickable(
                                    interactionSource = remember { MutableInteractionSource() },
                                    indication = rememberRipple()
                                ) {
                                    hapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)
                                    viewModel.onEventNameChange(inputName)
                                    showDialog = false
                                }
                                .padding(8.dp)
                        )
                    }
                }
            }
        }
    }

    // Automatycznie ustawiaj focus na pole tekstowe i pokazuj klawiaturę
    if (showDialog) {
        LaunchedEffect(Unit) {
            delay(100)
            focusRequester.requestFocus()
            keyboardController?.show()
        }
    }
}



@Composable
fun ParticipantsPopupButton(
    modifier: Modifier,
    onParticipantsSelected: () -> Unit = {}
) {
    var selectedPeople by remember { mutableStateOf<Int?>(null) }
    var showDialog by remember { mutableStateOf(false) }
    val viewModel: CreateEventViewModel = CreateEventViewModelProvider.createEventViewModel
    val limit by viewModel.limit.observeAsState("")
    val hapticFeedback = LocalHapticFeedback.current

    // Przycisk, który otwiera popup
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .background(
                Color.White,
                shape = RoundedCornerShape(16.dp)
            )
            .clickable {
                hapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)
                showDialog = true
            } // Kliknięcie otwiera popup
            .padding(16.dp)
            .heightIn(min = 28.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = if (limit.isEmpty()) "Liczba uczestników" else limit,
                modifier = Modifier
                    .weight(1f)
                    .align(Alignment.CenterVertically),
                style = TextStyle(
                    fontSize = 16.sp,
                    fontFamily = if(limit.isEmpty()) FontFamily(Font(R.font.proximanovaregular)) else FontFamily(Font(R.font.proximanovabold)),
                    fontWeight = if(limit.isEmpty()) FontWeight.Medium else FontWeight.Bold,
                    color = if(limit.isEmpty()) Color.Gray else Color(0xFF003366),
                    textAlign = TextAlign.Center
                )
            )
        }
    }

    // Popup dialog z przewijalną listą uczestników
    if (showDialog) {
        Dialog(onDismissRequest = { showDialog = false }) {
            Surface(
                shape = RoundedCornerShape(16.dp),
                color = Color.White,
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "Wybierz liczbę uczestników",
                        style = TextStyle(
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black
                        ),
                        modifier = Modifier
                            .padding(bottom = 16.dp)
                            .align(Alignment.CenterHorizontally)
                    )

                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(max = 300.dp) // Ograniczenie wysokości popupu
                    ) {
                        items((2..22).toList()) { peopleCount ->
                            Text(
                                text = peopleCount.toString(),
                                modifier = Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .fillMaxWidth()
                                    .clickable {
                                        selectedPeople = peopleCount
                                        viewModel.onLimitChange(peopleCount.toString())
                                        showDialog = false // Zamknięcie popupu po wyborze liczby
                                        hapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)
                                        onParticipantsSelected()
                                    }
                                    .padding(12.dp),
                                fontSize = 18.sp,
                                fontFamily = FontFamily(Font(R.font.proximanovalight)),
                                color = Color.Black,
                                textAlign = TextAlign.Start
                            )
                        }
                    }
                }
            }
        }
    }
}

