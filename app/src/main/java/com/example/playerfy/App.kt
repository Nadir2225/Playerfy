package com.example.playerfy

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

@Composable
fun App() {
    val items = listOf(
        Song(name = "song 1", color = Color(0xFF2ecc71)),
        Song(name = "song 2", color = Color(0xFFe74c3c)),
        Song(name = "song 3", color = Color(0xFF2980b9)),
        Song(name = "song 4", color = Color(0xFFe67e22))
    )
    PlayerInterface(items)
}