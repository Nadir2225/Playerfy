package com.example.playerfy

import androidx.compose.ui.graphics.Color

data class Song (
    var img: Int? = 0,
    var name: String,
    var min: Int = 0,
    var sec: Int = 0,
    var color: Color
)