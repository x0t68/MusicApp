package com.x2t68.MusicPlayer.data.repository

import android.content.Context
import com.x2t68.MusicPlayer.data.Song
import com.x2t68.MusicPlayer.data.getSongs
import com.x2t68.MusicPlayer.data.local.FavoriteDao
import com.x2t68.MusicPlayer.data.local.FavoriteSong
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class MusicRepository(
    private val context: Context,
    private val favoriteDao: FavoriteDao
) {
    private var currentQueue: List<Song> = emptyList()

    fun setCurrentQueue(songs: List<Song>) {
        currentQueue = songs
    }
    fun getCurrentQueue(): List<Song> = currentQueue

    fun getAllSongs(): List<Song> = getSongs(context)

    fun getFavorites(): Flow<List<Song>> = favoriteDao.getAllFavorites().map { favorites ->
        favorites.map { it.toSong() }
    }

    suspend fun addFavorite(song: Song) {
        favoriteDao.insertFavorite(song.toFavoriteSong())
    }

    suspend fun removeFavorite(song: Song) {
        favoriteDao.deleteFavorite(song.toFavoriteSong())
    }

    fun isFavorite(songId: Long): Flow<Boolean> = favoriteDao.isFavorite(songId)

    private fun Song.toFavoriteSong() = FavoriteSong(
        id = id,
        title = title,
        artist = artist,
        albumId = albumId,
        data = data,
        dateAdded = dateAdded,
        album = album,
        folder = folder
    )

    private fun FavoriteSong.toSong() = Song(
        id = id,
        title = title,
        artist = artist,
        albumId = albumId,
        data = data,
        dateAdded = dateAdded,
        album = album,
        folder = folder
    )
}