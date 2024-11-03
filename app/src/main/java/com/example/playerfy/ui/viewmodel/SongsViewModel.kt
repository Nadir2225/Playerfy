package com.example.playerfy.ui.viewmodel

import android.app.Application
import android.content.ContentResolver
import android.content.ContentUris
import android.content.IntentFilter
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.provider.MediaStore
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.playerfy.data.model.CurrentSong
import com.example.playerfy.data.model.Song
import com.example.playerfy.ui.service.MusicBroadcastReceiver
import com.example.playerfy.util.BroadcastHelper
import java.io.File

class SongsViewModel(application: Application) : AndroidViewModel(application) {
    private var _currentSong: MutableLiveData<CurrentSong> = MutableLiveData(CurrentSong(null))
    val currentSong: LiveData<CurrentSong> = _currentSong

    private var _songsList: MutableLiveData<MutableList<Song>> = MutableLiveData(mutableListOf())
    val songsList: LiveData<MutableList<Song>> = _songsList

    private var _songsQueue: MutableLiveData<MutableList<Song>> = MutableLiveData(mutableListOf())
    val songsQueue: LiveData<MutableList<Song>> = _songsQueue

    private var _isPaused: MutableLiveData<Boolean> = MutableLiveData(false)
    val isPaused: LiveData<Boolean> = _isPaused

    private val context = getApplication<Application>().applicationContext
    private var musicReceiver: MusicBroadcastReceiver

    private val _currentPosition = MutableLiveData<Long>()
    val currentPosition: LiveData<Long> = _currentPosition

    private val _totalDuration = MutableLiveData<Long>()
    val totalDuration: LiveData<Long> = _totalDuration

    init {
        // Register the BroadcastReceiver
        musicReceiver = MusicBroadcastReceiver()
        val filter = IntentFilter().apply {
            addAction("PLAY")
            addAction("PAUSE")
            addAction("RESUME")
            addAction("STOP")
        }
        context.registerReceiver(musicReceiver, filter)
    }

    override fun onCleared() {
        super.onCleared()
        // Unregister the receiver when ViewModel is cleared
        context.unregisterReceiver(musicReceiver)
    }

    // Function to update the currently selected song
    fun updateCurrentSong(newSong: Song) {
        _currentSong.value = CurrentSong(newSong)
    }

    fun updateSongsList(l: MutableList<Song>) {
        _songsList.value = l
    }

    fun shuffle() {
        _songsQueue.value?.shuffle()
    }

    fun unShuffle() {
        _songsQueue.value = _songsList.value
    }

    fun playSong(song: Song?) {
        if (song != null) {
            BroadcastHelper.sendMusicControlBroadcast(context = context, "PLAY", audioUri = song.contentUri.toString())
        }
    }

    fun pauseMusic() {
        BroadcastHelper.sendMusicControlBroadcast(context, "PAUSE")
        _isPaused.value = true
    }

    fun updateIsPaused(value: Boolean) {
        _isPaused.value = value
    }


    fun resumeMusic() {
        BroadcastHelper.sendMusicControlBroadcast(context, "RESUME")
        _isPaused.value = false
    }

    fun fetchAudioFiles() {
        val audioList = mutableListOf<Song>()
        val contentResolver = getApplication<Application>().contentResolver

        val projection = arrayOf(
            MediaStore.Audio.Media._ID,
            MediaStore.Audio.Media.TITLE,
            MediaStore.Audio.Media.ARTIST,
            MediaStore.Audio.Media.DATA,        // file path
            MediaStore.Audio.Media.DURATION,    // duration in milliseconds
            MediaStore.Audio.Media.ALBUM_ID     // to fetch album art
        )

        val selection = MediaStore.Audio.Media.IS_MUSIC + "!= 0"
        val sortOrder = MediaStore.Audio.Media.TITLE + " ASC"

        val cursor = contentResolver.query(
            MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
            projection,
            selection,
            null,
            sortOrder
        )

        cursor?.use {
            val idColumn = it.getColumnIndexOrThrow(MediaStore.Audio.Media._ID)
            val titleColumn = it.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE)
            val artistColumn = it.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST)
            val filePathColumn = it.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA)
            val durationColumn = it.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION)
            val albumIdColumn = it.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM_ID)

            while (it.moveToNext()) {
                val id = it.getLong(idColumn)
                val title = it.getString(titleColumn)
                val artist = it.getString(artistColumn)
                val filePath = it.getString(filePathColumn)
                val duration = it.getLong(durationColumn)
                val albumId = it.getLong(albumIdColumn)

                // Build content URI for the song file
                val contentUri: Uri = ContentUris.withAppendedId(
                    MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, id
                )

                // Build URI for album art (album art URI may not be available on all devices)
                val albumArtUri = ContentUris.withAppendedId(
                    Uri.parse("content://media/external/audio/albumart"), albumId
                )

                val folderName = File(filePath).parentFile?.name ?: "Unknown"

                audioList += Song(
                    id = id,
                    title = title,
                    artist = artist,
                    contentUri = contentUri,
                    albumArtUri = albumArtUri,
                    folderName = folderName,
                    duration = duration
                )
            }
        }
        _songsList.value = audioList
    }

    fun getBitmapFromUri(contentResolver: ContentResolver, imageUri: Uri): Bitmap? {
        return try {
            // Open an InputStream using the ContentResolver
            val inputStream = contentResolver.openInputStream(imageUri)
            // Decode the InputStream into a Bitmap
            BitmapFactory.decodeStream(inputStream)
        } catch (e: Exception) {
            e.printStackTrace() // Handle exceptions (e.g., log them)
            null // Return null if there's an error
        }
    }
}
