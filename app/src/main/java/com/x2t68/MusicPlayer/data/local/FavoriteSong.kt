package com.x2t68.MusicPlayer.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorite_songs")
data class FavoriteSong(
    @PrimaryKey val id: Long,
    val title: String?,
    val artist: String?,
    val albumId: Long,
    val data: String,
    val dateAdded: Long,
    val album: String?,
    val folder: String?
)