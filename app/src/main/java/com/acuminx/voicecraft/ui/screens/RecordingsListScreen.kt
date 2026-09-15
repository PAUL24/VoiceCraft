package com.acuminx.voicecraft.ui.screens

import android.net.Uri
import android.os.Environment
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.acuminx.voicecraft.audio.player.AudioPlayerClient
import java.io.File

@Composable
fun RecordingsListScreen(playerClient: AudioPlayerClient) {
    val context = LocalContext.current
    val dir = context.getExternalFilesDir(Environment.DIRECTORY_MUSIC)

    // Track file list state to force recomposition on rename/delete
    var files by remember { mutableStateOf(dir?.listFiles()?.toList() ?: emptyList()) }
    val isPlaying by playerClient.isPlaying.collectAsState()
    val playingUri by playerClient.currentlyPlayingUri.collectAsState()

    LazyColumn(modifier = Modifier.fillMaxSize()) {
        items(files) { file ->
            val fileUri = Uri.fromFile(file)
            val isThisFilePlaying = playingUri == fileUri && isPlaying

            Card(
                modifier = Modifier.fillMaxWidth().padding(8.dp)
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = file.name, modifier = Modifier.weight(1f))

                    IconButton(onClick = {
                        if (isThisFilePlaying) {
                            playerClient.pause()
                        } else {
                            playerClient.playFile(fileUri)
                        }
                    }) {
                        Icon(
                            imageVector = if (isThisFilePlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                            contentDescription = "Play/Pause"
                        )
                    }

                    // Delete Button
                    IconButton(onClick = {
                        file.delete()
                        files = dir?.listFiles()?.toList() ?: emptyList() // Refresh list
                    }) {
                        Icon(Icons.Default.Delete, contentDescription = "Delete")
                    }
                }
            }
        }
    }
}