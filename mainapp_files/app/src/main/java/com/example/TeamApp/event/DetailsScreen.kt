package com.example.TeamApp.event
// ... (imports remain largely the same, ensure DescriptionTextField is imported if in another file)
import DescriptionTextField // Assuming this is in the same package or correctly imported
import UserViewModel
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Paint
import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
// import androidx.compose.foundation.layout.wrapContentWidth // Not strictly needed for the status image
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons // For Material Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.EuroSymbol
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person // For participants
import androidx.compose.material.icons.filled.Star // For skill level
import androidx.compose.material3.Divider // For separating sections
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme // For typography
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.fragment.app.FragmentActivity
import androidx.navigation.NavController
import com.example.TeamApp.R
import com.example.TeamApp.data.Coordinates
import com.example.TeamApp.data.Event
import com.example.TeamApp.excludedUI.EventButton
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.tomtom.sdk.location.GeoPoint
import com.tomtom.sdk.map.display.MapOptions
import com.tomtom.sdk.map.display.camera.CameraOptions
import com.tomtom.sdk.map.display.image.ImageFactory
import com.tomtom.sdk.map.display.marker.MarkerOptions
import com.tomtom.sdk.map.display.ui.MapFragment
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun DetailsScreen(navController: NavController, activityId: String, userViewModel: UserViewModel) {
    val user by userViewModel.user.observeAsState()
    val viewModel: CreateEventViewModel = CreateEventViewModelProvider.createEventViewModel
    val event by remember(activityId) { mutableStateOf(viewModel.getEventById(activityId)) } // Ensure event updates if activityId changes

    val locationQuery = event?.location ?: ""
    val locationID = event?.locationID
    val cachedMapFragment = viewModel.mapFragment
    val context: Context = LocalContext.current

    val configuration = LocalConfiguration.current
    val screenHeightDp = configuration.screenHeightDp.dp
    val currentEventDetails = event
    val iconTintColor = Color(0xff4fc3f7)


    val eventInitialDetails by remember(activityId) {
        mutableStateOf(viewModel.getEventById(activityId))
    }

    // Observe the current participants count from the ViewModel
    val currentParticipantsCount by viewModel.eventDetailsCurrentParticipants.observeAsState()

    // Initialize the ViewModel's participant count when the screen loads or event changes
    LaunchedEffect(eventInitialDetails) {
        viewModel.setSelectedEventForDetails(eventInitialDetails)
    }


    LaunchedEffect(user, event) { // Observe user and event for isJoined state
        Log.d("DetailsScreen", "User: $user, Event: $event")
    }
    var isJoined by remember(user, eventInitialDetails, currentParticipantsCount) {
        // Recalculate isJoined based on initial participants list if needed,
        // though primarily it's for button state.
        // The actual participant list for checking might need to come from eventInitialDetails.participants
        mutableStateOf(user?.let { currentUser -> eventInitialDetails?.participants?.contains(currentUser.userID) } ?: false)
    }


    ConstraintLayout(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color(0xFFF2F2F2))
    ) {
        val (topAppBar, map, lazyColumn) = createRefs()

        val mapScale = 0.338f // Percentage of screen height for the map visible area
        val mapHeight = Dimension.value(screenHeightDp * mapScale)
        // LazyColumn starts slightly before map bottom to create overlap, adjust 0.02f as needed
        val lazyColumnTopMarginFromMapTop = screenHeightDp * (mapScale - 0.04f)


        TopAppBar(
            modifier = Modifier.constrainAs(topAppBar)
            {
                top.linkTo(parent.top);
                start.linkTo(parent.start)
                end.linkTo(parent.end)
                height = Dimension.value(screenHeightDp * 0.11f)
            },
            title = {
                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .padding(horizontal = 8.dp),
                    contentAlignment = Alignment.CenterStart
                ) {
                    Text(
                        text = "Szczegóły wydarzenia",
                        style = TextStyle(
                            fontSize = 24.sp,
                            fontFamily = FontFamily(Font(R.font.proximanovabold)),
                            fontWeight = FontWeight.W900,
                            color = Color(0xFF003366),
                            textAlign = TextAlign.End,
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            navigationIcon = {
                Box(
                    modifier = Modifier.fillMaxHeight(),
                    contentAlignment = Alignment.Center
                ) {
                    IconButton(
                        onClick = { navController.popBackStack() }

                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.arrowleft),
                            contentDescription = "Back Icon",

                            //tint = Color(0xFF003366)
                        )
                    }
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = Color.White
            )
        )
        if (locationID != null && currentEventDetails != null) {
            Box(modifier = Modifier.constrainAs(map) {
                top.linkTo(topAppBar.bottom)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
                width = Dimension.fillToConstraints
                height = mapHeight
            }) {
                TomTomMapView(
                    context = context,
                    locationID = locationID,
                    selectedAddress = locationQuery,
                    cachedMapFragment = cachedMapFragment,
                    event = currentEventDetails, // Pass the stable currentEventDetails
                )
            }
        } else {
            Box(modifier = Modifier.constrainAs(map) { /* Placeholder */ })
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White, RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp))
                .constrainAs(lazyColumn) {
                    top.linkTo(map.top, margin = lazyColumnTopMarginFromMapTop)
                    bottom.linkTo(parent.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    height = Dimension.fillToConstraints
                    width = Dimension.fillToConstraints
                }
        ) {
            // Reduced spacer height
            item { Spacer(modifier = Modifier.height(15.dp)) }

            currentEventDetails?.let { eventData ->
                item {
                    val titleToDisplay = if (!eventData.eventName.isNullOrBlank()) {
                        eventData.eventName
                    } else {
                        eventData.activityName
                    }
                    Text(
                        text = titleToDisplay,
                        style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp) // Reduced vertical padding
                    )
                    // Divider removed
                }

                item {
                    Column { // Removed extra padding from this Column
                        Text(
                            text = "Opis wydarzenia",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                            modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 12.dp) // Adjusted padding
                        )
                        DescriptionTextField(
                            label = "",
                            isEditable = false,
                            text = eventData.description,
                            modifier = Modifier.padding(top = 0.dp) // Ensure no extra top padding from here
                        )
                    }
                }

                item {
                    Column(modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 16.dp)) {
                        Text(
                            text = "Szczegóły",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                        DetailItem(icon = Icons.Filled.LocationOn, label = "Lokalizacja:", value = eventData.location, iconColor = iconTintColor)
                        val formattedDate = try {
                            val formatterFrom = DateTimeFormatter.ofPattern("d MMMM yyyy, HH:mm", Locale("pl"))
                            val formatterTo = DateTimeFormatter.ofPattern("EEEE, d MMMM yyyy HH:mm", Locale("pl"))
                            LocalDateTime.parse(eventData.date, formatterFrom).format(formatterTo)
                        } catch (e: Exception) {
                            eventData.date
                        }
                        DetailItem(icon = Icons.Filled.CalendarToday, label = "Data:", value = formattedDate, iconColor = iconTintColor)
                        DetailItem(icon = Icons.Filled.EuroSymbol, label = "Cena:", value = (eventData.price ?: "Nie podano") + if (!eventData.price.isNullOrBlank() && eventData.price != "Nie podano" && !eventData.price.endsWith("zł")) " zł" else "", iconColor = iconTintColor)
                        DetailItem(icon = Icons.Filled.Star, label = "Poziom:", value = eventData.skillLevel ?: "Nie podano", iconColor = iconTintColor)
                        DetailItem(
                            icon = Icons.Filled.Person,
                            label = "Uczestnicy:",
                            value = "${currentParticipantsCount ?: eventData.currentParticipants} / ${eventData.maxParticipants}",
                            iconColor = iconTintColor
                        )
                    }
                    // Divider removed
                }

                item {
                    Spacer(modifier = Modifier.height(16.dp))
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 8.dp)
                    ) {
                        Icon(
                            painter = painterResource(id = if (isJoined) R.drawable.joinstatuson else R.drawable.joinstatusoff),
                            contentDescription = "Join status",
                            modifier = Modifier.size(32.dp),
                            tint = if (isJoined) iconTintColor else Color.Gray // Using iconTintColor
                        )
                        Spacer(Modifier.width(8.dp))
                        Text(
                            text = if (isJoined) "Dołączono" else "Nie dołączono",
                            style = TextStyle(
                                fontSize = 20.sp,
                                fontFamily = FontFamily(Font(R.font.robotoblackitalic)),
                                fontWeight = FontWeight.ExtraBold,
                                color = Color(0xFF003366),
                            )
                        )
                    }
                }

                item {
                    Column(
                        modifier = Modifier
                            .padding(horizontal = 24.dp, vertical = 16.dp)
                            .navigationBarsPadding()
                    ) {
                        EventButton(
                            text = if (isJoined) "OPUŚĆ" else "DOŁĄCZ",
                            onClick = {
                                val db = FirebaseFirestore.getInstance()
                                val eventRef = db.collection("events").document(activityId)
                                val userRef = user?.let { db.collection("users").document(it.userID) }
                                val currentIsJoined = isJoined

                                if (!currentIsJoined) {
                                    if (user != null) {
                                        eventData.participants.add(user!!.userID) // Use eventData
                                        eventRef.update("participants", eventData.participants)
                                        eventRef.update("currentParticipants", eventData.participants.size)
                                            .addOnSuccessListener {
                                                viewModel.updateLocalParticipantCount(eventData.participants.size) // Update ViewModel LiveData
                                            }
                                        userRef?.update("attendedEvents", FieldValue.arrayUnion(eventRef.id))
                                    }
                                } else {
                                    if (user != null) {
                                        eventData.participants.remove(user!!.userID) // Use eventData
                                        eventRef.update("participants", eventData.participants)
                                        eventRef.update("currentParticipants", eventData.participants.size)
                                            .addOnSuccessListener {
                                                viewModel.updateLocalParticipantCount(eventData.participants.size) // Update ViewModel LiveData
                                            }
                                        userRef?.update("attendedEvents", FieldValue.arrayRemove(eventRef.id))
                                    }
                                }
                                isJoined = !currentIsJoined
                            }
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        EventButton(
                            text = "CZAT",
                            onClick = { /* ... */ }
                        )
                    }
                }
            } ?: item {
                Box(modifier = Modifier.fillMaxSize().padding(16.dp), contentAlignment = Alignment.Center) {
                    Text("Nie można załadować szczegółów wydarzenia.")
                }
            }
        }
    }
}

@Composable
fun DetailItem(icon: ImageVector, label: String, value: String, iconColor: Color) { // Added iconColor parameter
    Row(
        verticalAlignment = Alignment.Top, // Changed to Top for better alignment with multi-line text
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = iconColor, // Use passed iconColor
            modifier = Modifier.size(20.dp).padding(top = 2.dp) // Add slight top padding to align with text baseline
        )
        Spacer(modifier = Modifier.width(12.dp))
        Text(
            text = "$label ",
            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
            color = Color.Gray
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            color = Color.Black,
            // Removed maxLines and TextOverflow to allow full text wrapping for location
            modifier = Modifier.weight(1f) // Allow text to take remaining space and wrap
        )
    }
}


        //@Composable
//fun TomTomMapView(
//    context: Context,
//    coordinates: GeoPoint,
//    modifier: Modifier = Modifier
//) {
//    val mapOptions = MapOptions(mapKey = getApiKey(context))
//    val mapFragment = MapFragment.newInstance(mapOptions)
//
//}
        fun createBitmapWithAntialiasing(
            context: Context,
            drawableId: Int,
            width: Int,
            height: Int
        ): Bitmap? {
            val originalBitmap = BitmapFactory.decodeResource(context.resources, drawableId)

            if (originalBitmap == null) {
                Log.e("BitmapError", "Failed to decode resource with ID: $drawableId")
                return null
            }

            val scaledBitmap = Bitmap.createScaledBitmap(originalBitmap, width, height, true)

            val antialiasedBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
            val canvas = Canvas(antialiasedBitmap)

            val paint = Paint(Paint.ANTI_ALIAS_FLAG) // Antialiasing enabled
            canvas.drawBitmap(scaledBitmap, 0f, 0f, paint)

            return antialiasedBitmap
        }

        @Composable
        fun TomTomMapView(
            context: Context,
            locationID: Map<String, Coordinates>,
            selectedAddress: String,
            cachedMapFragment: MapFragment?,
            event: Event
        ) {

            var mapFragment by remember { mutableStateOf(cachedMapFragment) }
            var isMapFragmentReady by remember { mutableStateOf(cachedMapFragment != null) }
            val fragmentManager = (context as FragmentActivity).supportFragmentManager

            val cords = locationID[selectedAddress]

            if (!isMapFragmentReady) {
                val mapOptions = MapOptions(
                    mapKey = getApiKey(context),
                )
                Log.d("MapFragment", "Creating MapFragment")
                LaunchedEffect(Unit) {
                    createTomTomMapFragment(fragmentManager, mapOptions) { fragment ->
                        mapFragment = fragment
                        isMapFragmentReady = true
                    }
                }
            }

            if (isMapFragmentReady && mapFragment?.view != null) {
                val fragmentView = mapFragment?.view
                if (fragmentView != null) {
                    // Dopiero jeśli fragment ma widok, inicjalizuj AndroidView
                    AndroidView(
                        factory = { fragmentView },
                        update = {
                            mapFragment?.getMapAsync { tomTomMap ->
                                if (cords != null) {
                                    tomTomMap.moveCamera(
                                        CameraOptions(
                                            position = GeoPoint(cords.latitude, cords.longitude),
                                            zoom = 15.0
                                        )
                                    )
                                    val iconName = event.pinIconResId // np. "balonobraz"
                                    val resourceId = context.resources.getIdentifier(
                                        iconName,
                                        "drawable",
                                        context.packageName
                                    )
                                    val antialiasedBitmap =
                                        createBitmapWithAntialiasing(context, resourceId, 90, 120)
                                    if (antialiasedBitmap != null)
                                    {
                                        val markerOptions = MarkerOptions(
                                            coordinate = GeoPoint(cords.latitude, cords.longitude),
                                            pinImage = ImageFactory.fromBitmap(antialiasedBitmap),
                                            pinIconImage = ImageFactory.fromBitmap(antialiasedBitmap),
                                        )
                                        tomTomMap.addMarker(markerOptions)

                                        tomTomMap.addMarkerClickListener { }
                                    }
                                 else {
                                    Log.e("MapMarker", "Failed to create bitmap for marker.")
                                }

                                }
                            }
                        },
                    )
                }
            }
        }


//@Composable
//fun ShowWarsawMap() {
//    val context = LocalContext.current
//    val warsawCoordinates = GeoPoint(52.2297, 21.0122) // Coordinates for Warsaw
//
//    TomTomMapView(context = context, coordinates = warsawCoordinates)
//}






