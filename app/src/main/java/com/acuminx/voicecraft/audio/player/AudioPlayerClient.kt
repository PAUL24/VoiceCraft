package com.acuminx.voicecraft.audio.player

import android.content.ComponentName
import android.content.Context
import android.net.Uri
import androidx.core.content.ContextCompat
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.session.MediaController
import androidx.media3.session.SessionToken
import com.google.common.util.concurrent.ListenableFuture
import kotlinx.coroutines.flow.MutableStateFlow

class AudioPlayerClient(private val context: Context) {
    private var controllerFuture: ListenableFuture<MediaController>? = null
    private var controller: MediaController? = null

    val isPlaying = MutableStateFlow(false)
    val currentlyPlayingUri = MutableStateFlow<Uri?>(null)

    fun initialize() {
        val sessionToken = SessionToken(
            context,
            ComponentName(context, PlaybackService::class.java)
        )
        controllerFuture = MediaController.Builder(context, sessionToken).buildAsync()
        controllerFuture?.addListener({
            controller = controllerFuture?.get()
            setupListeners()
        }, ContextCompat.getMainExecutor(context))
    }

    private fun setupListeners() {
        controller?.addListener(object : Player.Listener {
            override fun onIsPlayingChanged(isPlayingState: Boolean) {
                isPlaying.value = isPlayingState
            }
        })
    }

    fun playFile(uri: Uri) {
        controller?.apply {
            setMediaItem(MediaItem.fromUri(uri))
            prepare()
            play()
            currentlyPlayingUri.value = uri
        }
    }

    fun pause() {
        controller?.pause()
    }

    fun release() {
        controllerFuture?.let { MediaController.releaseFuture(it) }
        controller = null
    }
}