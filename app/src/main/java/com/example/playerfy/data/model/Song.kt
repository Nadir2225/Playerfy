package com.example.playerfy.data.model

import android.net.Uri
import com.example.playerfy.R

data class Song(
    val id: Long,
    val title: String,
    val artist: String,
    val contentUri: Uri,
    val albumArtUri: Uri?, // nullable since some songs may not have album art
    val filePath: String,
    val duration: Long
)