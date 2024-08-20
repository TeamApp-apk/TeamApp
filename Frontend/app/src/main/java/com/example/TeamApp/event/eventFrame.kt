package com.example.TeamApp.event

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.TeamApp.R


object Variables {
    val LightGray: Color = Color(0xFFF2F2F2)
    val P3: Color = Color(0xFF1A73E8)
    val Black: Color = Color(0xFF000000)
}

@Composable
fun ActivityCard(
    iconResId: Int,
    date: String,
    activityName: String,
    currentParticipants: Int,
    maxParticipants: Int,
    location: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .shadow(
                elevation = 25.dp,
                spotColor = Color(0x12535990),
                ambientColor = Color(0x12535990)
            )
            .padding(4.dp)
            .width(348.dp)
            .height(106.dp)
            .background(color = Variables.LightGray, shape = RoundedCornerShape(size = 16.dp))
            .padding(start = 4.dp, top = 10.dp, end = 4.dp, bottom = 10.dp)
    ) {
        Image(
            painter = painterResource(id = iconResId),
            contentDescription = "Activity Icon",
            modifier = Modifier
                .padding(0.dp)
                .width(84.dp)
                .height(84.dp)
        )
        Spacer(modifier = Modifier.width(4.dp))
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp),
        ) {
            Row(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = date,
                    style = TextStyle(
                        fontSize = 15.sp,
                        fontFamily = FontFamily(Font(R.font.robotoblackitalic)),
                        fontWeight = FontWeight(900),
                        fontStyle = FontStyle.Italic,
                        color = Variables.P3
                    ),
                    modifier = Modifier.weight(1f) // Wypełnienie dostępnej przestrzeni
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = "$currentParticipants/$maxParticipants",
                    style = TextStyle(
                        fontSize = 12.sp,
                        fontFamily = FontFamily(Font(R.font.robotobold)),
                        fontWeight = FontWeight(500),
                        fontStyle = FontStyle.Italic,
                        color = Variables.Black,
                        textAlign = TextAlign.Start,
                    )
                )
                Image(
                    painter = painterResource(id = R.drawable.usersicon),
                    contentDescription = "Participants Icon",
                    modifier = Modifier
                        .width(25.12.dp)
                        .height(22.94.dp)
                )
            }
            Text(
                text = activityName,
                style = TextStyle(
                    fontSize = 14.sp, // Zmieniamy rozmiar czcionki
                    lineHeight = 20.sp,
                    fontFamily = FontFamily(Font(R.font.robotobold)),
                    fontWeight = FontWeight(500),
                    color = Color(0xFF120D26),
                ),
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.End
            ) {
                Image(
                    painter = painterResource(id = R.drawable.locicon),
                    contentDescription = "Location Icon",
                    modifier = Modifier
                        .padding(1.dp)
                        .width(24.dp)
                        .height(24.dp)
                )
                Spacer(modifier = Modifier.width(4.dp)) // Opcjonalnie, dodaje odstęp między ikoną a tekstem
                Text(
                    text = location,
                    style = TextStyle(
                        fontSize = 12.sp, // Zmieniamy rozmiar czcionki
                        fontFamily = FontFamily(Font(R.font.thinitalic)),
                        fontWeight = FontWeight.Black,
                        fontStyle = FontStyle.Italic,
                        color = Variables.Black,
                    ),
                )
            }


        }
    }
}

@Preview(showBackground = false)
@Composable
fun PreviewActivityCard() {
    ActivityCard(
        iconResId = R.drawable.dumbbelliconv2,
        date = "23 PAŹDZIERNIKA 12:45",
        activityName = "Skok ze spadochronem",
        currentParticipants = 21,
        maxParticipants = 32,
        location = "Beliny-Prażmowskiego",
        onClick = { /* akcja na kliknięcie */ }
    )
}
