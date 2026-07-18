package com.x2t68.MusicPlayer.presentation

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.x2t68.MusicPlayer.presentation.ui.MainScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MainScreen { songs, position ->
                val intent = Intent(this, PlayerActivity::class.java)
                intent.putParcelableArrayListExtra("songList", ArrayList(songs))
                intent.putExtra("position", position)
                intent.putExtra("isNewSelection", true)
                startActivity(intent)
            }
        }
    }
}