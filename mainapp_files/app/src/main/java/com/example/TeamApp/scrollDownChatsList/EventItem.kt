package com.example.TeamApp.scrollDownChatsList

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.TeamApp.R
import com.example.TeamApp.data.Event
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import java.text.SimpleDateFormat
import java.util.Locale

@Composable
fun EventItem(event: Event, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(3.dp)
            .clip(RoundedCornerShape(12.dp))
            .clickable { onClick() },
        elevation = 6.dp
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            val iconResId = try {
                Event.sportIcons[event.activityName]?.let { iconName ->
                    R.drawable::class.java.getField(iconName).getInt(null)
                } ?: android.R.drawable.ic_dialog_alert
            } catch (e: Exception) {
                android.R.drawable.ic_dialog_alert
            }

            Image(
                painter = painterResource(id = iconResId),
                contentDescription = "Ikona wydarzenia",
                modifier = Modifier
                    .size(50.dp)
                    .clip(CircleShape)
            )

            Column(
                modifier = Modifier
                    .padding(start = 8.dp)
                    .weight(1f)
                    .align(Alignment.CenterVertically)
            ) {
                Text(
                    text = event.activityName ?: "Brak tytułu",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Start,
                    modifier = Modifier.fillMaxWidth()
                )
                Text(
                    text = event.lastMessage?.let { lastMessage ->
                        val currentUserId = FirebaseAuth.getInstance().currentUser?.uid
                        val senderId = lastMessage["userId"] as? String
                        val prefix = if (senderId == currentUserId) "Ty:" else "${lastMessage["userName"] ?: "Unknown"}:"
                        val message = lastMessage["message"] as? String ?: "Brak wiadomości"
                        val totalMaxLength = 32 // Total length for prefix + message + ellipsis
                        val ellipsis = "..."
                        val prefixLength = prefix.length
                        val maxMessageLength = totalMaxLength - prefixLength - ellipsis.length - 1 // -1 for space
                        val truncatedMessage = if (message.length > maxMessageLength && maxMessageLength > 0) {
                            message.take(maxMessageLength) + ellipsis
                        } else {
                            message
                        }
                        "$prefix $truncatedMessage"
                    } ?: "Brak wiadomości",
                    fontSize = 16.sp,
                    textAlign = TextAlign.Start,
                    modifier = Modifier.fillMaxWidth()
                )
                Text(
                    text = event.lastMessage?.let { lastMessage ->
                        (lastMessage["timestamp"] as? Timestamp)?.let { timestamp ->
                            SimpleDateFormat("dd-MM-yyyy HH:mm", Locale.getDefault()).format(timestamp.toDate())
                        } ?: ""
                    } ?: "",
                    fontSize = 10.sp,
                    textAlign = TextAlign.Start,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            Image(
                painter = painterResource(id = R.drawable.arrow_icon),
                contentDescription = "Ikona strzałki",
                modifier = Modifier
                    .size(50.dp)
                    .clip(CircleShape)
                    .padding(start = 8.dp)
                    .rotate(180f)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun EventItemPreview() {
    val mockEvent = Event(
        id = "mock123",
        creatorID = "user456",
        iconResId = "figma_soccer_icon",
        pinIconResId = "figma_soccer_pin",
        date = "2025-05-19 20:12",
        activityName = "Piłka nożna",
        currentParticipants = 5,
        maxParticipants = 10,
        location = "Boisko nr 2, Warszawa",
        description = "Zbieramy się na mecz",
        locationID = null,
        participants = mutableListOf("user1", "user2", "user3"),
        lastMessage = mapOf(
            "userId" to "user456",
            "userName" to "John Doe",
            "message" to "Hej, gotowi na mecz?",
            "timestamp" to Timestamp.now()
        )
    )

    EventItem(event = mockEvent) {
        // Empty lambda for preview
    }
}