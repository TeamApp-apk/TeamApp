package com.example.TeamApp.event
import DescriptionTextField
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.TeamApp.R
import com.example.TeamApp.excludedUI.EventButton

@Composable
fun DetailsScreen() {
    val gradientColors = listOf(
        Color(0xFFE8E8E8),
        Color(0xFF007BFF)
    )

    var isJoined by remember { mutableStateOf(false) }

    // Outer Box filling the whole screen with gradient background
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(brush = Brush.linearGradient(colors = gradientColors))
            .padding(horizontal = 16.dp, vertical = 36.dp),
        contentAlignment = Alignment.Center // Centering the content
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxSize()
                .background(color = Color(0xFFF2F2F2), shape = RoundedCornerShape(size = 16.dp))
        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically, // Ensures vertical alignment of icon and text
                    horizontalArrangement = Arrangement.SpaceBetween, // Keeps everything aligned to the left
                    modifier = Modifier
                        .fillMaxWidth() // Makes the Row take up full width
                        .padding(bottom = 24.dp)

                ) {
                    IconButton(
                        onClick = { /* Handle back click */ },
                        modifier = Modifier
                            .size(26.dp),// Set size for the icon
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.arrowleft),
                            contentDescription = "Back Icon"
                        )
                    }


                    Text(
                        text = "Szczegóły wydarzenia",
                        style = TextStyle(
                            fontSize = 22.sp,
                            fontFamily = FontFamily(Font(R.font.robotobold)),
                            fontWeight = FontWeight(900),
                            color = Color(0xFF003366),
                            textAlign = TextAlign.Start, // Align text to the start
                            lineHeight = 25.sp,
                        ),
                        modifier = Modifier
                            .align(Alignment.CenterVertically) // Ensure text is vertically aligned with icon
                    )
                }
//                Text(
//                    text = "Tutaj bedzie opis eventu...",
//                    style = TextStyle(
//                        fontSize = 16.sp,
//                        fontFamily = FontFamily(Font(R.font.robotothin)),
//                        fontWeight = FontWeight.Black,
//                        color = Color.Black,
//                        textAlign = TextAlign.Start, // Align text to the start
//                    ),
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .fillMaxHeight(0.36f)
//                )
                DescriptionTextField(label = "wpisz cos", isEditable = false)
                Text(
                    text = "Lokalizacja:",
                    style = TextStyle(
                        fontSize = 22.sp,
                        fontFamily = FontFamily(Font(R.font.robotobold)),
                        fontWeight = FontWeight(900),
                        color = Color(0xFF003366),
                        textAlign = TextAlign.Start, // Align text to the start
                        lineHeight = 25.sp,
                    ),
                    modifier = Modifier.padding(bottom = 20.dp)
                )
                Image(
                    painter = painterResource(id = R.drawable.exmap),
                    contentDescription = "Activity Icon",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(220.dp)
                        .padding(bottom = 20.dp)
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 20.dp)
                ){
                    Image(
                        painter = painterResource(id = if (isJoined) R.drawable.joinstatuson else R.drawable.joinstatusoff),
                        contentDescription = "Activity Icon",
                        modifier = Modifier
                            .wrapContentWidth()
                            .height(24.dp)
                    )
                    Spacer(Modifier.width(8.dp))
                    Text(
                        text = if (isJoined) "dołączono" else "nie dołączono",
                        style = TextStyle(
                            fontSize = 14.sp,
                            fontFamily = FontFamily(Font(R.font.robotoblackitalic)),
                            fontWeight = FontWeight.ExtraBold,
                            color = Color.Black,
                        )
                    )
                }
                Column(modifier = Modifier
                    .padding(horizontal = 24.dp, vertical = 8.dp)
                ) {
                    EventButton(
                        text = if (isJoined) "OPUŚĆ" else "DOŁĄCZ",
                        onClick = {
                            isJoined = !isJoined
                            // TODO: Handle the button action
                        }
                    )
                    EventButton(
                        text = "CZAT",
                        onClick = { /*TODO*/ }
                    )
                }
            }
        }
    }
}



@Preview(showBackground = true)
@Composable
fun DetailsScreenPreview() {
    DetailsScreen()
}

