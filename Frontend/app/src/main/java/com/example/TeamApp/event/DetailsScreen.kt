package com.example.TeamApp.event
import DescriptionTextField
import UserViewModel
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import android.view.View
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentContainerView
import androidx.fragment.app.commit
import androidx.navigation.NavController
import com.example.TeamApp.MainAppActivity
import com.example.TeamApp.R
import com.example.TeamApp.data.Coordinates
import com.example.TeamApp.excludedUI.EventButton
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.tomtom.sdk.location.GeoPoint
import com.tomtom.sdk.map.display.MapOptions
import com.tomtom.sdk.map.display.TomTomMap
import com.tomtom.sdk.map.display.camera.CameraOptions
import com.tomtom.sdk.map.display.camera.CameraPosition
import com.tomtom.sdk.map.display.common.screen.Padding
import com.tomtom.sdk.map.display.map.OnlineCachePolicy
import com.tomtom.sdk.map.display.style.StyleMode
import com.tomtom.sdk.map.display.ui.MapFragment
import com.tomtom.sdk.map.display.ui.MapView
import java.util.Properties
import com.example.TeamApp.event.createTomTomMapFragment
import com.google.firebase.firestore.FirebaseFirestore
import com.tomtom.sdk.map.display.image.ImageFactory
import com.tomtom.sdk.map.display.marker.MarkerOptions


@Composable
fun DetailsScreen(navController: NavController, activityId: String, userViewModel: UserViewModel) {
    val user by userViewModel.user.observeAsState()
    val viewModel: CreateEventViewModel = ViewModelProvider.createEventViewModel
    val event = viewModel.getEventById(activityId)
    var geoPoint by remember { mutableStateOf<GeoPoint?>(null) }
    val locationQuery = event?.location ?: ""
    val locationID = event?.locationID
    val cachedMapFragment = viewModel.mapFragment


    val gradientColors = listOf( 
        Color(0xFFE8E8E8),
        Color(0xFF007BFF)
    )
    Log.d("DetailsScreen", "Event: ${user?.userID}")
    var isJoined by remember { mutableStateOf(user?.let { event?.participants?.contains(it.userID) }) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color(0xFFF2F2F2))
            .padding(horizontal = 16.dp, vertical = 36.dp),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxSize()
                .background(color = Color(0xFFF2F2F2), shape = RoundedCornerShape(size = 16.dp))
        ) {
            Column(
                modifier = Modifier
                    .padding(8.dp)
                    .navigationBarsPadding()
            ) {
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.padding(start = 5.dp, end = 5.dp, top = 5.dp, bottom = 5.dp).fillMaxWidth()
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
                        text = "Szczegóły wydarzenia",
                        style = TextStyle(
                            fontSize = 24.sp,
                            fontFamily = FontFamily(Font(R.font.proximanovabold)),
                            fontWeight = FontWeight(900),
                            color = Color(0xFF003366),
                            textAlign = TextAlign.End,
                        )
                    )

                }

                // Event details
                if (event != null) {
                    DescriptionTextField(
                        label = "Opis wydarzenia",
                        isEditable = false,
                        text = event.description
                    )
                }

                // Location label
                Text(
                    text = "Lokalizacja:",
                    style = TextStyle(
                        fontSize = 22.sp,
                        fontFamily = FontFamily(Font(R.font.robotobold)),
                        fontWeight = FontWeight(900),
                        color = Color(0xFF003366),
                        textAlign = TextAlign.Start,
                        lineHeight = 25.sp,
                    ),
                    modifier = Modifier.padding(bottom = 20.dp)
                )

                if (locationID != null) {
                    TomTomMapView(context = LocalContext.current, locationID = locationID, selectedAddress = locationQuery, cachedMapFragment = cachedMapFragment)
                }
                // Join status row
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 20.dp)
                ) {
                    Image(
                        painter = painterResource(id = if (isJoined == true) R.drawable.joinstatuson else R.drawable.joinstatusoff),
                        contentDescription = "Join status",
                        modifier = Modifier
                            .wrapContentWidth()
                            .height(24.dp)
                    )
                    Spacer(Modifier.width(8.dp))
                    Text(
                        text = if (isJoined==true) "dołączono" else "nie dołączono",
                        style = TextStyle(
                            fontSize = 14.sp,
                            fontFamily = FontFamily(Font(R.font.robotoblackitalic)),
                            fontWeight = FontWeight.ExtraBold,
                            color = Color.Black,
                        )
                    )
                }

                Column(
                    modifier = Modifier
                        .padding(horizontal = 24.dp)
                        .navigationBarsPadding()
                ) {
                    EventButton(
                        text = if (isJoined == true) "OPUŚĆ" else "DOŁĄCZ",
                        onClick = {
                            val db = FirebaseFirestore.getInstance()
                            val eventRef = db.collection("events").document(activityId)

                            if (!isJoined!!) {
                                if (event != null) {
                                    user?.let {
                                        event.participants.add(it.userID)
                                        eventRef.update("participants", event.participants)
                                            .addOnSuccessListener { Log.d("Firebase", "User added to participants") }
                                    }
                                }
                            } else {
                                if (event != null) {
                                    user?.let {
                                        event.participants.remove(it.userID)
                                        eventRef.update("participants", event.participants)
                                            .addOnSuccessListener { Log.d("Firebase", "User removed from participants") }
                                    }
                                }
                            }
                            isJoined = !isJoined!!
                        }
                    )

                    EventButton(
                        text = "CZAT",
                        onClick = {
                            navController.navigate("chat/$activityId") }
                    )
                }
            }
        }
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
@Composable
fun TomTomMapView(context: Context, locationID: Map<String, Coordinates>, selectedAddress: String, cachedMapFragment: MapFragment?) {
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

    if (isMapFragmentReady && mapFragment != null) {
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
                            val bitmap = BitmapFactory.decodeResource(context.resources, R.drawable.pin_icon)
                            val scaledBitmap = Bitmap.createScaledBitmap(bitmap, 100, 100, false) // Adjust size here
                            val markerOptions = MarkerOptions(
                                coordinate = GeoPoint(cords.latitude, cords.longitude),
                                pinImage = ImageFactory.fromBitmap(scaledBitmap),
                                pinIconImage = ImageFactory.fromBitmap(scaledBitmap),
                            )
                            tomTomMap.addMarker(markerOptions)

                            tomTomMap.addMarkerClickListener { }
                        }
                    }
                },
                modifier = Modifier
                    .clip(RoundedCornerShape(16.dp))
                    .fillMaxWidth()
                    .height(220.dp)
                    .border(2.dp, Color.Black, RoundedCornerShape(16.dp))
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






