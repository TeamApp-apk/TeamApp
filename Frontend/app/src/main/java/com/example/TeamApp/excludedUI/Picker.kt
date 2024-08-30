package com.example.TeamApp.excludedUI

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Divider
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.CompositingStrategy
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun Picker(
    items: List<String>,
    state: PickerState = rememberPickerState(),
    modifier: Modifier = Modifier,
    startIndex: Int = 0,
    visibleItemsCount: Int = 3,
    textModifier: Modifier = Modifier,
    textStyle: TextStyle = LocalTextStyle.current,
    dividerColor: Color = LocalContentColor.current,
    onScrollToIndex: (Int) -> Unit = {}, // Add a parameter for the scroll function
    loop: Boolean = true
) {

    val visibleItemsMiddle = visibleItemsCount / 2
    val listScrollCount = Integer.MAX_VALUE
    val listScrollMiddle = listScrollCount / 2
    val listStartIndex = if (loop) {
        listScrollMiddle - listScrollMiddle % items.size - visibleItemsMiddle + startIndex
    }
    else
    {
        startIndex
    }

    val paddedItems = if (loop) {
        items
    } else {
        // Add empty strings to the start and end of the list to ensure proper padding
        val emptyItems = List(visibleItemsMiddle) { "" }
        emptyItems + items + emptyItems
    }

    fun getItem(index: Int) = if (loop) {
        items[index % items.size]
    } else {
        paddedItems[index % paddedItems.size]
    }

    val listState = rememberLazyListState(initialFirstVisibleItemIndex = listStartIndex)
    val flingBehavior = rememberSnapFlingBehavior(lazyListState = listState)

    val itemHeightPixels = remember { mutableStateOf(0) }
    val itemHeightDp = pixelsToDp(itemHeightPixels.value)

    val fadingEdgeGradient = remember {
        Brush.verticalGradient(
            0f to Color.Transparent,
            0.5f to Color.Black,
            1f to Color.Transparent
        )
    }

    LaunchedEffect(listState) {
        if (loop)
        {
            snapshotFlow { listState.firstVisibleItemIndex }
                .map { index -> (index + visibleItemsMiddle) % items.size}
                .distinctUntilChanged()
                .collect { index -> state.selectedIndex = index }
        }
        else
        {
            snapshotFlow { listState.firstVisibleItemIndex }
                .map { index ->
                    (index - visibleItemsMiddle + 1).coerceIn(0, items.size - 1)
                }
                .distinctUntilChanged()
                .collect { index -> state.selectedIndex = index }
        }
    }


    Box(modifier = modifier) {

        LazyColumn(
            state = listState,
            flingBehavior = flingBehavior,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .height(itemHeightDp * visibleItemsCount)
                .fadingEdge(fadingEdgeGradient)
        ) {
            items(if (loop) Integer.MAX_VALUE else paddedItems.size) { index ->
                Text(
                    text = getItem(index),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    style = textStyle,
                    modifier = Modifier
                        .onSizeChanged { size -> itemHeightPixels.value = size.height }
                        .then(textModifier)
                )
            }
        }

        Divider(
            color = dividerColor,
            modifier = Modifier.offset(y = itemHeightDp * visibleItemsMiddle)
        )

        Divider(
            color = dividerColor,
            modifier = Modifier.offset(y = itemHeightDp * (visibleItemsMiddle + 1))
        )

    }

}

private fun Modifier.fadingEdge(brush: Brush) = this
    .graphicsLayer(compositingStrategy = CompositingStrategy.Offscreen)
    .drawWithContent {
        drawContent()
        drawRect(brush = brush, blendMode = BlendMode.DstIn)
    }

@Composable
private fun pixelsToDp(pixels: Int) = with(LocalDensity.current) { pixels.toDp() }