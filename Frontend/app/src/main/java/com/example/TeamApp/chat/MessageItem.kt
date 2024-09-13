package com.example.TeamApp.chat

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import com.example.TeamApp.R
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.Locale

@Composable
fun MessageItem(message: Message, currentUserId: String) {
    val isCurrentUser = message.userId == currentUserId

    // Tworzymy formatter dla polskiego formatu daty
    val polishDateFormatter = DateTimeFormatter.ofPattern("HH:mm, dd MMMM yyyy", Locale("pl", "PL"))

    // Formatowanie daty
    val formattedDate = message.timestamp?.toDate()
        ?.toInstant()
        ?.atZone(ZoneId.systemDefault())
        ?.toLocalDateTime()
        ?.format(polishDateFormatter) ?: ""

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
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
            Text(
                text = message.userId,
                style = MaterialTheme.typography.caption,
                fontFamily = FontFamily(Font(R.font.proximanovaregular)),
                color = if (isCurrentUser) MaterialTheme.colors.onPrimary else MaterialTheme.colors.onSurface
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = formattedDate,
                fontFamily = FontFamily(Font(R.font.proximanovaregular)),
                style = MaterialTheme.typography.overline,
                color = if (isCurrentUser) MaterialTheme.colors.onPrimary else MaterialTheme.colors.onSurface
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = message.message,
                fontFamily = FontFamily(Font(R.font.proximanovaregular)),
                style = MaterialTheme.typography.body1,
                color = if (isCurrentUser) MaterialTheme.colors.onPrimary else MaterialTheme.colors.onSurface
            )
        }
    }
}
