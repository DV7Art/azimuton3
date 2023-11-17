package com.example.azimuton3.model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class GameViewModel : ViewModel() {

    fun measureFrameTime(lastFrameTimeNanos: Long, callback: (Int) -> Unit) {
        viewModelScope.launch {
            val fps = withContext(Dispatchers.Default) {
                calculateFPS(lastFrameTimeNanos)
            }
            callback(fps)
        }
    }

    private fun calculateFPS(lastFrameTimeNanos: Long): Int {
        //код измерения FPS
        if (lastFrameTimeNanos != 0L) {
            val frameTime = (System.nanoTime() - lastFrameTimeNanos) / 1_000_000

            if (frameTime > 0) {
                return (1_000 / frameTime).toInt()
            }
        }

        return 0
    }
}
