package com.example.TeamApp.searchThrough
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.TeamApp.R
import com.example.TeamApp.auth.LoginViewModel
import com.example.TeamApp.event.CreateEventViewModel
import com.example.TeamApp.event.ViewModelProvider

@Composable
fun FiltersScreen(navController: NavController) {
    val viewModel: SearchThroughViewModel = viewModel()
    var showEmptyMessage by remember { mutableStateOf(false) }
    //val context = LocalContext.current

    val gradientColors = listOf(
        Color(0xFFE8E8E8),
        Color(0xFF007BFF),
    )
    ConstraintLayout(modifier = Modifier.fillMaxSize()) {
        val (acceptButton, reset, arrow ) = createRefs()
        val arrowStart = createGuidelineFromStart(0.1f)
        val arrowEnd = createGuidelineFromStart(0.2f)
        val arrowTop = createGuidelineFromTop(0.1f)
        val arrowBottom = createGuidelineFromTop(0.2f)

        Image(
            painter = painterResource(id = R.drawable.chevron_left_white),
            contentDescription = "back",
            modifier = Modifier
                .clickable { /* Handle back click */ }
                .size(40.dp)
                .border(1.dp, Color.Black)
                .constrainAs(arrow) {
                    top.linkTo(arrowTop)
                    bottom.linkTo(arrowBottom)
                    start.linkTo(arrowStart)
                    end.linkTo(arrowEnd)
                    height = Dimension.fillToConstraints
                    width = Dimension.fillToConstraints
                }
        )


        val passStart = createGuidelineFromStart(0.1f)
        val passEnd = createGuidelineFromStart(0.9f)
        val passTop = createGuidelineFromTop(0.80f)
        val passBottom = createGuidelineFromTop(0.88f)



        AcceptButton(
            text = "Filtruj",
            onClick = {

            },
            modifier = Modifier
                .constrainAs(acceptButton) {
                    top.linkTo(passTop)
                    bottom.linkTo(passBottom)
                    start.linkTo(passStart)
                    end.linkTo(passEnd)
                    height = Dimension.fillToConstraints
                    width = Dimension.fillToConstraints
                }
        )

        val resetStart = createGuidelineFromStart(0.35f)
        val resetEnd = createGuidelineFromStart(0.65f)
        val resetTop = createGuidelineFromTop(0.89f)
        val resetBottom = createGuidelineFromTop(0.94f)

        ClickableResetTextComponent(
            modifier = Modifier
                .constrainAs(reset) {
                    top.linkTo(resetTop)
                    bottom.linkTo(resetBottom)
                    start.linkTo(resetStart)
                    end.linkTo(resetEnd)
                    height = Dimension.fillToConstraints
                    width = Dimension.fillToConstraints

                },
            navController = navController
        )
    }
}
@Composable
fun AcceptButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val hapticFeedback = LocalHapticFeedback.current
    Button(
        onClick = {
            hapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)
            onClick()
        },
        modifier = modifier,
//            .padding(vertical = 20.dp, horizontal = 0.dp)
//            .fillMaxWidth()
//            .height(56.dp)
//            .background(color = Color(0xFF007BFF), shape = RoundedCornerShape(size = 70.dp)),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color(0xff4fc3f7), // Button background color
            contentColor = Color.Black // Button text color
        )
    ) {
        Text(
            text = text,
            style = TextStyle(
                fontSize = 20.sp,
                fontFamily = FontFamily(Font(R.font.proximanovabold)),
                fontWeight = FontWeight.Bold,
                color = Color(0xFF003366), // Button text color
                textAlign = TextAlign.Center,
                letterSpacing = 1.sp
            )
        )
    }
}
@Composable
fun ClickableResetTextComponent(modifier: Modifier = Modifier, navController: NavController) {
    val viewModel: LoginViewModel = viewModel()
    val context = LocalContext.current
    val loginText = "Resetuj"
    val annotatedString = buildAnnotatedString {
        pushStringAnnotation(tag = "Resetuj", annotation = loginText)
        withStyle(style = SpanStyle(color = Color(0xffe0e0e0), fontFamily =
        FontFamily(Font(R.font.proximanovabold)), fontWeight = FontWeight(900) )
        ) {
            append(loginText)
        }
        pop()
    }

    Box(
        modifier = modifier
            .border(1.dp, Color.Black) // Black outline with 1dp width
            .fillMaxWidth() // Make the box fill the available width
            .padding(8.dp), // Optional padding for aesthetics
        contentAlignment = Alignment.Center // Center the content (text) within the box
    ) {
        ClickableText(
            text = annotatedString,
            modifier = Modifier,
            style = TextStyle(textAlign = TextAlign.Center), // Center text horizontally
            onClick = {

            }
        )
    }
}

