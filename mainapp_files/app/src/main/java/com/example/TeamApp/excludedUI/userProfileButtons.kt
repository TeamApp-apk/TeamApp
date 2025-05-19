package com.example.TeamApp.excludedUI

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.PointerIcon.Companion.Text
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.navigation.NavController
import com.example.TeamApp.R

@Composable
fun UserProfileButton(iconId: Int,
                      mainText: String,
                      bottomText: String,
                      navController: NavController,
                      route: String,
                      modifier: Modifier){
    ConstraintLayout (
        modifier = modifier
            .fillMaxSize()
            .clickable { navController.navigate(route) }
    )
    {
        val (icon, topText, sideText, arrow) = createRefs()

        val iconStart = createGuidelineFromStart(0.05f)
        val iconTop = createGuidelineFromTop(0.25f)

        Image(
            painter = painterResource(id = iconId),
            contentDescription = "Activity Icon",
            modifier = Modifier
                .size(32.dp)
                .constrainAs(icon) {
                    top.linkTo(iconTop)
                    start.linkTo(iconStart)
                }
        )

        val topTextStart = createGuidelineFromStart(0.17f)
        val topTextTop = createGuidelineFromTop(0.2f)
        val topTextBottom = createGuidelineFromTop(0.5f)
        Text(
            modifier = Modifier
                .constrainAs(topText) {
                    top.linkTo(topTextTop)
                    bottom.linkTo(topTextBottom)
                    start.linkTo(topTextStart)
                },
            text = mainText,
            style = TextStyle(
                fontSize = 24.sp,
                fontFamily = FontFamily(Font(R.font.proximanovabold)),
                fontWeight = FontWeight(200),
                color = Variables.Black,
                textAlign = TextAlign.Center,
            )
        )

        val sideTextStart = createGuidelineFromStart(0.17f)
        val sideTextTop = createGuidelineFromTop(0.5f)
        val sideTextBottom = createGuidelineFromTop(0.9f)

        Text(
            modifier = Modifier
                .constrainAs(sideText) {
                    top.linkTo(sideTextTop)
                    bottom.linkTo(sideTextBottom)
                    start.linkTo(sideTextStart)
                },
            text = bottomText,
            style = TextStyle(
                fontSize = 14.sp,
                fontFamily = FontFamily(Font(R.font.proximanovaregular)),
                fontWeight = FontWeight(500),
                color = Color(0xFF979797),
                textAlign = TextAlign.Center,
            )
        )


        val arrowStart = createGuidelineFromStart(0.85f)
        val arrowTop = createGuidelineFromTop(0.25f)

        Image(
            painter = painterResource(id = R.drawable.arrowdownicon),
            contentDescription = "Activity Icon",
            modifier = Modifier
                .size(32.dp)
                .constrainAs(arrow) {
                    top.linkTo(arrowTop)
                    start.linkTo(arrowStart)
                }
        )

    }
}


//This version of the button on the bottom of profile screen has rounded down corners when hovered.
@Composable
fun UserProfileButtonBottom(iconId: Int,
                            mainText: String,
                            bottomText: String,
                            navController: NavController,
                            route: String,
                            modifier: Modifier){
    ConstraintLayout (
        modifier = modifier
            .fillMaxSize()
            .clip(RoundedCornerShape(bottomStart = 16.dp, bottomEnd = 16.dp))
            .clickable { navController.navigate(route) }
    )
    {
        val (icon, topText, sideText, arrow) = createRefs()

        val iconStart = createGuidelineFromStart(0.05f)
        val iconTop = createGuidelineFromTop(0.25f)

        Image(
            painter = painterResource(id = iconId),
            contentDescription = "Activity Icon",
            modifier = Modifier
                .size(32.dp)
                .constrainAs(icon) {
                    top.linkTo(iconTop)
                    start.linkTo(iconStart)
                }
        )

        val topTextStart = createGuidelineFromStart(0.17f)
        val topTextTop = createGuidelineFromTop(0.2f)
        val topTextBottom = createGuidelineFromTop(0.5f)
        Text(
            modifier = Modifier
                .constrainAs(topText) {
                    top.linkTo(topTextTop)
                    bottom.linkTo(topTextBottom)
                    start.linkTo(topTextStart)
                },
            text = mainText,
            style = TextStyle(
                fontSize = 24.sp,
                fontFamily = FontFamily(Font(R.font.proximanovabold)),
                fontWeight = FontWeight(200),
                color = Variables.Black,
                textAlign = TextAlign.Center,
            )
        )

        val sideTextStart = createGuidelineFromStart(0.17f)
        val sideTextTop = createGuidelineFromTop(0.5f)
        val sideTextBottom = createGuidelineFromTop(0.9f)

        Text(
            modifier = Modifier
                .constrainAs(sideText) {
                    top.linkTo(sideTextTop)
                    bottom.linkTo(sideTextBottom)
                    start.linkTo(sideTextStart)
                },
            text = bottomText,
            style = TextStyle(
                fontSize = 14.sp,
                fontFamily = FontFamily(Font(R.font.proximanovaregular)),
                fontWeight = FontWeight(500),
                color = Color(0xFF979797),
                textAlign = TextAlign.Center,
            )
        )


        val arrowStart = createGuidelineFromStart(0.85f)
        val arrowTop = createGuidelineFromTop(0.25f)

        Image(
            painter = painterResource(id = R.drawable.arrowdownicon),
            contentDescription = "Activity Icon",
            modifier = Modifier
                .size(32.dp)
                .constrainAs(arrow) {
                    top.linkTo(arrowTop)
                    start.linkTo(arrowStart)
                }
        )

    }
}

//@Preview
//@Composable
//fun previewUserProfileButton(){
//    UserProfileButton(iconId = R.drawable.mapicon, mainText = "Twoje wydarzenia", bottomText = "Zarządzaj swoimi wydarzeniami")
//}