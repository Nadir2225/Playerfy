// BroadcastHelper.kt
package com.example.playerfy.util

import android.content.Context
import android.content.Intent
import android.widget.Toast

object BroadcastHelper {

    // Function to send broadcast intents for music control actions
    fun sendMusicControlBroadcast(context: Context, action: String, audioUri: String? = null) {
        val intent = Intent(action).apply {
            audioUri?.let { putExtra("AUDIO_URI", it) }
        }
        context.sendBroadcast(intent)
    }
}
