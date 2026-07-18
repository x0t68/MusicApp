package com.x2t68.MusicPlayer.presentation.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.x2t68.MusicPlayer.data.local.AppDatabase
import com.x2t68.MusicPlayer.data.repository.MusicRepository

class ViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    companion object {
        private var repositoryInstance: MusicRepository? = null

        private fun getRepository(context: Context): MusicRepository {
            return repositoryInstance ?: synchronized(this) {
                val database = AppDatabase.getDatabase(context)
                val repo = MusicRepository(context.applicationContext, database.favoriteDao())
                repositoryInstance = repo
                repo
            }
        }
    }

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val repository = getRepository(context)
        if (modelClass.isAssignableFrom(MusicViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MusicViewModel(repository) as T
        }
        if (modelClass.isAssignableFrom(PlayerViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return PlayerViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}