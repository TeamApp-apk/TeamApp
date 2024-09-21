package com.example.TeamApp.excludedUI

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter
import com.example.TeamApp.auth.LoginViewModel

@Composable
fun AvatarSelection(viewModel: LoginViewModel) {
    // Observe avatars and selected avatar
    val avatars by viewModel.avatars.observeAsState(emptyList())
    val selectedAvatarIndex by viewModel.selectedAvatarIndex.observeAsState(0)

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

        // Button to load more avatars
        Button(onClick = { viewModel.loadMoreAvatars() }) {
            Text("Load more avatars")
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

