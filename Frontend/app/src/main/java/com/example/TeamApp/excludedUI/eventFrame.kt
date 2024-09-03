package com.example.TeamApp.excludedUI

import android.content.Context
import android.util.Log
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.TeamApp.R


object Variables {
    val LightGray: Color = Color(0xFFF2F2F2)
    val P3: Color = Color(0xFF1A73E8)
    val Black: Color = Color(0xFF000000)
}
fun getIconResourceId(context: Context, iconName: String): Int {
    val resourceId = context.resources.getIdentifier(iconName, "drawable", context.packageName)
    Log.d("ResourceCheck", "Resource ID for $iconName: $resourceId")
    return resourceId
}



@Composable
fun ActivityCard(
    iconResId: String,
    date: String,
    activityName: String,
    currentParticipants: Int,
    maxParticipants: Int,
    location: String,
    isHighlighted: Boolean=false,
    onClick: () -> Unit
) {
    val targetColor = if (isHighlighted) Color(0xffbdbdbd) else Variables.LightGray
    val backgroundColor by animateColorAsState(
        targetColor,
        animationSpec = tween(durationMillis = 1000)
    )
    val elevation by animateDpAsState(
        targetValue = if (isHighlighted) 16.dp else 4.dp,
        animationSpec = tween(durationMillis = 300)
    )

    val context = LocalContext.current
    val iconId = getIconResourceId(context, iconResId)
    Row(
        modifier = Modifier
            .shadow(
                elevation = elevation,
                spotColor = Color(0x12535990),
                ambientColor = Color(0x12535990)
            )
            .padding(4.dp)
            .fillMaxWidth()
            .height(120.dp)
            .background(backgroundColor, shape = RoundedCornerShape(size = 10.dp))
            .padding(top = 10.dp, bottom = 10.dp)
    ) {
        Image(
            painter = painterResource(id = iconId),
            contentDescription = "Activity Icon",
            modifier = Modifier
                .padding(8.dp)
                .width(84.dp)
                .height(96.dp)
        )
        Spacer(modifier = Modifier.width(4.dp))
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
        ) {
            Row(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = date,
                    style = TextStyle(
                        fontSize = 16.sp,
                        fontFamily = FontFamily(Font(R.font.proximanovabold)),
                        fontWeight = FontWeight.ExtraBold,
                        //fontStyle = FontStyle.Italic,
                        color = Variables.P3
                    ),
                    modifier = Modifier.weight(1f) // Wypełnienie dostępnej przestrzeni
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = "$currentParticipants/$maxParticipants",
                    style = TextStyle(
                        fontSize = 16.sp,
                        fontFamily = FontFamily(Font(R.font.proximanovabold)),
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
                        .width(32.dp)
                        .height(28.dp)
                )
            }
            Text(
                text = activityName,
                style = TextStyle(
                    fontSize = 15.sp, // Zmieniamy rozmiar czcionki
                    lineHeight = 20.sp,
                    fontFamily = FontFamily(Font(R.font.proximanovabold)),
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
                        fontSize = 14.sp, // Zmieniamy rozmiar czcionki
                        fontFamily = FontFamily(Font(R.font.proximanovalight)),
                        fontWeight = FontWeight.Normal,
                        fontStyle = FontStyle.Italic,
                        color = Variables.Black,
                    ),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f)
                )
            }


        }
    }
}

//@Preview(showBackground = false)
//@Composable
//fun PreviewActivityCard() {
//    ActivityCard(
//        iconResId = R.drawable.figma_gym_icon,
//        date = "23 PAŹDZIERNIKA 12:45",
//        activityName = "Skok ze spadochronem",
//        currentParticipants = 21,
//        maxParticipants = 32,
//        location = "Beliny-Prażmowskiego",
//        onClick = { /* akcja na kliknięcie */ }
//    )
//}
