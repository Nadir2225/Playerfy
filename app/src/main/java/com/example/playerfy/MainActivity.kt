package com.example.playerfy

import SongsViewModelFactory
import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import com.example.playerfy.App
import com.example.playerfy.ui.screens.NoPermissionScreen
import com.example.playerfy.ui.service.MusicBroadcastReceiver
import com.example.playerfy.ui.theme.PlayerfyTheme
import com.example.playerfy.ui.viewmodel.MainViewModel
import com.example.playerfy.ui.viewmodel.SongsViewModel

class MainActivity : ComponentActivity() {

    private val songsViewModel: SongsViewModel by viewModels {
        SongsViewModelFactory(application)
    }

    private val mainViewModel: MainViewModel by viewModels()

    private lateinit var musicReceiver: MusicBroadcastReceiver

    private val permission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        Manifest.permission.READ_MEDIA_AUDIO
    } else {
        Manifest.permission.READ_EXTERNAL_STORAGE
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PlayerfyTheme {
                if (checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED) {
                    songsViewModel.fetchAudioFiles()
                    App(songsViewModel = songsViewModel)
                } else {
                    NoPermissionScreen(mainViewModel = mainViewModel, permission = permission)
                }
            }
        }

        // Initialiser le BroadcastReceiver
        musicReceiver = MusicBroadcastReceiver()
        val filter = IntentFilter().apply {
            addAction("PLAY")
            addAction("PAUSE")
            addAction("STOP")
        }
        registerReceiver(musicReceiver, filter)

    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(musicReceiver) // Désenregistrer le BroadcastReceiver
    }

}
