package com.example.playerfy

import android.annotation.SuppressLint
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ExpandedPlayerInterface(songs: List<Song>, expanded: Boolean, collapse: () -> Unit) {
    val state = rememberPagerState(pageCount = { songs.size })

    // Remember the current song name based on the current page index
    val currentSongIndex = state.currentPage
    val currentSongName = songs[currentSongIndex].name

    val screenHeight = LocalConfiguration.current.screenHeightDp.dp

    val offset by animateDpAsState(
        targetValue = if (expanded) 0.dp else screenHeight + 100.dp,
        animationSpec = tween(durationMillis = 300)
    )

    Box (
        modifier = Modifier
            .offset(y = offset)
            .fillMaxSize()
            .background(Color(0xFF2ecc71))
            .zIndex(1f)
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(80.dp)
                    .padding(start = 10.dp, bottom = 10.dp)
                    .padding(top = 21.dp),
                verticalArrangement = Arrangement.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.arrow_down),
                    contentDescription = null,
                    modifier = Modifier
                        .align(Alignment.Start)
                        .clickable { collapse() }
                )
            }
            HorizontalPager(state = state) { index ->
                val song = songs[index]

                // Directly display the song name based on the current index
                Column(
                    modifier = Modifier
                        .fillMaxHeight(0.5F)
                        .fillMaxWidth(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Image(
                        painter = painterResource(song.img ?: R.drawable.song_img),
                        contentDescription = null,
                        modifier = Modifier
                            .clip(RoundedCornerShape(16.dp))
                            .clickable { collapse() }
                    )
                }
            }
            // Display the current song name
            PlayerActions(currentSongName)
        }
    }
}

fun CollapsedPlayerInterface(songs: List<Song>, expand: () -> Unit) {

}

@Composable
fun PlayerActions(name: String) {
    Text(text = name)
}
