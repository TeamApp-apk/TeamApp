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

    LaunchedEffect(user, event) { // Observe user and event for isJoined state
        Log.d("DetailsScreen", "User: $user, Event: $event")
    }
    var isJoined by remember {
        mutableStateOf(user?.let { currentUser -> event?.participants?.contains(currentUser.userID) } ?: false)
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
        if (locationID != null && event != null) { // Ensure event is not null for TomTomMapView
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
                    event = event!!, // Safe due to outer null check
                )
            }
        } else {
            // Placeholder or empty box if map can't be shown
            Box(modifier = Modifier.constrainAs(map) {
                top.linkTo(topAppBar.bottom)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
                width = Dimension.fillToConstraints
                height = mapHeight
            })
        }


        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White, RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp))
                .constrainAs(lazyColumn) {
                    // Link top of LazyColumn to map.top with a margin to achieve the overlap effect
                    top.linkTo(map.top, margin = lazyColumnTopMarginFromMapTop)
                    bottom.linkTo(parent.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    height = Dimension.fillToConstraints // Make LazyColumn scrollable within its constraints
                    width = Dimension.fillToConstraints
                }
        ) {
            // Spacer to push content below the rounded corners / map overlap
            item { Spacer(modifier = Modifier.height(30.dp)) } // Adjust this height for visual spacing

            event?.let { currentEvent -> // Use let for safe access to currentEvent
                item {
                    val titleToDisplay = if (!currentEvent.eventName.isNullOrBlank()) {
                        currentEvent.eventName
                    } else {
                        currentEvent.activityName // Fallback to activityName
                    }

                    Text(
                        text = titleToDisplay,
                        style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                    )
                    Divider(modifier = Modifier.padding(horizontal = 16.dp))
                }

                item {
                    Column() {
                        Text(
                            text = "Opis wydarzenia",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                            modifier = Modifier.padding(horizontal = 16.dp)
                                .padding(top = 16.dp)
                        )
                        DescriptionTextField(
                            label = "", // Label isn't really needed here as we have the title above
                            isEditable = false,
                            text = currentEvent.description
                        )
                    }
                }

                item {
                    Column(modifier = Modifier.padding(start = 16.dp, end = 16.dp, bottom = 16.dp)) {
                        Text(
                            text = "Szczegóły",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                        DetailItem(icon = Icons.Filled.LocationOn, label = "Lokalizacja:", value = currentEvent.location)
                        // Date Formatting
                        val formattedDate = try {
                            val formatterFrom = DateTimeFormatter.ofPattern("d MMMM yyyy, HH:mm", Locale("pl"))
                            val formatterTo = DateTimeFormatter.ofPattern("EEEE, d MMMM yyyy HH:mm", Locale("pl"))
                            LocalDateTime.parse(currentEvent.date, formatterFrom).format(formatterTo)
                        } catch (e: Exception) {
                            currentEvent.date // Fallback to original string if parsing fails
                        }
                        DetailItem(icon = Icons.Filled.CalendarToday, label = "Data:", value = formattedDate)
                        DetailItem(icon = Icons.Filled.EuroSymbol, label = "Cena:", value = currentEvent.price ?: "Nie podano")
                        DetailItem(icon = Icons.Filled.Star, label = "Poziom:", value = currentEvent.skillLevel ?: "Nie podano")
                        DetailItem(
                            icon = Icons.Filled.Person,
                            label = "Uczestnicy:",
                            value = "${currentEvent.currentParticipants} / ${currentEvent.maxParticipants}"
                        )
                    }
                    Divider(modifier = Modifier.padding(horizontal = 16.dp))
                }


                item {
                    Spacer(modifier = Modifier.height(16.dp))
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 8.dp) // Increased bottom padding
                    ) {
                        Icon( // Changed Image to Icon for consistency if using Material Icons
                            painter = painterResource(id = if (isJoined) R.drawable.joinstatuson else R.drawable.joinstatusoff),
                            contentDescription = "Join status",
                            modifier = Modifier.size(32.dp), // Adjusted size
                            tint = if (isJoined) Color(0xFF4CAF50) else Color.Gray // Example tint
                        )
                        Spacer(Modifier.width(8.dp))
                        Text(
                            text = if (isJoined) "Dołączono" else "Nie dołączono",
                            style = TextStyle(
                                fontSize = 20.sp, // Slightly smaller for balance
                                fontFamily = FontFamily(Font(R.font.robotoblackitalic)), // Ensure this font is available
                                fontWeight = FontWeight.ExtraBold,
                                color = Color(0xFF003366),
                            )
                        )
                    }
                }

                item {
                    Column(
                        modifier = Modifier
                            .padding(horizontal = 24.dp, vertical = 16.dp) // Added vertical padding
                            .navigationBarsPadding() // Handles system navigation bar
                    ) {
                        EventButton(
                            text = if (isJoined) "OPUŚĆ" else "DOŁĄCZ",
                            onClick = { /* ... Your existing join/leave logic ... */
                                val db = FirebaseFirestore.getInstance()
                                val eventRef = db.collection("events").document(activityId)
                                val userRef = user?.let { db.collection("users").document(it.userID) }

                                val currentIsJoined = isJoined // Use a stable value for the transaction

                                if (!currentIsJoined) {
                                    if (user != null) {
                                        currentEvent.participants.add(user!!.userID)
                                        eventRef.update("participants", currentEvent.participants)
                                        eventRef.update("currentParticipants", currentEvent.participants.size)
                                        userRef?.update("attendedEvents", FieldValue.arrayUnion(eventRef.id))
                                    }
                                } else {
                                    if (user != null) {
                                        currentEvent.participants.remove(user!!.userID)
                                        eventRef.update("participants", currentEvent.participants)
                                        eventRef.update("currentParticipants", currentEvent.participants.size)
                                        userRef?.update("attendedEvents", FieldValue.arrayRemove(eventRef.id))
                                    }
                                }
                                isJoined = !currentIsJoined // Update UI state
                            }
                        )
                        Spacer(modifier = Modifier.height(10.dp)) // Space between buttons
                        EventButton(
                            text = "CZAT",
                            onClick = {
                                if (isJoined) {
                                    navController.navigate("chat/$activityId")
                                } else {
                                    Log.d("DetailsScreen", "User is not a participant, cannot access chat.")
                                    // TODO: Show a Toast or Snackbar to the user
                                }
                            }
                        )
                    }
                }
            } ?: item { // Fallback if event is null
                Box(modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp), contentAlignment = Alignment.Center) {
                    Text("Nie można załadować szczegółów wydarzenia.")
                }
            }
        }
    }
}

@Composable
fun DetailItem(icon: ImageVector, label: String, value: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = MaterialTheme.colorScheme.primary, // Use theme color
            modifier = Modifier.size(20.dp)
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
            overflow = TextOverflow.Ellipsis,
            maxLines = 2 // Allow value to wrap slightly if long
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






