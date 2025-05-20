import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.imePadding
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
import androidx.compose.material3.Surface
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
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
    val keyboardController = LocalSoftwareKeyboardController.current
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

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(Color(0xFFE8E8E8),
                        Color(0xFF74B5FC)
                    ),
                    startY = 0f,
                    endY = Float.POSITIVE_INFINITY
                )
            )

    ) {
        TopAppBar(
            title = {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.chevron_left_white),
                        contentDescription = "back",
                        modifier = Modifier
                            .size(40.dp)
                            .clickable { navController.popBackStack() } // Use navController.popBackStack() to go back
                    )

                    Spacer(modifier = Modifier.weight(1f))

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center,
                        modifier = Modifier.weight(8f)
                    ) {
                        Image(
                            painter = painterResource(id = iconResourceId),
                            contentDescription = activityName,
                            modifier = Modifier
                                .size(40.dp)
                                .border(
                                    width = 1.dp,
                                    color = Color.White
                                )
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = activityName ?: "Loading...",
                            color = Color.White,
                            fontFamily = FontFamily(Font(R.font.proximanovabold)),
                            fontSize = androidx.compose.ui.unit.TextUnit(
                                28f,
                                androidx.compose.ui.unit.TextUnitType.Sp
                            )
                        )
                    }

                    Spacer(modifier = Modifier.weight(1f))

                    Image(
                        painter = painterResource(id = R.drawable.info),
                        contentDescription = "info",
                        modifier = Modifier
                            .size(40.dp)
                            .padding(end = 4.dp)
                            .clickable { showParticipantsDialog = true }
                    )
                }
            },
            modifier = Modifier.fillMaxWidth(),
            colors = TopAppBarDefaults.smallTopAppBarColors(containerColor = Color(0xFF007BFF))
        )

        LazyColumn(
            state = listState,
            modifier = Modifier
                .weight(1f) // This makes LazyColumn take up all available space
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
                previousMessageTimestamp = message.timestamp?.toDate()?.time
            }
        }

        // This Row will stay at the bottom, pushed up by imePadding
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                modifier = Modifier
                    .weight(1f)
                    .shadow(4.dp, RoundedCornerShape(24.dp)),
                shape = RoundedCornerShape(24.dp),
                color = Color.White
            ) {
                OutlinedTextField(
                    value = messageText,
                    onValueChange = { messageText = it },
                    placeholder = { Text("Napisz wiadomość...", color = Color.Gray) },
                    modifier = Modifier
                        .fillMaxWidth()
                        ,
                    singleLine = false,
                    maxLines = 5,
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        containerColor = Color.Transparent,
                        focusedBorderColor = Color.Transparent,
                        unfocusedBorderColor = Color.Transparent,
                        cursorColor = Color.Black
                    ),
                    shape = RoundedCornerShape(24.dp)
                )
            }

            IconButton(
                onClick = {
                    hapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)
                    if (messageText.isNotBlank()) {
                        user?.let { sendMessage(eventId, it.userID, messageText) }
                        messageText = ""
                        keyboardController?.hide()
                    }
                },
                modifier = Modifier
                    .padding(start = 8.dp)
                    .size(48.dp)
                    .background(Color(0xFF007BFF), RoundedCornerShape(50))
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.sendmessage),
                    contentDescription = "send message",
                    modifier = Modifier.size(24.dp),
                    tint = Color.White
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
                onResult(activityName)
            } else {
                onResult(null)
            }
        }
        .addOnFailureListener { exception ->
            onResult(null)
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

            val userIds = result.documents.mapNotNull { it.getString("userID") }

            if (userIds.isEmpty()) {
                Log.d("Firestore", "No user IDs found")
                onResult(emptyList())
                return@addOnSuccessListener
            }

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

@Composable
fun TransparentStatusBar() {
    val systemUiController = rememberSystemUiController()

    systemUiController.setSystemBarsColor(
        color = Color.Transparent,
        darkIcons = false
    )
}