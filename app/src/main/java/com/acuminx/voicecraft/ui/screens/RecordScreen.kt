package com.acuminx.voicecraft.ui.screens

import android.content.Intent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.acuminx.voicecraft.audio.recorder.AudioRecorderManager
import com.acuminx.voicecraft.audio.recorder.RecordingService
import com.acuminx.voicecraft.ui.components.WaveformVisualizer

@Composable
fun RecordScreen() {
    val context = LocalContext.current
    val isRecording by AudioRecorderManager.isRecording.collectAsState()

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        WaveformVisualizer(AudioRecorderManager.amplitudeFlow)

        Spacer(modifier = Modifier.height(48.dp))

        Button(
            onClick = {
                val intent = Intent(context, RecordingService::class.java)
                if (isRecording) {
                    intent.action = "ACTION_STOP"
                    context.startService(intent)
                } else {
                    intent.action = "ACTION_START"
                    context.startService(intent)
                }
            },
            modifier = Modifier.size(80.dp),
            shape = CircleShape,
            colors = ButtonDefaults.buttonColors(
                containerColor = if (isRecording) Color.Red else Color.Blue
            )
        ) {
            Icon(
                imageVector = if (isRecording) Icons.Default.Stop else Icons.Default.Mic,
                contentDescription = "Toggle Recording",
                tint = Color.White
            )
        }
    }
}