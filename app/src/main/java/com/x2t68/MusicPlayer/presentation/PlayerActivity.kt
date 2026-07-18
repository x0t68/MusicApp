package com.x2t68.MusicPlayer.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.x2t68.MusicPlayer.data.Song
import com.x2t68.MusicPlayer.presentation.ui.PlayerScreen

class PlayerActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val mySongList = intent.getParcelableArrayListExtra<Song>("songList")?.toList() ?: emptyList()
        val initialIndex = intent.getIntExtra("position", 0)
        val isNewSelection = intent.getBooleanExtra("isNewSelection", false)

        setContent {
            PlayerScreen(
                songList = mySongList,
                initialIndex = initialIndex,
                isNewSelection = isNewSelection,
                onBack = { finish() }
            )
        }
    }
}