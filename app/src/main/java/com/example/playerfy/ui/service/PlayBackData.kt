package com.example.playerfy.ui.service

import androidx.lifecycle.MutableLiveData

object PlaybackData {
    val currentPosition = MutableLiveData<Long>()
    val totalDuration = MutableLiveData<Long>()
}