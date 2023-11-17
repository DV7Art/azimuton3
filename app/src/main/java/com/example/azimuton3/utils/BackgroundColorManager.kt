package com.example.azimuton3.utils

import android.content.Context
import android.content.SharedPreferences
import android.graphics.Color
import androidx.core.content.edit
import kotlin.random.Random

class BackgroundColorManager(private val context: Context) {

    private val sharedPref: SharedPreferences =
        context.getSharedPreferences("background_color_pref", Context.MODE_PRIVATE)

    fun saveBackgroundColor() {
        sharedPref.edit {
            putInt("background_color", getRandomColor())
            commit()
        }
    }

    fun restoreBackgroundColor(defaultColor: Int): Int {
        return sharedPref.getInt("background_color", defaultColor)
    }

    private fun getRandomColor(): Int {
        return Color.rgb(Random.nextInt(256), Random.nextInt(256), Random.nextInt(256))
    }
}
