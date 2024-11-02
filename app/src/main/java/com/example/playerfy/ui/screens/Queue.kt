package com.example.playerfy.ui.screens

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex

@Composable
fun Queue(show: Boolean, close: () -> Unit) {

    val screenHeight = LocalConfiguration.current.screenHeightDp.dp

    var offsetY by remember { mutableStateOf(0f) }

    val offset by animateDpAsState(
        targetValue = if (show) 0.dp else screenHeight + 100.dp,
        animationSpec = tween(durationMillis = 300)
    )

    Box (
        modifier = Modifier
            .offset(y = offset + offsetY.dp)
            .fillMaxSize()
            .clickable {

            }
            .background(Color(0xFF121212))
            .zIndex(2f)
    ) {
        PlayerHeader(folderName = "current", collapse = close, color = Color(0xFF121212))

    }
}