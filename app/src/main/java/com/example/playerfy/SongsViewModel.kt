package com.example.playerfy

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SongsViewModel: ViewModel() {
    private var _song: MutableLiveData<Song>? = null
    val song: LiveData<Song>? = _song


}