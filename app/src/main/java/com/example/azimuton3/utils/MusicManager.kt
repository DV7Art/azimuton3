package com.example.azimuton3.utils

import android.content.Context
import android.media.MediaPlayer
import android.util.Log

object MusicManager {

    private var mediaPlayer: MediaPlayer? = null

    fun start(context: Context, rawResourceId: Int) {
        if (mediaPlayer == null) {
            mediaPlayer = MediaPlayer.create(context, rawResourceId)
            mediaPlayer?.isLooping = true
            mediaPlayer?.start()
            Log.d("MusicManager", "Music started")
        }
    }

    fun stop() {
        mediaPlayer?.let {
            if (it.isPlaying) {
                it.stop()
                it.release()
                mediaPlayer = null
                Log.d("MusicManager", "Music stopped")
            }
        }
    }

    fun changeMusic(context: Context, rawResourceId: Int) {
        stop()
        start(context, rawResourceId)
        Log.d("MusicManager", "Music changed")
    }
}
