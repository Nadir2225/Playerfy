package com.example.playerfy

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PlayerInterface(songs: List<Song>) {
    val state = rememberPagerState(pageCount = { songs.size })
    HorizontalPager(state = state) { index ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(songs[index].color)
        ) {
            Text(text = songs[index].name)
        }
    }
    PlayerActions()
}

@Composable
fun PlayerActions() {
    Text(text = "hehe")
}