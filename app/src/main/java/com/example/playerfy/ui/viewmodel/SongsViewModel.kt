package com.example.playerfy.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.playerfy.data.model.Song

class SongsViewModel : ViewModel() {
    private var _currentSong: MutableLiveData<MutableMap<String, Any>> = MutableLiveData(mutableMapOf())
    val currentSong: LiveData<MutableMap<String, Any>> = _currentSong

    private var _songsList: MutableLiveData<MutableList<Song>> = MutableLiveData(mutableListOf())
    val songsList: LiveData<MutableList<Song>> = _songsList

    private var _songsQueue: MutableLiveData<MutableList<Song>> = MutableLiveData(mutableListOf())
    val songsQueue: LiveData<MutableList<Song>> = _songsQueue

    // Function to update the currently selected song
    fun updateCurrentSong(newSong: Song, index: Int) {
        _currentSong.value = mutableMapOf(
            "index" to index,
            "song" to newSong
        )
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
}