package com.example.azimuton3.ui

import MainFragment
import WebViewFragment
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.azimuton3.R
import com.example.azimuton3.utils.MusicManager

class MainActivity : AppCompatActivity() {
    private lateinit var musicManager: MusicManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, MainFragment())
                .commit()
        }
        musicManager = MusicManager
        MusicManager.start(this, R.raw.main_menu_music)

    }

    fun showWebView(url: String) {
        val fragment = WebViewFragment()
        val bundle = Bundle()
        bundle.putString("url", url)
        fragment.arguments = bundle

        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, fragment)
            .addToBackStack(null)
            .commit()
    }

    fun showGameScreen() {
        val fragment = GameFragment()

        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, fragment)
            .addToBackStack(null)
            .commit()
        MusicManager.changeMusic(this, R.raw.background_music)
    }

    fun showHistoryScreen() {
        val fragment = HistoryFragment()
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, fragment)
            .addToBackStack(null)
            .commit()

    }


    override fun onDestroy() {
        super.onDestroy()
        MusicManager.stop()
    }
}
