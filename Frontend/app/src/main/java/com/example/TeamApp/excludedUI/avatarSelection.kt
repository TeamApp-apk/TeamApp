package com.example.TeamApp.excludedUI

import android.content.Intent
import android.util.Log
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import coil.imageLoader
import coil.request.CachePolicy
import coil.request.ImageRequest
import com.example.TeamApp.MainAppActivity
import com.example.TeamApp.auth.LoginViewModel
import com.example.TeamApp.auth.LoginViewModelProvider
import kotlinx.coroutines.delay


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun AvatarSelection(navController: NavController) {
    val viewModel: LoginViewModel = LoginViewModelProvider.loginViewModel
    val avatars by viewModel.avatars.observeAsState(emptyList())
    val listState = rememberLazyListState()
    val flingBehavior = rememberSnapFlingBehavior(lazyListState = listState)
    val screenWidth = LocalConfiguration.current.screenWidthDp.dp
    val avatarSize = 150.dp // Size of your avatar items
    val contentPadding = (screenWidth - avatarSize * 3) / 2
    val visibleItemsCount = 3
    val visibleItemsMiddle = visibleItemsCount / 2
    val listScrollCount = Integer.MAX_VALUE
    val listScrollMiddle = listScrollCount / 2
    val listStartIndex = listScrollMiddle - listScrollMiddle % avatars.size - visibleItemsMiddle
    var isScrolledToStart by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val hapticFeedback = LocalHapticFeedback.current

    val preloadedAvatars = remember { mutableListOf<ImageRequest>() }
    // State for loading indicator
    var isLoading by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        listState.scrollToItem(listStartIndex)
        listState.scroll {
            with(flingBehavior) {
                performFling(1000f)
            }
        }
    }
    LaunchedEffect(avatars) {
//        if (isLoading) {
//            val imageLoader = context.imageLoader
//
//            avatars.forEach { avatar ->
//                val request = ImageRequest.Builder(context)
//                    .data(avatar.faceUrl)
//                    .memoryCachePolicy(CachePolicy.ENABLED) // Use memory cache
//                    .diskCachePolicy(CachePolicy.ENABLED)   // Use disk cache
//                    .build()
//
//                // Load each avatar into memory cache
//                imageLoader.enqueue(request)
//            }
//            isLoading = false
//        }
        delay(200)
        isLoading = false
    }

    val selectedAvatarIndex = remember { mutableStateOf(0) }

    LaunchedEffect(listState) {
        snapshotFlow { listState.firstVisibleItemIndex }
            .collect { index ->
                val newIndex = (index + visibleItemsMiddle) % avatars.size
                if (newIndex != selectedAvatarIndex.value) {
                    // Trigger haptic feedback when the avatar in the middle changes
                    hapticFeedback.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                    selectedAvatarIndex.value = newIndex
                }
            }
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth()
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        // Show CircularProgressIndicator if loading
        if (isLoading) {
            CircularProgressIndicator() // Show loading indicator
        } else {
            LazyRow(
                state = listState,
                flingBehavior = flingBehavior,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp),
                horizontalArrangement = Arrangement.Center,
                contentPadding = PaddingValues(horizontal = 16.dp)
            ) {
                items(if (avatars.isNotEmpty()) Integer.MAX_VALUE else 0) { index ->
                    val avatar = avatars[index % avatars.size]
                    val isCenter = (index % avatars.size) == selectedAvatarIndex.value
                    AvatarFaceItem(
                        faceUrl = avatar.faceUrl,
                        isCenter = isCenter,
                        onClick = {}
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(40.dp))
        Button(onClick = {
            val selectedAvatar = avatars.getOrNull(selectedAvatarIndex.value)
            if (selectedAvatar != null) {
                viewModel.saveSelectedAvatar(selectedAvatar.avatarUrl)
                Log.d("AvatarSelection", "Selected avatar: $selectedAvatar")
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
fun AvatarFaceItem(
    faceUrl: String,
    isCenter: Boolean,
    onClick: () -> Unit
) {
    val offsetY by animateDpAsState(targetValue = if (isCenter) 40.dp else 0.dp)

    Image(
        painter = rememberImagePainter(faceUrl),
        contentDescription = null,
        modifier = Modifier
            .size(150.dp)
            .offset(y = offsetY)
            .clickable(onClick = onClick)
    )
}