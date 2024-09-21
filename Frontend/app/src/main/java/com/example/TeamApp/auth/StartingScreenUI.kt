package com.example.TeamApp.auth

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.TeamApp.R
import com.example.TeamApp.auth.LoginViewModel
import com.example.TeamApp.event.CreateEventViewModel
import com.example.TeamApp.event.ViewModelProvider
import androidx.compose.material3.RangeSlider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.background
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource

import androidx.compose.ui.text.buildAnnotatedString

import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout

@Composable
fun StartingScreenUI(){
    val gradientColorsBackGround = listOf(
        Color(0xFFE8E8E8),
        Color(0xFF007BFF),
    )
    ConstraintLayout(modifier = Modifier
        .fillMaxSize()
        .background(Brush.linearGradient(colors = gradientColorsBackGround))
        .padding(10.dp)) {
        val (image,loginButton,registerButton,WelcomeText,RowWithOtherOptions) = createRefs()
        val imageStart = createGuidelineFromStart(0.1f)
        val imageEnd = createGuidelineFromStart(0.9f)
        val imageTop = createGuidelineFromTop(0.19f)
        val imageBottom = createGuidelineFromBottom(0.56f)

            Image(
                painter = painterResource(id = R.drawable.logo),
                contentDescription = null,
                modifier= Modifier
                    .fillMaxSize()

                    .constrainAs(image) {
                        top.linkTo(imageTop)
                        start.linkTo(imageStart)
                        end.linkTo(imageEnd)
                        bottom.linkTo(imageBottom)
                        height = Dimension.fillToConstraints
                        width = Dimension.fillToConstraints

                    }

            )
        Text(
            modifier = Modifier
                .width(253.dp)
                .padding(vertical = 4.dp)
                .height(96.dp)
                .constrainAs(WelcomeText) {
                    top.linkTo(image.bottom, margin = 40.dp)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)

                },
            text = "Załóż darmowe konto",
            style = TextStyle(
                fontSize = 24.sp,
                fontFamily = FontFamily(Font(R.font.proximanovaregular)),
                fontWeight = FontWeight(900),
                color = Color.White,

                textAlign = TextAlign.Center,
                letterSpacing = 1.sp,
            )

        )
        CreateAccountStartUI(Modifier.constrainAs(loginButton){
            top.linkTo(WelcomeText.bottom)
            start.linkTo(parent.start)
            end.linkTo(parent.end)
        })
        LogInStartUI(modifier = Modifier.constrainAs(registerButton) {
            top.linkTo(loginButton.bottom, margin = 30.dp)
            start.linkTo(parent.start)
            end.linkTo(parent.end)
        })

        RowWithDifferentOptions(modifier = Modifier.constrainAs(RowWithOtherOptions) {
            top.linkTo(registerButton.bottom, margin = 104.dp)
            start.linkTo(parent.start)
            end.linkTo(parent.end)
        })






}
}
@Composable
fun CreateAccountStartUI(modifier: Modifier) {
    // Define gradient colors
    val gradientColors = listOf(
        Color(0xB5D46161), // Light Red
        Color(0xFF007BFF)  // Blue
    )

    Button(
        onClick = { /* Your action here */ },
        colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
        modifier = modifier

            .width(200.46777.dp)
            .height(48.74976.dp)// Adjust button size
            .background(
                brush = Brush.horizontalGradient(gradientColors),
                shape = RoundedCornerShape(16.dp)

            ),

        contentPadding = PaddingValues(0.dp),
        shape = RoundedCornerShape(16.dp)// Remove default padding
    ) {
        // Button text or content
        Text(modifier = Modifier
            .width(128.71365.dp)
            .height(16.dp)
            ,
            text = "STWÓRZ KONTO",
            style = TextStyle(
                fontSize = 12.sp,
                fontFamily = FontFamily(Font(R.font.proximanovabold)),
                fontWeight = FontWeight(500),
                color = Color.White,

                textAlign = TextAlign.Center,
                letterSpacing = 1.sp,
            )
        )
    }
}


@Preview(showBackground = false)
@Composable
fun CreateAccountStartUIPreview() {
    StartingScreenUI()
}
@Composable
fun LogInStartUI(modifier: Modifier) {
    // Define gradient colors
    val gradientColors = listOf(
        Color(0xB5D46161), // Light Red
        Color(0xFF007BFF)  // Blue
    )

    Button(
        onClick = { /* Your action here */ },
        colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
        modifier = modifier

            .width(200.46777.dp)
            .height(48.74976.dp)// Adjust button size
            .background(
                brush = Brush.horizontalGradient(gradientColors),
                shape = RoundedCornerShape(16.dp)

            ),

        contentPadding = PaddingValues(0.dp),
        shape = RoundedCornerShape(16.dp)// Remove default padding
    ) {
        // Button text or content
        Text(modifier = Modifier
            .width(128.71365.dp)
            .height(16.dp),
            text = "ZALOGUJ SIĘ",
            style = TextStyle(
                fontSize = 12.sp,
                fontFamily = FontFamily(Font(R.font.proximanovabold)),
                fontWeight = FontWeight(500),
                color = Color.White,

                textAlign = TextAlign.Center,
                letterSpacing = 1.sp,
            )
        )
    }
}
@Composable
fun RowWithDifferentOptions(modifier: Modifier) {
    Row(
        modifier = modifier

            .width(349.dp)
            .height(51.dp)
            .padding(start = 10.dp, top = 10.dp, end = 10.dp, bottom = 10.dp),
        horizontalArrangement = Arrangement.spacedBy(64.dp, Alignment.CenterHorizontally),
        verticalAlignment = Alignment.Bottom,
    ) {

        Text(modifier = Modifier
            .width(60.dp)
            .height(16.dp)
            .clickable {  },
            text = "O aplikacji",
            style = TextStyle(
                fontSize = 10.sp,
                fontFamily = FontFamily(Font(R.font.proximanovaregular)),
                fontWeight = FontWeight(700),
                color = Color.White,

                textAlign = TextAlign.Center,
                letterSpacing = 1.sp,
            )
        )

        // Clickable text 2
        Text(modifier = Modifier
            .width(60.dp)
            .height(16.dp)
            .clickable {  },
            text = "Prywatność",
            style = TextStyle(
                fontSize = 10.sp,
                fontFamily = FontFamily(Font(R.font.proximanovaregular)),
                fontWeight = FontWeight(700),
                color = Color.White,

                textAlign = TextAlign.Center,
                letterSpacing = 1.sp,
            )
        )

        // Clickable text 3
        Text(modifier = Modifier
            .width(60.dp)
            .height(16.dp)
            .clickable {  },
            text = "Warunki",
            style = TextStyle(
                fontSize = 10.sp,
                fontFamily = FontFamily(Font(R.font.proximanovaregular)),
                fontWeight = FontWeight(700),
                color = Color.White,

                textAlign = TextAlign.Center,
                letterSpacing = 1.sp,
            )
        )
    }
}




