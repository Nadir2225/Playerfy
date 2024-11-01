package com.example.playerfy.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.playerfy.data.model.Song

class SongsViewModel: ViewModel() {
    private var _song: MutableLiveData<Song>? = null
    val song: LiveData<Song>? = _song


}