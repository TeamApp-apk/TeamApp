import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.platform.LocalHapticFeedback
import com.example.TeamApp.R
import com.example.TeamApp.chat.Message
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.text.style.TextAlign
import androidx.navigation.NavController
import com.google.firebase.firestore.FirebaseFirestore

@Composable
fun MessageScreen(
    message: Message,
    currentUserId: String,
    previousMessageTimestamp: Long?
) {
    var username by remember { mutableStateOf<String?>(null) }
    val isCurrentUser = message.userId == currentUserId
    val viewModel = UserViewModel()
    LaunchedEffect(message.userId) {
        viewModel.getUsernameById(message.userId) { fetchedUsername ->
            username = fetchedUsername
        }
    }

    // Formatter for displaying only hour and minutes
    val timeFormatter = DateTimeFormatter.ofPattern("HH:mm", Locale("pl", "PL"))

    // Formatter for displaying full date
    val dateFormatter = DateTimeFormatter.ofPattern("HH:mm, dd MMMM yyyy", Locale("pl", "PL"))

    // Format the timestamp to show only hour and minutes for separator
    val formattedTime = message.timestamp?.toDate()
        ?.toInstant()
        ?.atZone(ZoneId.systemDefault())
        ?.toLocalDateTime()
        ?.format(timeFormatter) ?: ""

    // Format the timestamp to show full date when expanded
    val formattedDate = message.timestamp?.toDate()
        ?.toInstant()
        ?.atZone(ZoneId.systemDefault())
        ?.toLocalDateTime()
        ?.format(dateFormatter) ?: ""

    // State to track if the message is expanded
    var isExpanded by remember { mutableStateOf(false) }

    // Haptic feedback instance
    val hapticFeedback = LocalHapticFeedback.current

    // Calculate time difference for separator
    val showTimestampSeparator = previousMessageTimestamp?.let {
        val currentTimestamp = message.timestamp?.toDate()?.time ?: 0
        (currentTimestamp - it) > 300000// 5mins in milli
    } ?: true

    Column {
        if (showTimestampSeparator) {
            // Display timestamp separator
            Text(
                text = formattedTime,
                fontFamily = FontFamily(Font(R.font.proximanovaregular)),
                style = MaterialTheme.typography.overline,
                color = Color.Gray,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                textAlign = TextAlign.Center
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
                .clickable {
                    // Perform haptic feedback
                    hapticFeedback.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                    // Toggle expanded state
                    isExpanded = !isExpanded
                },
            horizontalArrangement = if (isCurrentUser) Arrangement.End else Arrangement.Start
        ) {
            Column(
                modifier = Modifier
                    .background(
                        if (isCurrentUser) Color(0xFF007BFF) else Color(0xFFF2F2F2),
                        shape = RoundedCornerShape(8.dp)
                    )
                    .padding(8.dp)
            ) {
                // Display user name
                Text(
                    text = username ?: "",
                    style = MaterialTheme.typography.caption,
                    fontFamily = FontFamily(Font(R.font.proximanovaregular)),
                    color = if (isCurrentUser) MaterialTheme.colors.onPrimary else MaterialTheme.colors.onSurface
                )

                Spacer(modifier = Modifier.height(4.dp))

                // Display message content
                Text(
                    text = message.message,
                    fontFamily = FontFamily(Font(R.font.proximanovaregular)),
                    style = MaterialTheme.typography.body1,
                    color = if (isCurrentUser) MaterialTheme.colors.onPrimary else MaterialTheme.colors.onSurface
                )

                // Display full date/time only if message is expanded
                if (isExpanded) {
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = formattedDate,
                        fontFamily = FontFamily(Font(R.font.proximanovaregular)),
                        style = MaterialTheme.typography.overline,
                        color = if (isCurrentUser) MaterialTheme.colors.onPrimary else MaterialTheme.colors.onSurface
                    )
                }
            }
        }
    }
}




