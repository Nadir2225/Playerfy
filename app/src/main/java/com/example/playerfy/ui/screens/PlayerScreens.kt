package com.example.playerfy.ui.screens

import android.content.ContentResolver
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.palette.graphics.Palette
import com.example.playerfy.R
import com.example.playerfy.data.model.Song
import com.example.playerfy.ui.components.SongProgressBar
import com.example.playerfy.ui.viewmodel.SongsViewModel
import com.jetpack.marqueetext.MarqueeText
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ExpandedPlayerInterface(songs: MutableList<Song>, expanded: Boolean, songsViewModel: SongsViewModel, cont: ContentResolver, collapse: () -> Unit) {
    val state = rememberPagerState(pageCount = { songs.size })
    val currentSong by songsViewModel.currentSong.observeAsState()

    LaunchedEffect(currentSong) {
        state.scrollToPage(songs.indexOf(currentSong?.song))
    }

    var isQueueShown by remember { mutableStateOf(false) }

    var songColor by remember { mutableStateOf(Color.Gray) }
    var offsetY by remember { mutableStateOf(0f) }
    var isDragging by remember { mutableStateOf(false) }
    val context = LocalContext.current

    val currentSongIndex = state.currentPage
    val song = songs[currentSongIndex]

    val bitmap: Bitmap = if (song.albumArtUri != null) {
        songsViewModel.getBitmapFromUri(cont, song.albumArtUri)?:
        BitmapFactory.decodeResource(context.resources, R.drawable.song_img)
    } else {
        BitmapFactory.decodeResource(context.resources, R.drawable.song_img)
    }

    // Remember the current song name based on the current page index

    LaunchedEffect(song.albumArtUri) {
        // Load the bitmap and generate the color palette
        withContext(Dispatchers.Default) {
//            val bitmap = BitmapFactory.decodeResource(context.resources, song.img)

            Palette.from(bitmap).generate { palette ->
                palette?.dominantSwatch?.let { swatch ->
                    songColor = Color(swatch.rgb) // Update the dominant color
                }
            }
        }
    }

    LaunchedEffect(expanded) {
        if (expanded) {
            offsetY = 0f
        }
    }

    val screenHeight = LocalConfiguration.current.screenHeightDp.dp

    val offset by animateDpAsState(
        targetValue = if (expanded) 0.dp else screenHeight + 100.dp,
        animationSpec = tween(durationMillis = 300)
    )
    val animatedColor by animateColorAsState(
        targetValue = songColor,
        animationSpec = tween(durationMillis = 500, easing = LinearEasing)
    )

    val gradientColors = listOf(
        animatedColor.copy(alpha = 0.7f), // Optional lighter shade
        animatedColor
    )

    val brush = Brush.verticalGradient(colors = gradientColors)

    if (expanded) {
        Box (
            modifier = Modifier
                .offset(y = offset + offsetY.dp)
                .clip(RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp))
                .fillMaxSize()
                .background(Color.White)
                //            .background(Color(0xFF2ecc71))
                .zIndex(1f)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(brush)
                    .pointerInput(Unit) {
                        detectDragGestures(
                            onDrag = { change, dragAmount ->
                                change.consume() // Consume the drag gesture
                                Log.e("dd", offsetY.toString())
                                offsetY =
                                    if ((offsetY + dragAmount.y * .5f) < 0) offsetY else (offsetY + dragAmount.y * .5f)// Update offset
                            },
                            onDragEnd = {
                                // Action when the drag ends
                                if (offsetY >= 20) {
                                    collapse()// Collapse if dragged halfway or more
                                } else {
                                    offsetY = 0f // Snap back if dragged less than halfway
                                }
                            }
                        )
                    }
            ) {
                PlayerHeader(song.folderName, collapse)
                HorizontalPager(state = state) {
                    // Directly display the song name based on the current index
                    Column(
                        modifier = Modifier
                            .fillMaxHeight(0.5F)
                            .fillMaxWidth(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Image(
                            painter = BitmapPainter(bitmap.asImageBitmap()),
                            contentDescription = null,
                            modifier = Modifier
                                .fillMaxWidth(0.87f) // 60% of screen width
                                .aspectRatio(1f)
                                .clip(RoundedCornerShape(8.dp))
                        )
                    }
                }
                // Display the current song name
                PlayerActions(songsViewModel = songsViewModel, openQueue = { isQueueShown = true })
            }
        }
        Queue(show = isQueueShown, close = { isQueueShown = false })
    }
}

@Composable
fun CollapsedPlayerInterface(songsViewModel: SongsViewModel, cont: ContentResolver, expand: () -> Unit) {
    val currentSong by songsViewModel.currentSong.observeAsState()
    val isPaused by songsViewModel.isPaused.observeAsState()
    val songsList by songsViewModel.songsList.observeAsState()

    val context = LocalContext.current

    val bitmap: Bitmap = if (currentSong?.song?.albumArtUri != null) {
        currentSong!!.song?.albumArtUri?.let { songsViewModel.getBitmapFromUri(cont, it) } ?:
        BitmapFactory.decodeResource(LocalContext.current.resources, R.drawable.song_img)
    } else {
        BitmapFactory.decodeResource(LocalContext.current.resources, R.drawable.song_img)
    }

    var songColor = Color.Gray

    LaunchedEffect(currentSong?.song?.albumArtUri) {
        // Load the bitmap and generate the color palette
        withContext(Dispatchers.Default) {
//            val bitmap = BitmapFactory.decodeResource(context.resources, song.img)

            Palette.from(bitmap).generate { palette ->
                palette?.dominantSwatch?.let { swatch ->
                    songColor = Color(swatch.rgb) // Update the dominant color
                }
            }
        }
    }

    val animatedColor by animateColorAsState(
        targetValue = songColor,
        animationSpec = tween(durationMillis = 500, easing = LinearEasing)
    )

    Row(
        modifier = Modifier
            .fillMaxSize()
            .clip(RoundedCornerShape(14.dp))
            .background(animatedColor)
            .padding(start = 10.dp, end = 10.dp)
            .clickable { },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth(.5f)
                .clickable { expand() },
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = BitmapPainter(bitmap.asImageBitmap()),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxHeight(.95f)
                    .width(60.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .padding(end = 10.dp)
            )
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center
            ) {
                (if (currentSong?.song?.title?.length!! >= 23) "${currentSong?.song?.title!!.slice(0..22)}..." else currentSong!!.song?.title)?.let { Text(text = it, color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold) }
                currentSong?.song?.let { Text(text = it.artist, color = Color( 211, 211, 211, 179), fontSize = 10.sp,) }
            }
        }
        Row (
            modifier = Modifier.fillMaxWidth(.8f),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Image(
                modifier = Modifier
                    .size(30.dp)
                    .clickable {
                        if (songsList!!.indexOf(currentSong?.song) != 0) {
                            songsViewModel.updateCurrentSong(
                                songsList?.get(
                                    songsList!!.indexOf(
                                        currentSong?.song
                                    ) - 1
                                )!!
                            )
                        }
                    }
                ,
                painter = painterResource(R.drawable.previous),
                contentDescription = null
            )
            if (isPaused == true) {
                Box(modifier = Modifier
                    .size(30.dp)
                    .background(Color.White, shape = CircleShape)
                    .clickable {
                        songsViewModel.resumeMusic()
                    }
                ) {
                    Image(
                        modifier = Modifier
                            .size(15.dp)
                            .align(Alignment.Center),
                        painter = painterResource(R.drawable.play),
                        contentDescription = null
                    )
                }
            } else {
                Box(modifier = Modifier
                    .size(30.dp)
                    .background(Color.White, shape = CircleShape)
                    .clickable {
                        songsViewModel.pauseMusic()
                    }
                ) {
                    Image(
                        modifier = Modifier
                            .size(15.dp)
                            .align(Alignment.Center),
                        painter = painterResource(R.drawable.pause),
                        contentDescription = null
                    )
                }
            }

            Image(
                modifier = Modifier
                    .size(30.dp)
                    .clickable {
                        if (songsList!!.indexOf(currentSong?.song) != songsList!!.size - 1) {
                            songsViewModel.updateCurrentSong(
                                songsList?.get(
                                    songsList!!.indexOf(
                                        currentSong?.song
                                    ) + 1
                                )!!
                            )
                        }
                    }
                ,
                painter = painterResource(R.drawable.next),
                contentDescription = null,
            )
        }
    }
}

@Composable
fun PlayerHeader(folderName: String, collapse: () -> Unit, color: Color = Color.Transparent) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(110.dp)
            .background(color)
            .padding(start = 20.dp, end = 20.dp, bottom = 20.dp)
            .padding(top = 21.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Image(
            painter = painterResource(id = R.drawable.arrow_down),
            contentDescription = null,
            modifier = Modifier
                .size(20.dp)
                .clickable { collapse() }
        )
        Column(
            modifier = Modifier.fillMaxHeight(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "PLAYING FROM FOLDER",
                color = Color.White,
                fontSize = 12.sp,
                lineHeight = 12.sp
            )
            Text(
                text = folderName,
                color = Color.White,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                lineHeight = 12.sp
            )
        }
        Box(modifier = Modifier.width(20.dp)) {}
    }
}

@Composable
fun PlayerActions(songsViewModel: SongsViewModel, openQueue: () -> Unit) {
    val context = LocalContext.current
    val currentSong by songsViewModel.currentSong.observeAsState()
    val songsList by songsViewModel.songsList.observeAsState()
    val isPaused by songsViewModel.isPaused.observeAsState()

    Column(
        modifier = Modifier
            .padding(start = 20.dp, end = 20.dp)
            .padding(top = 35.dp)
    ) {
        Column {
            MarqueeText(
                text = currentSong?.song?.title?:"idk",
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 21.sp
            )
            Text(
                text = currentSong?.song?.artist?:"idk",
                color = Color(211, 211, 211, 179),
            )
        }
        SongProgressBar(
            modifier = Modifier
                .padding(0.dp)
                .padding(top = 19.dp),
            prog = .5f
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 3.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "0:00",
                color = Color(211, 211, 211, 179),
                fontSize = 12.sp
            )
            Text(
                text = "2:23",
                color = Color(211, 211, 211, 179),
                fontSize = 12.sp
            )
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 10.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(R.drawable.shuffle),
                contentDescription = null,
                modifier = Modifier.size(30.dp)
            )
            Image(
                painter = painterResource(R.drawable.previous),
                contentDescription = null,
                modifier = Modifier.size(30.dp).clickable {
                    if (songsList!!.indexOf(currentSong?.song) != 0) {
                        songsViewModel.updateCurrentSong(
                            songsList?.get(
                                songsList!!.indexOf(
                                    currentSong?.song
                                ) - 1
                            )!!
                        )
                    }
                }
            )
            if (isPaused == true) {
                Box(
                    modifier = Modifier
                        .clip(CircleShape)
                        .background(Color.White)
                        .size(60.dp)
                        .clickable {
                            songsViewModel.resumeMusic()
                        }
                ) {
                    Image(
                        painter = painterResource(R.drawable.play),
                        contentDescription = null,
                        modifier = Modifier
                            .align(Alignment.Center)
                            .size(23.dp)
                    )
                }
            } else {
                Box(
                    modifier = Modifier
                        .clip(CircleShape)
                        .background(Color.White)
                        .size(60.dp)
                        .clickable {
                            songsViewModel.pauseMusic()
                        }
                ) {
                    Image(
                        painter = painterResource(R.drawable.pause),
                        contentDescription = null,
                        modifier = Modifier
                            .align(Alignment.Center)
                            .size(23.dp)
                    )
                }
            }

            Image(
                painter = painterResource(R.drawable.next),
                contentDescription = null,
                modifier = Modifier
                    .size(30.dp)
                    .clickable {
                        if (songsList!!.indexOf(currentSong?.song) != songsList!!.size - 1) {
                            songsViewModel.updateCurrentSong(
                                songsList?.get(
                                    songsList!!.indexOf(
                                        currentSong?.song
                                    ) + 1
                                )!!
                            )
                        }
                    }
            )
            Image(
                painter = painterResource(R.drawable.repeat),
                contentDescription = null,
                modifier = Modifier.size(30.dp)
            )
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 15.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Image(
                painter = painterResource(R.drawable.share),
                contentDescription = null,
                modifier = Modifier.size(30.dp)
            )
            Image(
                painter = painterResource(R.drawable.menu96),
                contentDescription = null,
                modifier = Modifier
                    .size(30.dp)
                    .clickable {
                        openQueue()
                    }
            )
        }
    }
}