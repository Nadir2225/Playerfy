package com.example.playerfy.util

import android.content.Context
import android.content.Intent

object BroadcastHelper {

    // Function to send broadcast intents for music control actions
    fun sendMusicControlBroadcast(context: Context, action: String) {
        val intent = Intent(action)
        context.sendBroadcast(intent)
    }
}
