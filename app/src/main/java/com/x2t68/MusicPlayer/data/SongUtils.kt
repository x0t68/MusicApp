package com.x2t68.MusicPlayer.data

import android.content.Context
import android.provider.MediaStore
import java.io.File

fun getSongs(context: Context): List<Song> {
    val songs = mutableListOf<Song>()
    val uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
    val selection = "${MediaStore.Audio.Media.IS_MUSIC}!=0"
    val sortOrder = "${MediaStore.Audio.Media.DATE_ADDED} DESC"

    val projection = arrayOf(
        MediaStore.Audio.Media._ID,
        MediaStore.Audio.Media.TITLE,
        MediaStore.Audio.Media.ARTIST,
        MediaStore.Audio.Media.DATA,
        MediaStore.Audio.Media.ALBUM_ID,
        MediaStore.Audio.Media.DATE_ADDED,
        MediaStore.Audio.Media.ALBUM
    )

    val cursor = context.contentResolver.query(
        uri, projection, selection, null, sortOrder
    )
    cursor?.use {
        val idCol = it.getColumnIndexOrThrow(MediaStore.Audio.Media._ID)
        val titleCol = it.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE)
        val artistCol = it.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST)
        val dataCol = it.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA)
        val albumIdCol = it.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM_ID)
        val dateAddedCol = it.getColumnIndexOrThrow(MediaStore.Audio.Media.DATE_ADDED)
        val albumCol = it.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM)

        while (it.moveToNext()) {
            val id = it.getLong(idCol)
            val title = it.getString(titleCol)
            val artist = it.getString(artistCol)
            val data = it.getString(dataCol)
            val albumId = it.getLong(albumIdCol)
            val dateAdded = it.getLong(dateAddedCol)
            val album = it.getString(albumCol)
            
            val folder = File(data).parentFile?.name ?: "Unknown"

            songs.add(Song(id, title, artist, albumId, data, dateAdded, album, folder))
        }
    }
    return songs
}