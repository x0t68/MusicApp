package com.x2t68.MusicPlayer.presentation

import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import com.x2t68.MusicPlayer.theme.SplashScreen

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SplashScreen(
                onStartClick ={
                    startActivity(Intent(this@SplashActivity, MainActivity::class.java))
                    finish()
                }
            )
        }
    }
}