import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.text.KeyboardOptions
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
import androidx.compose.ui.hapticfeedback.HapticFeedback
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.TeamApp.R
import com.example.TeamApp.chat.Message
import com.example.TeamApp.chat.sendMessage
import com.google.firebase.firestore.FirebaseFirestore



import androidx.compose.material3.TopAppBar
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.ui.graphics.Color

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatScreen(eventId: String, currentUserId: String) {
    val db = FirebaseFirestore.getInstance()
    val messages = remember { mutableStateListOf<Message>() }
    var messageText by remember { mutableStateOf("") }
    var activityName by remember { mutableStateOf<String?>(null) }
    val listState = rememberLazyListState()
    val hapticFeedback = LocalHapticFeedback.current

    // Fetch the event name when eventId is passed
    LaunchedEffect(eventId) {
        getEventNameById(eventId) { name ->
            activityName = name
        }
    }

    LaunchedEffect(eventId) {
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
        // Add a top bar with the activity name
        TopAppBar(
            title = {
                Text(
                    text = activityName ?: "Loading...",  // Display activity name or "Loading..."
                    color = Color.White
                )
            },
            modifier = Modifier.fillMaxWidth(),
            colors = TopAppBarDefaults.smallTopAppBarColors(containerColor = Color.Black)  // Set the top bar color
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
                MessageItem(
                    message = message,
                    currentUserId = currentUserId,
                    previousMessageTimestamp = previousMessageTimestamp
                )
                // Update previous message timestamp
                previousMessageTimestamp = message.timestamp?.toDate()?.time
            }
        }

        Row(
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically // Center alignment
        ) {
            OutlinedTextField(
                value = messageText,
                onValueChange = { messageText = it },
                placeholder = { Text("Napisz wiadomość...") },
                modifier = Modifier.weight(1f),
                singleLine = true,
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    containerColor = Color.Transparent,
                    focusedBorderColor = Color.Black
                )
            )

            IconButton(
                onClick = {
                    // Perform haptic feedback
                    hapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)

                    if (messageText.isNotBlank()) {
                        sendMessage(eventId, currentUserId, messageText)
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

@Preview(showBackground = true)
@Composable
fun PreviewChatScreen() {
    // Replace `eventId` and `currentUserId` with test values
    val testEventId = "1DKd6noTqjcw8k13EXzH"
    val testUserId = "testUser123"

    // Call the `ChatScreen` function in `Preview`
    ChatScreen(eventId = testEventId, currentUserId = testUserId)
}
