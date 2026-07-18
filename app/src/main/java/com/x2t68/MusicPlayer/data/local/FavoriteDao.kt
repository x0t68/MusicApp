package com.x2t68.MusicPlayer.data.local

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoriteDao {
    @Query("SELECT * FROM favorite_songs ORDER BY dateAdded DESC")
    fun getAllFavorites(): Flow<List<FavoriteSong>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavorite(song: FavoriteSong)

    @Delete
    suspend fun deleteFavorite(song: FavoriteSong)

    @Query("SELECT EXISTS(SELECT * FROM favorite_songs WHERE id = :songId)")
    fun isFavorite(songId: Long): Flow<Boolean>
}