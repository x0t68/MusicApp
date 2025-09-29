package com.x2t68.MusicPlayer.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize


@Parcelize
data class Song (
    val id: Long,
    val title: String?,
    val artist: String?,
    val albumId: Long,
    val data: String

): Parcelable