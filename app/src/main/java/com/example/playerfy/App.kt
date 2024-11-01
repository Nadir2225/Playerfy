package com.example.playerfy

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp

@Composable
fun App() {
    var expanded by remember { mutableStateOf(true) }
    val items = listOf(
        Song(name = "song 1", color = Color(0xFF2ecc71)),
        Song(name = "song 2", color = Color(0xFFe74c3c)),
        Song(name = "song 3", color = Color(0xFF2980b9)),
        Song(name = "song 4", color = Color(0xFFe67e22))
    )
    Box(modifier = Modifier.fillMaxSize()) {
        Text(text = "hii")
        ExpandedPlayerInterface(items, expanded, collapse = { expanded = false })
        if (!expanded) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.95F)
                    .padding(0.dp)
                    .padding(bottom = 60.dp)
                    .background(Color(0xFF2ecc71), shape = RoundedCornerShape(16.dp))
                    .align(Alignment.BottomCenter)
                    .animateContentSize(tween(300)) // Animate size changes
                    .height(60.dp)
                    .clickable {
                        expanded = true
                    }
            ) {
                CollapsedPlayerInterface(items, expand = { expanded = true })
            }
        }
    }
}