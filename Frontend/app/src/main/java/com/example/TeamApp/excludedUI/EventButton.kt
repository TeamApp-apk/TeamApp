package com.example.TeamApp.excludedUI

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.TeamApp.R

@Composable
fun EventButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .padding(vertical = 20.dp, horizontal = 0.dp)
            .fillMaxWidth()
            .height(56.dp)  // Taka sama wysokość jak w SearchStreetField
            .background(color = Color(0xFF007BFF), shape = RoundedCornerShape(size = 70.dp)),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color(0xff4fc3f7), // Kolor tła przycisku
            contentColor = Color.Black // Kolor tekstu przycisku
        )

    ) {
        Text(
            text = text,
            style = TextStyle(
                fontSize = 19.sp,
                fontFamily = FontFamily(Font(R.font.robotobold)),
                fontWeight = FontWeight(600),
                color = Color(0xFF003366), // Kolor tekstu w przycisku
                textAlign = TextAlign.Center,
                letterSpacing = 1.sp
            )
        )
    }
}