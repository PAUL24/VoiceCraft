# VoiceCraft 🎙️

VoiceCraft is a modern Android application designed for recording high-quality voice memos and playing them back seamlessly in the background. It is built entirely with **Kotlin** and **Jetpack Compose**, leveraging the power of **Jetpack Media3 (ExoPlayer)** and Android 14+ **Foreground Services**.

## Features ✨

*   **High-Quality Recording:** Uses `MediaRecorder` to capture audio via a dedicated Foreground Service, preventing the system from killing the recording when the app is minimized.
*   **Real-Time Visualizer:** A dynamic waveform preview that renders microphone amplitude data natively using a Jetpack Compose Canvas.
*   **Background Playback:** Utilizes Jetpack Media3's `MediaSessionService` and ExoPlayer for robust background playback, including system media notification controls (Play/Pause) that persist across lock screens.
*   **Recordings Management:** View, play, and delete recorded audio files saved locally in scoped storage.
*   **Modern Architecture:** Built following Android 14's strict foreground service requirements (`foregroundServiceType="microphone"` and `mediaPlayback`).

## Tech Stack 🛠️

*   **UI:** Jetpack Compose, Material Design 3
*   **Media Playback:** Jetpack Media3 (ExoPlayer, MediaSession)
*   **Audio Recording:** Android `MediaRecorder` API
*   **Concurrency:** Kotlin Coroutines & StateFlow
*   **Services:** Android `ServiceCompat`, `MediaSessionService`

## Getting Started 🚀

### 1. Requirements
*   Android Studio Iguana or newer.
*   Minimum SDK: 24 (Android 7.0)
*   Target SDK: 34 (Android 14)

### 2. Add Dependencies
Ensure your `app/build.gradle.kts` includes the required Media3 and Compose Icons dependencies:
```kotlin
dependencies {
    // Media3 ExoPlayer & Session
    val media3Version = "1.3.1" // Use latest stable version
    implementation("androidx.media3:media3-exoplayer:$media3Version")
    implementation("androidx.media3:media3-session:$media3Version")
    implementation("androidx.media3:media3-ui:$media3Version")

    // Material Icons Extended
    implementation("androidx.compose.material:material-icons-extended")
}
```

### 3. Permissions Setup
The app automatically requests the necessary runtime permissions (`RECORD_AUDIO`, `POST_NOTIFICATIONS`) on launch. Make sure your `AndroidManifest.xml` includes the necessary Foreground Service configurations for `microphone` and `mediaPlayback` as per Android 14 guidelines.

## Project Structure 📁

```text
com.example.voicecraft
├── MainActivity.kt
├── audio/
│   ├── player/
│   │   ├── AudioPlayerClient.kt     # Connects Compose to MediaSession
│   │   └── PlaybackService.kt       # Media3 MediaSessionService
│   └── recorder/
│       ├── AudioRecorderManager.kt  # MediaRecorder logic & amplitude polling
│       └── RecordingService.kt      # Microphone Foreground Service
├── ui/
│   ├── components/
│   │   └── WaveformVisualizer.kt    # Custom Compose Canvas visualizer
│   └── screens/
│       ├── RecordScreen.kt          # UI for recording audio
│       └── RecordingsListScreen.kt  # UI for listing and playing files
└── utils/
    └── PermissionHelper.kt          # Helper for Android runtime permissions
```

## License 📄
This project is open-source and available under the MIT License.
