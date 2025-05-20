package com.example.TeamApp.profile

import UserViewModel
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import coil.compose.rememberImagePainter
import coil.request.ImageRequest
import com.example.TeamApp.R
import com.example.TeamApp.excludedUI.UserProfileButton
import com.example.TeamApp.excludedUI.UserProfileButtonBottom
import com.example.TeamApp.excludedUI.Variables

@Composable
fun ProfileScreen(navController: NavController, vieModel:UserViewModel) {
    val context = LocalContext.current
    val user by vieModel.user.observeAsState()
    val gradientColors = listOf(
        Color(0xFFE8E8E8),
        Color(0xFF007BFF),
    )
    user?.let { userData ->
        val avatarUrl = user!!.avatar
        Box(modifier = Modifier
            .fillMaxSize()
            .background(brush = Brush.linearGradient(colors = gradientColors))
        ){}

        ConstraintLayout(modifier = Modifier
            .fillMaxSize()
            ) {
            val (whiteBox) = createRefs()

            val boxTop = createGuidelineFromTop(0.05f)
            val boxBottom = createGuidelineFromTop(0.95f)
            val boxStart = createGuidelineFromStart(0.05f)
            val boxEnd = createGuidelineFromStart(0.95f)

            Box(modifier = Modifier
                .constrainAs(whiteBox) {
                    top.linkTo(boxTop)
                    bottom.linkTo(boxBottom)
                    start.linkTo(boxStart)
                    end.linkTo(boxEnd)
                    height = Dimension.fillToConstraints
                    width = Dimension.fillToConstraints
                }
                .background(color = Color.White, shape = RoundedCornerShape(16.dp))
            ){
                ConstraintLayout(modifier = Modifier
                    .fillMaxSize()
                ) {
                    val (avatar, welcomeText, editButton, events, settings, contactUs, share,
                        divider0, divider1, divider2, divider3 ) = createRefs()

                    val maxStart = createGuidelineFromStart(0f)
                    val maxEnd = createGuidelineFromStart(1f)
                    val avatarStart = createGuidelineFromStart(0.2f)
                    val avatarEnd = createGuidelineFromStart(0.8f)
                    val avatarTop = createGuidelineFromTop(0.03f)
                    val avatarBottom = createGuidelineFromTop(0.28f)
                    AsyncImage(
                        model = ImageRequest.Builder(LocalContext.current) //O(1) request thanks to caching an image
                            .data(avatarUrl)
                            .crossfade(true)
                            .build(),
                        contentDescription = "Profile picture",
                        placeholder = painterResource(id = R.drawable.eye),
                        modifier = Modifier
                            .constrainAs(avatar) {
                                top.linkTo(avatarTop)
                                bottom.linkTo(avatarBottom)
                                start.linkTo(avatarStart)
                                end.linkTo(avatarEnd)
                                width = Dimension.fillToConstraints
                                height = width
                            }
                    )


                    val welcomeTextStart = createGuidelineFromStart(0.1f)
                    val welcomeTextEnd = createGuidelineFromStart(0.9f)
                    val welcomeTextTop = createGuidelineFromTop(0.32f)
                    val welcomeTextBottom = createGuidelineFromTop(0.37f)
                    Text(
                        text = "Witaj ${userData.name}!",
                        style = TextStyle(
                            fontSize = 24.sp,
                            fontFamily = FontFamily(Font(R.font.proximanovaregular)),
                            fontWeight = FontWeight(700),
                            color = Variables.Black,
                            textAlign = TextAlign.Center,
                        ),
                        modifier = Modifier
                            .constrainAs(welcomeText) {
                                top.linkTo(welcomeTextTop)
                                bottom.linkTo(welcomeTextBottom)
                                start.linkTo(welcomeTextStart)
                                end.linkTo(welcomeTextEnd)
                                width = Dimension.fillToConstraints
                                height = Dimension.fillToConstraints
                            }
                    )

                    val editButtonStart = createGuidelineFromStart(0.1f)
                    val editButtonEnd = createGuidelineFromStart(0.9f)
                    val editButtonTop = createGuidelineFromTop(0.4f)
                    val editButtonBottom = createGuidelineFromTop(0.5f)
                    Button(
                        onClick = { /*TODO*/ },
                        modifier = Modifier
                            .constrainAs(editButton) {
                                top.linkTo(editButtonTop)
                                bottom.linkTo(editButtonBottom)
                                start.linkTo(editButtonStart)
                                end.linkTo(editButtonEnd)
                                width = Dimension.fillToConstraints
                                height = Dimension.fillToConstraints
                            },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFFF2F2F2)
                        ),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Text(
                            text = "Edytuj profil",
                            style = TextStyle(
                                fontSize = 24.sp,
                                fontFamily = FontFamily(Font(R.font.proximanovaregular)),
                                fontWeight = FontWeight(600),
                                color = Variables.Black,
                                textAlign = TextAlign.Center,
                            )
                        )
                    }

                    val eventsTop = createGuidelineFromTop(0.56f)
                    val eventsBottom = createGuidelineFromTop(0.67f)
                    HorizontalDivider(
                        modifier = Modifier
                            .constrainAs(divider0) {
                                top.linkTo(eventsTop)
                                bottom.linkTo(eventsTop)
                                start.linkTo(maxStart)
                                end.linkTo(maxEnd)
                                width = Dimension.fillToConstraints
                                height = Dimension.value(1.dp)
                            },
                        thickness = 1.dp,
                        color = Color(0xFFD9D9D9)
                    )
                    UserProfileButton(iconId = R.drawable.mapicon,
                        mainText = "Twoje wydarzenia",
                        bottomText = "Zarządzaj swoimi wydarzeniami",
                        navController,
                        "yourEvents",
                        modifier = Modifier
                            .constrainAs(events) {
                                top.linkTo(eventsTop)
                                bottom.linkTo(eventsBottom)
                                start.linkTo(maxStart)
                                end.linkTo(maxEnd)
                                width = Dimension.fillToConstraints
                                height = Dimension.fillToConstraints
                            }
                            .background(color = Color(0xFFF2F2F2))
                    )
                    HorizontalDivider(
                        modifier = Modifier
                            .constrainAs(divider1) {
                                top.linkTo(eventsBottom)
                                bottom.linkTo(eventsBottom)
                                start.linkTo(maxStart)
                                end.linkTo(maxEnd)
                                width = Dimension.fillToConstraints
                                height = Dimension.value(1.dp)
                            },
                        thickness = 1.dp,
                        color = Color(0xFFD9D9D9)
                    )

                    val settingsTop = createGuidelineFromTop(0.67f)
                    val settingsBottom = createGuidelineFromTop(0.78f)
                    UserProfileButton(iconId = R.drawable.settingsicon,
                        mainText = "Ustawienia",
                        bottomText = "Ustawienia konta",
                        navController,
                        "settings",
                        modifier = Modifier
                            .constrainAs(settings) {
                                top.linkTo(settingsTop)
                                bottom.linkTo(settingsBottom)
                                start.linkTo(maxStart)
                                end.linkTo(maxEnd)
                                width = Dimension.fillToConstraints
                                height = Dimension.fillToConstraints
                            }
                            .background(color = Color(0xFFF2F2F2))
                    )
                    HorizontalDivider(
                        modifier = Modifier
                            .constrainAs(divider2) {
                                top.linkTo(settingsBottom)
                                bottom.linkTo(settingsBottom)
                                start.linkTo(maxStart)
                                end.linkTo(maxEnd)
                                width = Dimension.fillToConstraints
                                height = Dimension.value(1.dp)
                            },
                        thickness = 1.dp,
                        color = Color(0xFFD9D9D9)
                    )

                    val contactUsTop = createGuidelineFromTop(0.78f)
                    val contactUsBottom = createGuidelineFromTop(0.89f)
                    UserProfileButton(iconId = R.drawable.contactusicon,
                        mainText = "Napisz do nas",
                        bottomText = "Skontaktuj się z nami",
                        navController,
                        "sendUsMessage",
                        modifier = Modifier
                            .constrainAs(contactUs) {
                                top.linkTo(contactUsTop)
                                bottom.linkTo(contactUsBottom)
                                start.linkTo(maxStart)
                                end.linkTo(maxEnd)
                                width = Dimension.fillToConstraints
                                height = Dimension.fillToConstraints
                            }
                            .background(color = Color(0xFFF2F2F2))
                    )
                    HorizontalDivider(
                        modifier = Modifier
                            .constrainAs(divider3) {
                                top.linkTo(contactUsBottom)
                                bottom.linkTo(contactUsBottom)
                                start.linkTo(maxStart)
                                end.linkTo(maxEnd)
                                width = Dimension.fillToConstraints
                                height = Dimension.value(1.dp)
                            },
                        thickness = 1.dp,
                        color = Color(0xFFD9D9D9)
                    )

                    val shareTop = createGuidelineFromTop(0.89f)
                    val shareBottom = createGuidelineFromTop(1f)
                    UserProfileButtonBottom(iconId = R.drawable.shareicon,
                        mainText = "Udostępnij",
                        bottomText = "Niech wszyscy wiedzą",
                        navController,
                        "",
                        modifier = Modifier
                            .constrainAs(share) {
                                top.linkTo(shareTop)
                                bottom.linkTo(shareBottom)
                                start.linkTo(maxStart)
                                end.linkTo(maxEnd)
                                width = Dimension.fillToConstraints
                                height = Dimension.fillToConstraints
                            }
                            .background(color = Color(0xFFF2F2F2), shape = RoundedCornerShape(16.dp))
                    )
                }
            }
            }
        /*
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 32.dp)
                    .background(
                        Color(0xFFF2F2F2),
                        shape = RoundedCornerShape(bottomStart = 16.dp, bottomEnd = 16.dp)
                    )
            ){
                Column(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.Start
                ){
                    UserProfileButton(iconId = R.drawable.mapicon, mainText = "Twoje wydarzenia", bottomText = "Zarządzaj swoimi wydarzeniami", navController, "yourEvents")
                    HorizontalDivider(
                        modifier = Modifier
                            .padding(horizontal = 8.dp)
                            .fillMaxWidth(),
                        thickness = 1.dp,
                        color = Color(0xFFD9D9D9)
                    )
                    UserProfileButton(iconId = R.drawable.settingsicon, mainText = "Ustawienia", bottomText = "Ustawienia konta",navController, "settings")
                    HorizontalDivider(
                        modifier = Modifier
                            .padding(horizontal = 8.dp)
                            .fillMaxWidth(),
                        thickness = 1.dp,
                        color = Color(0xFFD9D9D9)
                    )
                    UserProfileButton(iconId = R.drawable.contactusicon, mainText = "Napisz do nas", bottomText = "Skontaktuj się z nami", navController, "sendUsMessage")
                    HorizontalDivider(
                        modifier = Modifier
                            .padding(horizontal = 8.dp)
                            .fillMaxWidth(),
                        thickness = 1.dp,
                        color = Color(0xFFD9D9D9)
                    )
                    UserProfileButton(iconId = R.drawable.shareicon, mainText = "Udostępnij", bottomText = "Niech wszyscy wiedzą", navController, "")
                }
            }
        }*/
    } ?: run {
        // Wyświetl coś, gdy użytkownik jest null
        Text(text = "Nie znaleziono użytkownika!")
        UserProfileButton(iconId = R.drawable.contactusicon,
            mainText = "Napisz do nas",
            bottomText = "Skontaktuj się z nami",
            navController,
            "settings",
            modifier = Modifier
                .size(150.dp)
        )
    }


}

