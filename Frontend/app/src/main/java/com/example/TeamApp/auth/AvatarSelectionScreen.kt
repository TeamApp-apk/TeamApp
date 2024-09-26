package com.example.TeamApp.auth

import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.navOptions
import com.example.TeamApp.excludedUI.AvatarSelection
import com.example.ui.theme.textInUpperBoxForgotPassword

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun AvatarSelectionScreen(navController: NavController) {
    val viewModel: LoginViewModel = LoginViewModelProvider.loginViewModel
    val avatars by viewModel.avatars.observeAsState(emptyList())
    val selectedAvatarIndex by viewModel.selectedAvatarIndex.observeAsState(0)
    LaunchedEffect(Unit){
        Log.d("AvatarSelection", "avatar selection")
    }

    val gradientColors = listOf(
        Color(0xFFE8E8E8),
        Color(0xFF007BFF),
    )

    ConstraintLayout(modifier = Modifier.fillMaxSize()
        .background(brush = Brush.linearGradient(colors = gradientColors)).fillMaxSize()) {
        val (avatar) = createRefs()

        val avatarStart = createGuidelineFromStart(0f)
        val avatarEnd = createGuidelineFromStart(1f)
        val avatarTop = createGuidelineFromTop(0.2f)
        val avatarBottom = createGuidelineFromTop(0.70f)

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .constrainAs(avatar)
                {
                    top.linkTo(avatarTop)
                    bottom.linkTo(avatarBottom)
                    start.linkTo(avatarStart)
                    end.linkTo(avatarEnd)
                    height = Dimension.fillToConstraints
                    width = Dimension.fillToConstraints
                }


        ) {
            Text(
                textAlign = TextAlign.Center,
                style = textInUpperBoxForgotPassword,
                text = "Wybierz swój awatar ${viewModel.user.value!!.name}",
                modifier = Modifier.padding(16.dp)

            )
            AvatarSelection(navController)
        }
    }


}