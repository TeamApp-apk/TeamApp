package com.example.TeamApp.settings

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.TeamApp.R
import com.example.compose.primaryLight
import com.example.compose.secondaryLight
import com.example.ui.theme.fontFamily
import com.example.TeamApp.excludedUI.CustomSnackbar



@Composable
fun YourEventsScreen() {
    val gradientColors = listOf(
        Color(0xFFE8E8E8),
        Color(0xFF007BFF)
    )
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(brush = Brush.linearGradient(colors = gradientColors))
    ) {
        Column(
            modifier = Modifier
                .align(Alignment.Center) // Center the Column within the Box
                .width(360.dp)
                .height(764.dp)
                .background(color = Color(0xFFF2F2F2), shape = RoundedCornerShape(size = 16.dp))
                .padding(start = 10.dp, top = 10.dp, end = 10.dp, bottom = 10.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp, Alignment.Start),
                verticalAlignment = Alignment.Top, modifier = Modifier
                    .width(359.dp)
                    .height(45.dp)
                    .padding(start = 10.dp, top = 10.dp, end = 10.dp, bottom = 10.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.arrow_icon),
                    contentDescription = "arrow",
                    modifier = Modifier
                        .clickable {

                        }
                        .size(24.dp)
                        .padding(1.dp)

                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    modifier = Modifier.width(303.dp)
                        .height(25.dp),
                    text = "Twoje wydarzenia",
                    style = TextStyle(
                        fontSize = 20.sp,
                        lineHeight = 25.sp,
                        fontFamily = FontFamily(Font(R.font.proximanovabold)),
                        fontWeight = FontWeight(900),
                        color = Color(0xFF003366),
                        textAlign = TextAlign.Right,
                    )
                )

            }
        }
    }
}
//@Composable
//@Preview(showBackground = false)
//    fun yourEventsScreenPreview(){
//        YourEventsScreen()
//    }


