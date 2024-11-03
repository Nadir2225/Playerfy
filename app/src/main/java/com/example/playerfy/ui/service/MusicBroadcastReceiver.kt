package com.example.playerfy.ui.service

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast

class MusicBroadcastReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        context?.let {
            val action = intent?.action
            val musicIntent = Intent(it, MusicService::class.java).apply {
                this.action = action

                // Pass the AUDIO_URI from the original intent to the musicIntent
                val audioUri = intent?.getStringExtra("AUDIO_URI")
                if (audioUri != null) {
                    putExtra("AUDIO_URI", audioUri) // Add the AUDIO_URI to the new intent
                }
            }
            it.startService(musicIntent)
        }
    }
}
