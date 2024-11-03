package com.example.playerfy

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.playerfy.data.model.Song
import com.example.playerfy.ui.components.SongsList
import com.example.playerfy.ui.screens.CollapsedPlayerInterface
import com.example.playerfy.ui.screens.ExpandedPlayerInterface
import com.example.playerfy.ui.theme.Black
import com.example.playerfy.ui.viewmodel.SongsViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@Composable
fun App(songsViewModel: SongsViewModel) {
    val currentSong by songsViewModel.currentSong.observeAsState()
    val songsList by songsViewModel.songsList.observeAsState()

    LaunchedEffect(currentSong) {
//        if (currentSong?.song != null) {
//            if (currentSong!!.paused != null) {
//                if (currentSong!!.paused != true) {
//
//                }
//            }
//        }
        songsViewModel.updateIsPaused(false)
        songsViewModel.playSong(currentSong?.song)
    }

    var expanded by remember { mutableStateOf(true) }
    var context = LocalContext.current

    Box(modifier = Modifier
        .fillMaxSize()
        .background(Black)) {
        if (!songsList.isNullOrEmpty()) {
            SongsList(
                cont = context.contentResolver,
                songs = songsList!!,
                songsViewModel = songsViewModel,
                onSongClick = { song ->
                    expanded = true
                    songsViewModel.updateCurrentSong(song)
            })
        } else {
            Text(text = "no files are found", modifier = Modifier.align(Alignment.Center))
        }
        if (currentSong?.song != null) {
            ExpandedPlayerInterface(
                songs = songsList!!,
                expanded = expanded,
                songsViewModel = songsViewModel,
                cont = LocalContext.current.contentResolver,
                collapse = { expanded = false }
            )
            if (!expanded) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(0.95F)
                        .padding(0.dp)
                        .padding(bottom = 60.dp)
                        .background(Color.Transparent, shape = RoundedCornerShape(14.dp))
                        .align(Alignment.BottomCenter)
                        .animateContentSize(tween(300)) // Animate size changes
                        .height(60.dp)
                ) {
                    CollapsedPlayerInterface(songsViewModel = songsViewModel, cont = context.contentResolver, expand = { expanded = true })
                }
        }
        }
    }
}
