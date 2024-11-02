package com.example.playerfy.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.LayoutCoordinates
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.unit.dp
import kotlin.math.max
import kotlin.math.min

@Composable
fun SongProgressBar(modifier: Modifier = Modifier, prog: Float) {
    var progress by remember { mutableStateOf(prog) }
    var boxWidth by remember { mutableStateOf(0f) }
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .onGloballyPositioned { coordinates: LayoutCoordinates ->
                boxWidth = coordinates.size.width.toFloat() // Capture the width of the box
            }
            .pointerInput(Unit) {
                // Detect the click and calculate progress
                detectTapGestures { offset ->
                    // Calculate the new progress based on the click position
                    val newProgress = min(maxOf(0f, offset.x / size.width), 1f) // Ensure progress is between 0 and 1
                    progress = newProgress
                }
            }.then(modifier),
    ) {
        LinearProgressIndicator(
            progress = {
                progress // Value between 0f and 1f
            },
            modifier = Modifier
                .clip(RoundedCornerShape(5.dp))
                .fillMaxWidth()
                .height(3.dp)
                .align(Alignment.Center)
                .pointerInput(Unit) {
                    // Detect the click and calculate progress
                    detectDragGestures { change, dragAmount ->
                        // Calculate the new progress based on the drag amount
                        change.consume() // Consume the drag change to indicate it has been handled
                        val newProgress = min(max(progress + dragAmount.x / size.width, 0f), 1f)
                        progress = newProgress
                    }
                }, // Adjust height as needed
            color = Color.White,
            trackColor = Color(128, 128, 128, 179),
        )

        Box(
            modifier = Modifier
                .offset(x = (boxWidth * .5 * progress).dp - 4.dp) // Center the circle on the progress
                .align(Alignment.CenterStart)
                .size(8.dp) // Size of the circular indicator
                .background(Color.White, CircleShape) // Change the color as needed
                .align(Alignment.CenterStart) // Align to the start of the box
                .align(Alignment.Center)
        )
    }
}