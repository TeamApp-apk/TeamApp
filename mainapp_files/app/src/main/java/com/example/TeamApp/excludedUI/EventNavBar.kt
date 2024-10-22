import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.TeamApp.R
import androidx.compose.foundation.clickable
import androidx.compose.runtime.*
import com.airbnb.lottie.compose.*

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box

@Composable
fun BottomNavBar(navController: NavController) {
    val currentDestination = navController.currentBackStackEntryAsState().value?.destination?.route

    var isPlaying by remember { mutableStateOf(false) }
    var isPlusPlaying by remember { mutableStateOf(false) }
    var isProfilePlaying by remember { mutableStateOf(false) }
    var isChatPlaying by remember { mutableStateOf(false) }

    val searchComposition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.search))
    val searchProgress by animateLottieCompositionAsState(
        composition = searchComposition,
        isPlaying = isPlaying,
        iterations = 1,
        speed = 0f
    )

    val plusComposition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.plus))
    val plusProgress by animateLottieCompositionAsState(
        composition = plusComposition,
        isPlaying = isPlusPlaying,
        iterations = 1,
        speed = 0f
    )

    val profileComposition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.profile))
    val profileProgress by animateLottieCompositionAsState(
        composition = profileComposition,
        isPlaying = isProfilePlaying,
        iterations = 1,
        speed = 0f
    )

    val chatComposition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.chat))
    val chatProgress by animateLottieCompositionAsState(
        composition = chatComposition,
        isPlaying = isChatPlaying,
        iterations = 1,
        speed = 0f
    )

    LaunchedEffect(searchProgress, plusProgress, profileProgress) {
        if (searchProgress >=0f) isPlaying = false
        if (plusProgress >= 0f) isPlusPlaying = false
        if (profileProgress >= 0f) isProfilePlaying = false
    }

    Row(
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp)
            .background(color = Color(0xFFFFFFFF))
            .navigationBarsPadding()
            .padding(horizontal = 30.dp, vertical = 8.dp)
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .height(40.dp)
        ) {

            Box(
                modifier = Modifier
                    .size(50.dp)
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null,
                        onClick = {
                            if (currentDestination != "search") {
                                navController.navigate("search") {
                                    popUpTo("search") { inclusive = true }
                                }
                                isPlaying = true
                            }
                        }
                    )
                    .background(
                        color = if (currentDestination == "search") Color.LightGray else Color.Transparent,
                        shape = RoundedCornerShape(13.dp)
                    )
            ) {
                LottieAnimation(
                    composition = searchComposition,
                    progress = { searchProgress },
                    modifier = Modifier.fillMaxSize()
                )
            }
            Box(
                modifier = Modifier
                    .size(50.dp)
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null,
                        onClick = {
                            if (currentDestination != "createEvent") {
                                navController.navigate("createEvent") {
                                    popUpTo("createEvent") { inclusive = true }
                                }
                                isPlusPlaying = true
                            }
                        }
                    )
                    .background(
                        color = if (currentDestination == "createEvent") Color.LightGray else Color.Transparent,
                        shape = RoundedCornerShape(13.dp)
                    )
            ) {
                LottieAnimation(
                    composition = plusComposition,
                    progress = { plusProgress },
                    modifier = Modifier.fillMaxSize()
                )
            }

            Box(
                modifier = Modifier
                    .size(50.dp)
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null,
                        //x do zmiany, gdy bedzie ui czatu
                        onClick = {
                            if (currentDestination != "x") {
                                navController.navigate("x") {
                                    popUpTo("x") { inclusive = true }
                                }
                                isPlusPlaying = true
                            }
                        }
                    )
                    .background(
                        color = if (currentDestination == "x") Color.LightGray else Color.Transparent,
                        shape = RoundedCornerShape(13.dp)
                    )
            ) {
                LottieAnimation(
                    composition = chatComposition,
                    progress = { chatProgress },
                    modifier = Modifier.fillMaxSize()
                )
            }


            Box(
                modifier = Modifier
                    .size(50.dp)
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null,
                        onClick = {
                            if (currentDestination != "profile") {
                                navController.navigate("profile") {
                                    popUpTo("profile") { inclusive = true }
                                }
                                isProfilePlaying = true
                            }
                        }
                    )
                    .background(
                        color = if (currentDestination == "profile") Color.LightGray else Color.Transparent,
                        shape = RoundedCornerShape(13.dp)
                    )
            ) {
                LottieAnimation(
                    composition = profileComposition,
                    progress = { profileProgress },
                    modifier = Modifier.fillMaxSize()
                )
            }


        }
    }
}



