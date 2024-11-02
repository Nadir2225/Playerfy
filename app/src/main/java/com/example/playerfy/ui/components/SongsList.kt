package com.example.playerfy.ui.components

import android.content.ContentResolver
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.playerfy.R
import com.example.playerfy.data.model.Song
import com.example.playerfy.ui.theme.Green
import com.example.playerfy.ui.viewmodel.SongsViewModel

@Composable
fun SongsList(cont: ContentResolver, songs: MutableList<Song>, songsViewModel: SongsViewModel, onSongClick: (Song) -> Unit) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 30.dp),
    ) {
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(110.dp)
            ) {
                Box(modifier = Modifier
                    .size(50.dp)
                    .clip(CircleShape)
                    .background(Green)
                    .align(Alignment.Center)) {
                    Image(
                        painter = painterResource(R.drawable.play),
                        contentDescription = null,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
            }
            Column {
                songs.forEach { song ->
                    SongItem(cont = cont, song = song, songsViewModel = songsViewModel, onClick = { onSongClick(song) })
                }
            }
            Spacer(modifier = Modifier.height(140.dp))
        }
    }
}