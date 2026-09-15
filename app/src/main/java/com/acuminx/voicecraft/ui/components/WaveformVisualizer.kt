package com.acuminx.voicecraft.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.flow.StateFlow

@Composable
fun WaveformVisualizer(amplitudeFlow: StateFlow<Int>) {
    val amplitude by amplitudeFlow.collectAsState()
    val amplitudes = remember { mutableStateListOf<Int>() }

    LaunchedEffect(amplitude) {
        amplitudes.add(amplitude)
        if (amplitudes.size > 50) amplitudes.removeAt(0)
    }

    Canvas(modifier = Modifier.fillMaxWidth().height(120.dp)) {
        val barWidth = 10f
        val gap = 6f

        amplitudes.forEachIndexed { index, amp ->
            val normalizedHeight = (amp / 32767f) * size.height
            val xOffset = index * (barWidth + gap)

            drawLine(
                color = Color.Cyan,
                start = Offset(xOffset, size.height / 2 - normalizedHeight / 2),
                end = Offset(xOffset, size.height / 2 + normalizedHeight / 2),
                strokeWidth = barWidth,
                cap = StrokeCap.Round
            )
        }
    }
}