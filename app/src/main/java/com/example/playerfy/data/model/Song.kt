package com.example.playerfy.data.model

import com.example.playerfy.R

data class Song (
    var img: Int = R.drawable.song_img,
    var name: String,
    var min: Int = 0,
    var sec: Int = 0
)