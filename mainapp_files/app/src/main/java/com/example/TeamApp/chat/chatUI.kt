import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import com.example.TeamApp.R
import com.example.TeamApp.chat.Message
import com.example.TeamApp.chat.sendMessage
import com.google.firebase.firestore.FirebaseFirestore



import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavController

import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.google.android.gms.tasks.Tasks
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatScreen(navController: NavController, eventId: String, userViewModel: UserViewModel) {
    val user by userViewModel.user.observeAsState()
    val db = FirebaseFirestore.getInstance()
    val messages = remember { mutableStateListOf<Message>() }
    var messageText by remember { mutableStateOf("") }
    var activityName by remember { mutableStateOf<String?>(null) }
    val listState = rememberLazyListState()
    val hapticFeedback = LocalHapticFeedback.current
    var showParticipantsDialog by remember { mutableStateOf(false) }
    var participants by remember { mutableStateOf<List<String>>(emptyList()) }
    // Updated sportIcons map with drawable resource IDs
    val sportIcons: Map<String, Int> = mapOf(
        "Badminton" to R.drawable.figma_badminton_icon,
        "Bilard" to R.drawable.figma_pool_icon,
        "Bieganie" to R.drawable.figma_running_icon,
        "Boks" to R.drawable.figma_boxing_icon,
        "Jazda na deskorolce" to R.drawable.figma_skate_icon,
        "Jazda na rolkach" to R.drawable.figma_rollerskates_icon,
        "Kajakarstwo" to R.drawable.figma_kayak_icon,
        "Kolarstwo" to R.drawable.figma_cycling_icon,
        "Koszykówka" to R.drawable.figma_basketball_icon,
        "Kręgle" to R.drawable.figma_bowling_icon,
        "Kalistenika" to R.drawable.figma_calistenics_icon,
        "Łyżwiarstwo" to R.drawable.figma_iceskate_icon,
        "Piłka nożna" to R.drawable.figma_soccer_icon,
        "Pingpong" to R.drawable.figma_pingpong_icon,
        "Pływanie" to R.drawable.figma_swimming_icon,
        "Rzutki" to R.drawable.figma_dart_icon,
        "Siatkówka" to R.drawable.figma_volleyball_icon,
        "Siłownia" to R.drawable.figma_gym_icon,
        "Szermierka" to R.drawable.figma_fencing_icon,
        "Tenis" to R.drawable.figma_tennis_icon,
        "Wędkarstwo" to R.drawable.figma_fishing_icon
    )

    // Fetch the event name when eventId is passed
    LaunchedEffect(eventId) {
        delay(500)
        getEventNameById(eventId) { name ->
            activityName = name
        }
        fetchParticipants(eventId) { participantList ->
            participants = participantList
        }
    }

    val iconResourceId =
        sportIcons[activityName] ?: R.drawable.chevron_down

    /*TODO: Dodac spinning wheel do animacji ładowania, ten sleep jest potrzebny do zaladowania animacji*/
    LaunchedEffect(eventId) {
        delay(500)
        val messagesRef = db.collection("events").document(eventId).collection("messages")
        messagesRef.orderBy("timestamp").addSnapshotListener { snapshot, error ->
            if (error != null) {
                Log.e("Chat", "Listen failed.", error)
                return@addSnapshotListener
            }

            if (snapshot != null && !snapshot.isEmpty) {
                val newMessages = snapshot.toObjects(Message::class.java)
                messages.clear()
                messages.addAll(newMessages)
            } else {
                Log.d("Chat", "No messages found.")
            }
        }
    }

    LaunchedEffect(messages.size) {
        if (messages.isNotEmpty()) {
            listState.animateScrollToItem(messages.size - 1)
        }
    }



        Column(modifier = Modifier.fillMaxSize()) {
            // Add a top bar with the activity name and sport icon
            TopAppBar(
                title = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth() // Make sure the Row takes full width
                    ) {
                        // Left Arrow with Padding
                        Image(
                            painter = painterResource(id = R.drawable.chevron_left_white),
                            contentDescription = "back",
                            modifier = Modifier
                                .size(40.dp)
                                // Add padding to match desired space
                                .clickable { /* Handle back click */ }
                        )

                        Spacer(modifier = Modifier.weight(1f)) // Pushes the text and icon to the center

                        // Centered Text and Icon
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center,
                            modifier = Modifier.weight(8f) // Adjust the weight to center it properly
                        ) {
                            Image(
                                painter = painterResource(id = iconResourceId),
                                contentDescription = activityName,
                                modifier = Modifier
                                    .size(40.dp)
                                    .border(
                                        width = 1.dp,
                                        color = Color.White
                                    ) // Adjust size of the icon
                            )
                            Spacer(modifier = Modifier.width(8.dp)) // Spacing between icon and text
                            Text(
                                text = activityName
                                    ?: "Loading...", // Display activity name or "Loading..."
                                color = Color.White,
                                fontFamily = FontFamily(Font(R.font.proximanovabold)),
                                fontSize = androidx.compose.ui.unit.TextUnit(
                                    28f,
                                    androidx.compose.ui.unit.TextUnitType.Sp
                                ) // Set the font size
                            )
                        }

                        Spacer(modifier = Modifier.weight(1f)) // Balances the layout

                        // Info Icon on the Right with Padding
                        Image(
                            painter = painterResource(id = R.drawable.info),
                            contentDescription = "info",
                            modifier = Modifier
                                .size(40.dp)
                                .padding(end = 4.dp) // Add padding around the info icon to match the arrow
                                .clickable { showParticipantsDialog = true }
                        )
                    }
                },
                modifier = Modifier
                    .fillMaxWidth(),

                colors = TopAppBarDefaults.smallTopAppBarColors(containerColor = Color(0xFF007BFF)) // Set the top bar color
            )





            LazyColumn(
                state = listState,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                contentPadding = PaddingValues(8.dp)
            ) {
                var previousMessageTimestamp: Long? = null

                items(messages) { message ->
                    user?.let {
                        MessageScreen(
                            message = message,
                            currentUserId = it.userID,
                            previousMessageTimestamp = previousMessageTimestamp
                        )
                    }
                    // Update previous message timestamp
                    previousMessageTimestamp = message.timestamp?.toDate()?.time
                }
            }

            Row(
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Use Box to make the TextField grow based on its content
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                         // Add border if needed
                         // Make corners rounded
                ) {
                    OutlinedTextField(
                        value = messageText,
                        onValueChange = { messageText = it },
                        placeholder = { Text("Napisz wiadomość...") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(min = 48.dp) // Set minimum height
                            .padding(
                                horizontal = 4.dp,
                                vertical = 12.dp
                            ), // Adjust padding inside the text field
                        singleLine = false, // Allow multiple lines
                        maxLines = 5, // Limit to 5 lines max
                        colors = TextFieldDefaults.outlinedTextFieldColors(
                            containerColor = Color.Transparent,
                            focusedBorderColor = Color.Black,
                            unfocusedBorderColor = Color.Black
                        ),
                        shape = RoundedCornerShape(16.dp)
                    )
                }


                IconButton(
                    onClick = {
                        // Perform haptic feedback
                        hapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)

                        if (messageText.isNotBlank()) {
                            user?.let { sendMessage(eventId, it.userID, messageText) }
                            messageText = "" // Clear text field after sending message
                        }
                    },
                    modifier = Modifier
                        .padding(start = 8.dp)
                        .size(48.dp)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.sendmessage),
                        contentDescription = "send message",
                        modifier = Modifier.size(40.dp) // Adjust icon size
                    )
                }
            }
        }

            if (showParticipantsDialog) {
            AlertDialog(
                onDismissRequest = { showParticipantsDialog = false },
                title = { Text("Participants") },
                text = {
                    Column {
                        participants.forEach { participant ->
                            Text(text = participant)
                        }
                    }
                },
                confirmButton = {
                    Button(onClick = { showParticipantsDialog = false }) {
                        Text("Close")
                    }
                }
            )
        }
    }




fun getEventNameById(eventId: String, onResult: (String?) -> Unit) {
    val db = FirebaseFirestore.getInstance()
    val eventRef = db.collection("events").document(eventId)

    eventRef.get()
        .addOnSuccessListener { document ->
            if (document != null && document.exists()) {
                val activityName = document.getString("activityName")
                onResult(activityName) // Return the activity name
            } else {
                onResult(null) // Document does not exist
            }
        }
        .addOnFailureListener { exception ->
            onResult(null) // Handle failure
        }
}
fun fetchParticipants(eventId: String, onResult: (List<String>) -> Unit) {
    val db = FirebaseFirestore.getInstance()
    val participantsRef = db.collection("events").document(eventId).collection("participants")

    participantsRef.get()
        .addOnSuccessListener { result ->
            if (result.isEmpty) {
                Log.d("Firestore", "No participants found")
                onResult(emptyList())
                return@addOnSuccessListener
            }

            // Extract user IDs
            val userIds = result.documents.mapNotNull { it.getString("userID") }

            // If no user IDs are found, return empty list
            if (userIds.isEmpty()) {
                Log.d("Firestore", "No user IDs found")
                onResult(emptyList())
                return@addOnSuccessListener
            }

            // Fetch user names based on user IDs
            val userNames = mutableListOf<String>()
            val userCollection = db.collection("users")
            val nameFetchTasks = userIds.map { userId ->
                userCollection.document(userId).get()
                    .addOnSuccessListener { userDocument ->
                        val userName = userDocument.getString("name")
                        if (userName != null) {
                            userNames.add(userName)
                        }
                    }
                    .addOnFailureListener { exception ->
                        Log.e("Firestore", "Error fetching user data for ID: $userId", exception)
                    }
            }

            // Wait for all name fetch tasks to complete
            Tasks.whenAllComplete(nameFetchTasks).addOnCompleteListener {
                if (it.isSuccessful) {
                    onResult(userNames)
                } else {
                    Log.e("Firestore", "Error fetching user names", it.exception)
                    onResult(emptyList())
                }
            }
        }
        .addOnFailureListener { exception ->
            Log.e("Firestore", "Error fetching participants", exception)
            onResult(emptyList())
        }
}

//@Preview(showBackground = true)
//@Composable
//fun PreviewChatScreen() {
//    // Replace `eventId` and `currentUserId` with test values
//    val testEventId = "YUCN1qQeFfEcJ4LXHVuL"
//    val testUserId = "huj"
//
//
//    // Call the `ChatScreen` function in `Preview`
//    ChatScreen(eventId = testEventId, user.userI = testUserId)
//}
@Composable
fun TransparentStatusBar() {
    val systemUiController = rememberSystemUiController()

    // Make the status bar transparent
    systemUiController.setSystemBarsColor(
        color = Color.Transparent, // Set status bar color to transparent
        darkIcons = false // Adjust icons for light or dark status bar (true for dark icons on light background)
    )
}
