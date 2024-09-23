package com.example.TeamApp.excludedUI

import android.content.Intent
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import com.example.TeamApp.MainAppActivity
import com.example.TeamApp.auth.LoginViewModel
import androidx.lifecycle.viewmodel.compose.viewModel as viewModel

@Composable
fun AvatarSelection(navController: NavController) {
    val viewModel : LoginViewModel = viewModel()
    // Observe avatars and selected avatar
    val avatars by viewModel.avatars.observeAsState(emptyList())
    val selectedAvatarIndex by viewModel.selectedAvatarIndex.observeAsState(0)
    val listState = rememberLazyListState()
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth()
    ) {
        // Display selected avatar (the one in the center)
        avatars.getOrNull(selectedAvatarIndex)?.let { avatar ->
            DisplaySelectedAvatar(avatar.avatarUrl)
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Avatar carousel showing only 3 avatars with the center one enlarged
        LazyRow(
            state = listState,
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp),
            horizontalArrangement = Arrangement.Center,
            contentPadding = PaddingValues(horizontal = 16.dp)
        ) {
            itemsIndexed(avatars) { index, avatar ->
                val isCenter = index == selectedAvatarIndex
                val scale = if (isCenter) 2f else 1.5f // Enlarge the center avatar

                AvatarFaceItem(
                    faceUrl = avatar.faceUrl,
                    scale = scale,
                    onClick = {
                        // Handle avatar click to shift the selection to the center
                        viewModel.selectAvatarByIndex(index)

                    }
                )
            }
        }

        val shouldLoadMore = remember {
            derivedStateOf {
                val visibleItems = listState.layoutInfo.visibleItemsInfo
                if (visibleItems.isNotEmpty()) {
                    val firstVisibleIndex = visibleItems.firstOrNull()?.index ?: 0
                    firstVisibleIndex == 0
                } else {
                    false
                }
            }
        }

        // Trigger loading more avatars when user scrolls close to the end
        LaunchedEffect(shouldLoadMore.value) {
            if (shouldLoadMore.value) {
                viewModel.loadAvatars(25)
            }
        }

        Button(onClick = {
            val selectedAvatar = avatars.getOrNull(selectedAvatarIndex)
            if (selectedAvatar != null) {
                viewModel.saveSelectedAvatar(selectedAvatar.avatarUrl)
                    val context = navController.context
                    val intent = Intent(context, MainAppActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    context.startActivity(intent)
            }
        }) {
            Text("Save Avatar")
        }
    }
}

@Composable
fun DisplaySelectedAvatar(avatarUrl: String) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxWidth()
            .height(250.dp) // Adjust display size as needed
    ) {
        Image(
            painter = rememberImagePainter(avatarUrl),
            contentDescription = null,
            modifier = Modifier
                .size(200.dp) // Adjust image size
        )
    }
}

@Composable
fun AvatarFaceItem(
    faceUrl: String,
    scale: Float, // Scale to handle enlargement
    onClick: () -> Unit
) {
    Image(
        painter = rememberImagePainter(faceUrl),
        contentDescription = null,
        modifier = Modifier
            .size(80.dp * scale) // Adjust size based on the scale
            .clickable(onClick = onClick)
    )
}

