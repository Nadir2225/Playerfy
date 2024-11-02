package com.example.playerfy.ui.screens

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.playerfy.ui.viewmodel.MainViewModel

@Composable
fun NoPermissionScreen(mainViewModel: MainViewModel, permission: String) {
    val audioPermissionResultLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted ->
            mainViewModel.onPermissionResult(
                permission = permission,
                isGranted = isGranted
            )
        }
    )
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.padding(16.dp)
        ) {
            Text("Permission Denied")
            Spacer(modifier = Modifier.height(8.dp))
            Text("This app needs access to your audio files.")
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = {
                audioPermissionResultLauncher.launch(permission)
            }) {
                Text("Request Permission Again")
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text("or either if the button didn't work allow permissions from the settings")
        }
    }
}
