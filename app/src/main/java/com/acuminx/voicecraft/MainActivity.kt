package com.acuminx.voicecraft

import android.Manifest
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.acuminx.voicecraft.audio.player.AudioPlayerClient
import com.acuminx.voicecraft.ui.screens.RecordScreen
import com.acuminx.voicecraft.ui.screens.RecordingsListScreen
import com.acuminx.voicecraft.utils.PermissionHelper

class MainActivity : ComponentActivity() {
    private lateinit var playerClient: AudioPlayerClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        playerClient = AudioPlayerClient(this)

        setContent {
            VoiceCraftApp(playerClient)
        }
    }
}

@Composable
fun VoiceCraftApp(playerClient: AudioPlayerClient) {
    val context = androidx.compose.ui.platform.LocalContext.current
    var hasPermissions by remember { mutableStateOf(PermissionHelper.hasRecordingPermissions(context)) }
    var selectedTab by remember { mutableStateOf(0) }

    val permissionLauncher = androidx.activity.compose.rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) {
        hasPermissions = PermissionHelper.hasRecordingPermissions(context)
    }

    DisposableEffect(Unit) {
        if (!hasPermissions) {
            val permissions = mutableListOf(Manifest.permission.RECORD_AUDIO)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                permissions.add(Manifest.permission.POST_NOTIFICATIONS)
            }
            permissionLauncher.launch(permissions.toTypedArray())
        }

        playerClient.initialize()

        onDispose {
            playerClient.release()
        }
    }

    MaterialTheme {
        Scaffold(
            bottomBar = {
                NavigationBar {
                    NavigationBarItem(
                        icon = { Text("Record") },
                        selected = selectedTab == 0,
                        onClick = { selectedTab = 0 }
                    )
                    NavigationBarItem(
                        icon = { Text("Library") },
                        selected = selectedTab == 1,
                        onClick = { selectedTab = 1 }
                    )
                }
            }
        ) { paddingValues ->
            Column(modifier = Modifier.padding(paddingValues)) {
                if (!hasPermissions) {
                    Text("Permissions required to use this app.")
                } else {
                    when (selectedTab) {
                        0 -> RecordScreen()
                        1 -> RecordingsListScreen(playerClient = playerClient)
                    }
                }
            }
        }
    }
}