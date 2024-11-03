// MusicService.kt
package com.example.playerfy.ui.service

import android.app.Service
import android.content.Intent
import android.media.MediaPlayer
import android.net.Uri
import android.os.IBinder
import android.widget.Toast
import androidx.compose.ui.platform.LocalContext

class MusicService : Service() {

    private lateinit var mediaPlayer: MediaPlayer

    override fun onCreate() {
        super.onCreate()
        mediaPlayer = MediaPlayer()
        mediaPlayer.isLooping = true // Optional: Set to loop
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when (intent?.action) {
            "PLAY" -> {
                val audioUri = intent.getStringExtra("AUDIO_URI")
                if (audioUri != null) {
                    playMusic(audioUri)
                } else {
                    Toast.makeText(this, "somthing went wrong", Toast.LENGTH_SHORT).show()
                }
            }
            "PAUSE" -> pauseMusic()
            "RESUME" -> resumeMusic()
            "STOP" -> stopMusic()
        }
        return START_STICKY
    }

    private fun playMusic(audioUri: String) {
        try {
            mediaPlayer.reset() // Reset the MediaPlayer
            mediaPlayer.setDataSource(this, Uri.parse(audioUri)) // Use the URI
            mediaPlayer.prepare() // Prepare for playback
            mediaPlayer.start() // Start playback
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(this, "Error playing audio: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }

    private fun pauseMusic() {
        if (mediaPlayer.isPlaying) {
            mediaPlayer.pause()
        }
    }

    private fun resumeMusic() {
        if (!mediaPlayer.isPlaying) {
            mediaPlayer.start() // Resume playback
        }
    }

    private fun stopMusic() {
        if (mediaPlayer.isPlaying) {
            mediaPlayer.stop()
        }
        stopSelf()
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer.release()
    }

    override fun onBind(intent: Intent?): IBinder? = null
}
