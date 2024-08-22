package com.example.TeamApp.event
import DescriptionTextField
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
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
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.TeamApp.R
import org.intellij.lang.annotations.JdkConstants.HorizontalAlignment

@Composable
fun CreateEventv2() {
    val gradientColors = listOf(
        Color(0xFFE8E8E8),
        Color(0xFF007BFF)
    )

    // Outer Box filling the whole screen with gradient background
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(brush = Brush.linearGradient(colors = gradientColors))
            .padding(horizontal = 12.dp, vertical = 36.dp),
        contentAlignment = Alignment.Center // Centering the content
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxSize()
                .background(color = Color(0xFFF2F2F2), shape = RoundedCornerShape(size = 16.dp))
        ) {
            // Inner Box centered with specific dimensions and background color
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxSize()
                    .padding(start = 10.dp, top = 10.dp, end = 10.dp, bottom = 10.dp)
                    .background(color = Color(0xFFF2F2F2), shape = RoundedCornerShape(size = 16.dp)),
                horizontalAlignment = Alignment.CenterHorizontally
            ){
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
                        ) }

                    Text(
                        text = "Stwórz wydarzenie",
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
                Spacer(modifier = Modifier.height(20.dp))
                Text(modifier = Modifier
                    .width(328.dp)
                    .height(20.dp)
                    .padding(horizontal = 16.dp)
                    ,
                    text = "Dodaj opis",
                    style = TextStyle(
                        fontSize = 16.sp,
                        lineHeight = 12.sp,
                        fontFamily = FontFamily(Font(R.font.robotobold)),
                        fontWeight = FontWeight(800),
                        color = Color(0xFF003366),
                    )
                )
                DescriptionTextField(label = "Opisz pokrótce szczegóły wydarzenia...", isEditable = true)
                Column(
                    modifier = Modifier.padding(horizontal = 28.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ){
                    Row(
                        modifier = Modifier
                            .padding(top = 8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {

                    }
                }
            }
        }
    }
}

@Preview(showBackground = false)
@Composable
fun CreateEventv2Preview() {
    CreateEventv2()
}




