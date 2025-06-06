package com.example.TeamApp.settings

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.TeamApp.R
import com.example.TeamApp.data.Event
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun EventItem(event: Event, onClick: () -> Unit) {
    val formatter = java.time.format.DateTimeFormatterBuilder()
        .parseCaseInsensitive()
        .appendPattern("d MMMM yyyy, HH:mm") // Upewnij się, że format jest poprawny, dodałem yyyy dla roku
        .toFormatter(Locale("pl"))

    val eventDateTime = try {
        java.time.LocalDateTime.parse(event.date, formatter)
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }

    val isPastEvent = eventDateTime?.isBefore(java.time.LocalDateTime.now()) == true
    val backgroundColor = if (isPastEvent) Color(0xFFE0E0E0) else Color.White // Ta zmienna nie jest używana w Card, ale może być przydatna
    val cardShape  = RoundedCornerShape(16.dp)
    Card(
        modifier = Modifier
            .padding(vertical = 4.dp, horizontal = 8.dp)
            .fillMaxWidth()

            .clickable { onClick() }
            ,
        elevation = 8.dp,
        shape = cardShape
        // backgroundColor = backgroundColor // Możesz chcieć użyć tego tutaj jeśli Card ma mieć inny kolor tła
    ) {
        Box {
            Row(
                verticalAlignment = Alignment.CenterVertically,

                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            ) {
                // 1. Ikona sportu (lewa strona)
                val iconResId = try {
                    Event.sportIcons[event.activityName]?.let { iconName ->
                        R.drawable::class.java.getField(iconName).getInt(null)
                    } ?: android.R.drawable.ic_dialog_alert // Domyślna ikona systemowa
                } catch (e: Exception) {
                    android.R.drawable.ic_dialog_alert // Domyślna ikona systemowa w razie błędu
                }

                Image(
                    painter = painterResource(id = iconResId),
                    contentDescription = "Ikona wydarzenia",
                    modifier = Modifier
                        .size(50.dp)
                        .clip(shape = CircleShape)
                )

                // Spacer między ikoną sportu a środkową kolumną
                Spacer(modifier = Modifier.width(12.dp))

                // 2. Kolumna ze szczegółami wydarzenia (środek)
                Column(
                    modifier = Modifier
                        .weight(1f) // Kluczowa zmiana: ta kolumna zajmie dostępną przestrzeń
                        .padding(horizontal = 8.dp), // Dodano padding horyzontalny dla estetyki
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = event.activityName ?: "Nieznana nazwa",
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.subtitle1,
                        textAlign = TextAlign.Center,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = event.location?.take(30) ?: "Brak lokalizacji",
                        style = MaterialTheme.typography.body2,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = event.date, // Formatowanie daty odbywa się wyżej, ale nie jest tu używane eventDateTime
                        color = Color.Gray,
                        style = MaterialTheme.typography.caption,
                        textAlign = TextAlign.Center,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                // Spacer między środkową kolumną a kolumną uczestników
                Spacer(modifier = Modifier.width(8.dp))

                // 3. Kolumna z ikoną użytkownika i liczbą uczestników (prawa strona)
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center // Aby wycentrować zawartość w pionie w tej małej kolumnie
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.user), // Upewnij się, że masz ten zasób
                        contentDescription = "Ikona uczestników",
                        modifier = Modifier
                            .size(24.dp) // Można dostosować rozmiar
                            .clip(shape = CircleShape)
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = "${event.currentParticipants}/${event.maxParticipants}",
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.body2, // Zmniejszono trochę dla lepszego dopasowania
                        textAlign = TextAlign.Center
                    )
                }
            }

            if (isPastEvent) {
                Box(
                    modifier = Modifier
                        .matchParentSize()
                        .background(Color(0xAA888888)) // Półprzezroczyste tło dla nieaktywnego
                )
                Text(
                    text = "Nieaktywne",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.h6, // Można dostosować styl
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        }
    }
}

