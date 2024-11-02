package com.example.playerfy.ui.components

import android.content.ContentResolver
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.playerfy.R
import com.example.playerfy.data.model.Song
import com.example.playerfy.ui.viewmodel.SongsViewModel

@Composable
fun SongItem(cont: ContentResolver, song: Song, songsViewModel: SongsViewModel, onClick: () -> Unit) {
    val context = LocalContext.current

    val bitmap: Bitmap = if (song.albumArtUri != null) {
        songsViewModel.getBitmapFromUri(cont, song.albumArtUri)?:
        BitmapFactory.decodeResource(context.resources, R.drawable.song_img)
    } else {
        BitmapFactory.decodeResource(context.resources, R.drawable.song_img)
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(75.dp)
            .clickable {
                onClick()
            }
            .padding(start = 20.dp, end = 20.dp)
        ,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = BitmapPainter(bitmap.asImageBitmap()),
            contentDescription = null,
            modifier = Modifier
                .size(60.dp)
                .clip(RoundedCornerShape(10.dp))
                .padding(end = 10.dp)
        )
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center
        ) {
            Text(text = if (song.title.length >= 23) "${song.title.slice(0..22)}..." else song.title, color = Color.White, fontSize = 15.sp, fontWeight = FontWeight.Bold)
            Text(text = song.artist, color = Color( 211, 211, 211, 179), fontSize = 13.sp,)
        }
    }
}