package com.x2t68.MusicPlayer.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Song (
    val id: Long,
    val title: String?,
    val artist: String?,
    val albumId: Long,
    val data: String,
    val dateAdded: Long = 0,
    val album: String? = null,
    val folder: String? = null
): Parcelable