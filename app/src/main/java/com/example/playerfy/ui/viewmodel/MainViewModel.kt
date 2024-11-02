package com.example.playerfy.ui.viewmodel

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MainViewModel: ViewModel() {
    private val _visiblePermissionDialogQueue = MutableLiveData<MutableList<String>>()
    val visiblePermissionDialogQueue: LiveData<MutableList<String>> = _visiblePermissionDialogQueue

    fun dimissDialog() {
        _visiblePermissionDialogQueue.value?.removeLast()
    }

    fun onPermissionResult(
        permission: String,
        isGranted: Boolean
    ) {
        if (!isGranted) {
            _visiblePermissionDialogQueue.value?.add(0, permission)
        }
    }
}