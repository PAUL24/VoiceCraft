package com.acuminx.voicecraft.audio.recorder

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.content.pm.ServiceInfo
import android.os.Build
import android.os.Environment
import android.os.IBinder
import androidx.core.app.NotificationCompat
import androidx.core.app.ServiceCompat
import java.io.File

class RecordingService : Service() {

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val action = intent?.action
        when (action) {
            "ACTION_START" -> startForegroundRecording()
            "ACTION_STOP" -> stopForegroundRecording()
        }
        return START_NOT_STICKY
    }

    private fun startForegroundRecording() {
        createNotificationChannel()
        val notification = NotificationCompat.Builder(this, "recording_channel")
            .setContentTitle("VoiceCraft Recording")
            .setContentText("Actively recording microphone audio...")
            .setSmallIcon(android.R.drawable.ic_btn_speak_now)
            .setOngoing(true)
            .build()

        // Declare the microphone foreground service type for Android 14+
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            ServiceCompat.startForeground(
                this,
                1,
                notification,
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) ServiceInfo.FOREGROUND_SERVICE_TYPE_MICROPHONE else 0
            )
        } else {
            startForeground(1, notification)
        }

        val dir = getExternalFilesDir(Environment.DIRECTORY_MUSIC)
        val file = File(dir, "VoiceCraft_${System.currentTimeMillis()}.m4a")
        AudioRecorderManager.startRecording(this, file)
    }

    private fun stopForegroundRecording() {
        AudioRecorderManager.stopRecording()
        stopForeground(STOP_FOREGROUND_REMOVE)
        stopSelf()
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                "recording_channel",
                "Audio Recording",
                NotificationManager.IMPORTANCE_LOW
            )
            val manager = getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(channel)
        }
    }

    override fun onBind(intent: Intent?): IBinder? = null
}