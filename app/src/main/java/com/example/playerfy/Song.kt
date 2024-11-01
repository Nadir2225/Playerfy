package com.example.playerfy

import android.content.Context
import android.graphics.BitmapFactory
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.palette.graphics.Palette

data class Song (
    var img: Int = R.drawable.song_img,
    var name: String,
    var min: Int = 0,
    var sec: Int = 0
)