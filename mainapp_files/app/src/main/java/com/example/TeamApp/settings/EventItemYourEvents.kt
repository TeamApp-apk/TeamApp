package com.example.TeamApp.settings
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.TeamApp.data.Event
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun EventItem(event: Event, onClick: () -> Unit) {
    val formatter = java.time.format.DateTimeFormatter.ofPattern("d MMMM yyyy, HH:mm", Locale("pl"))
    val eventDateTime = try {
        java.time.LocalDateTime.parse(event.date, formatter)
    } catch (e: Exception) {
        null
    }

    val isPastEvent = eventDateTime?.isBefore(java.time.LocalDateTime.now()) == true
    val backgroundColor = if (isPastEvent) Color(0xFFE0E0E0) else Color.White  // Jasnoszary jeśli minęło

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(vertical = 4.dp, horizontal = 8.dp),
        elevation = 4.dp,
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(backgroundColor)
                .padding(12.dp)
        ) {
            Text(
                text = event.activityName ?: "Nieznana nazwa",
                style = MaterialTheme.typography.h6,
                fontWeight = FontWeight.Bold
            )


            Spacer(modifier = Modifier.height(4.dp))

            Text(text = event.location ?: "Brak wiadomości")
            Text(
                text = event.date,
                color = Color.Gray,
                fontSize = MaterialTheme.typography.caption.fontSize
            )
        }
    }
}


