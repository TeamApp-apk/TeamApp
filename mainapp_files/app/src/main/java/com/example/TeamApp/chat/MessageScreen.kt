import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
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
import androidx.compose.ui.unit.sp
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



    val bubbleShape = if (isCurrentUser) {
        RoundedCornerShape(topStart = 16.dp, bottomStart = 16.dp, topEnd = 4.dp, bottomEnd = 16.dp)
    } else {
        RoundedCornerShape(topStart = 4.dp, bottomStart = 16.dp, topEnd = 16.dp, bottomEnd = 16.dp)
    }

    val bubbleColor = if (isCurrentUser) Color(0xFF007BFF) else Color(0xFFEFEFEF) // Jasnoszary
    val textColor = if (isCurrentUser) Color.White else Color.Black
    val usernameColor = if (isCurrentUser) Color.White.copy(alpha = 0.7f) else Color.DarkGray.copy(alpha = 0.7f)
    val timeColor = if (isCurrentUser) Color.White.copy(alpha = 0.6f) else Color.Gray

    Column {
        if (showTimestampSeparator) {
            Text(
                text = formattedTime,
                fontFamily = FontFamily(Font(R.font.proximanovaregular)),
                fontSize = 12.sp, // Użyj sp
                color = Color.Gray,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp), // Większy padding
                textAlign = TextAlign.Center
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 4.dp) // Zmniejsz pionowy padding między wiadomościami
                .clickable {
                    hapticFeedback.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                    isExpanded = !isExpanded
                },
            horizontalArrangement = if (isCurrentUser) Arrangement.End else Arrangement.Start
        ) {
            Column(
                modifier = Modifier
                    .widthIn(max = 280.dp) // Ogranicz szerokość dymka
                    .background(
                        color = bubbleColor,
                        shape = bubbleShape
                    )
                    .padding(horizontal = 12.dp, vertical = 8.dp) // Większy padding wewnątrz dymka
            ) {
                // Display user name
                if (!isCurrentUser) { // Nazwa użytkownika tylko dla wiadomości drugiej osoby
                    Text(
                        text = username ?: "",
                        fontFamily = FontFamily(Font(R.font.proximanovabold)), // Lekko pogrubiona nazwa
                        fontSize = 13.sp,
                        color = usernameColor
                    )
                    Spacer(modifier = Modifier.height(2.dp)) // Mniejszy odstęp
                }

                // Display message content
                Text(
                    text = message.message,
                    fontFamily = FontFamily(Font(R.font.proximanovaregular)),
                    fontSize = 16.sp, // Normalny rozmiar tekstu wiadomości
                    color = textColor
                )

                // Display full date/time only if message is expanded
                if (isExpanded) {
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = formattedDate,
                        fontFamily = FontFamily(Font(R.font.proximanovaregular)),
                        fontSize = 10.sp, // Mniejszy rozmiar
                        color = timeColor,
                        modifier = Modifier.align(Alignment.End) // Wyrównaj do prawej
                    )
                }
            }
        }
    }
}




