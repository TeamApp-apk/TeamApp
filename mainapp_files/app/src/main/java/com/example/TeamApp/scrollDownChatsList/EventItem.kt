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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.TeamApp.R
import com.example.TeamApp.data.Event
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

@Composable
fun EventItem(event: Event, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp)
            .clip(RoundedCornerShape(12.dp))
            .clickable { onClick() },
        elevation = 6.dp
    ) {
        Row(
            modifier = Modifier.padding(12.dp),

                    verticalAlignment = Alignment.CenterVertically
        ) {
            // Używamy ikony z event.iconResId
            val iconResId = sportIcons[event.activityName] ?: android.R.drawable.ic_dialog_alert

            Image(
                painter = painterResource(id = iconResId),
                contentDescription = "Ikona wydarzenia",
                modifier = Modifier
                    .size(50.dp)
                    .clip(CircleShape)




            )

            Column(modifier = Modifier.padding(3.dp).align(Alignment.CenterVertically)) {
                Text(
                    text = event.activityName ?: "Brak tytułu",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
                Text(
                    text = event.description ?: "Tutaj ma byc ostatnia wiadomosc z chatu",
                    fontSize = 16.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
                //Tutaj sie doda timestampa kiedy ostatnia wiadomosc zostala wyslana
                Text(
                    text = event.date,
                    fontSize = 10.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}

@Preview(showBackground = false)
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
        description = "Zbieramy się na szybki meczyk, poziom: rekreacyjny",
        locationID = null,
        participants = mutableListOf("user1", "user2", "user3")
    )

    EventItem(event = mockEvent) {
        // Pusta lambda, niepotrzebna w preview
    }
}

