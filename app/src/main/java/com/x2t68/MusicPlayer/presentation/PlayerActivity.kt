package com.x2t68.MusicPlayer.presentation

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.x2t68.MusicPlayer.data.Song
import com.x2t68.MusicPlayer.theme.PlayerScreen

class PlayerActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val mySongList=intent.getParcelableArrayListExtra("songList")?: emptyList<Song>()
        val initialIndex=intent.getIntExtra("position",0)
setContent {
    PlayerScreen(
        songList = mySongList,
        initialIndex = initialIndex,
        onBack = { finish() }
    )
}
        }
    }
