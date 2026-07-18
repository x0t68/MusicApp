package com.x2t68.MusicPlayer.presentation.viewmodel

import android.content.ComponentName
import android.content.ContentUris
import android.content.Context
import android.net.Uri
import android.provider.MediaStore
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.session.MediaController
import androidx.media3.session.SessionToken
import com.google.common.util.concurrent.ListenableFuture
import com.google.common.util.concurrent.MoreExecutors
import com.x2t68.MusicPlayer.data.Song
import com.x2t68.MusicPlayer.data.repository.MusicRepository
import com.x2t68.MusicPlayer.service.MusicService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class PlayerViewModel(
    private val repository: MusicRepository
) : ViewModel() {

    private var controllerFuture: ListenableFuture<MediaController>? = null
    private val _controller = MutableStateFlow<MediaController?>(null)
    val controller: StateFlow<MediaController?> = _controller.asStateFlow()

    private val _currentSong = MutableStateFlow<Song?>(null)
    val currentSong: StateFlow<Song?> = _currentSong.asStateFlow()

    private val _isFavorite = MutableStateFlow(false)
    val isFavorite: StateFlow<Boolean> = _isFavorite.asStateFlow()

    private val _currentQueue = MutableStateFlow<List<Song>>(emptyList())
    val currentQueue: StateFlow<List<Song>> = _currentQueue.asStateFlow()

    fun initWithContext(context: Context) {
        if (_controller.value != null) return
        _currentQueue.value = repository.getCurrentQueue()

        val sessionToken = SessionToken(context, ComponentName(context, MusicService::class.java))
        controllerFuture = MediaController.Builder(context, sessionToken).buildAsync()
        controllerFuture?.addListener({
            val ctrl = controllerFuture?.get()
            _controller.value = ctrl
            
            if (_currentQueue.value.isEmpty() && ctrl != null) {
                val list = mutableListOf<Song>()
                for (i in 0 until ctrl.mediaItemCount) {
                    val item = ctrl.getMediaItemAt(i)
                    val metadata = item.mediaMetadata
                    list.add(Song(
                        id = item.mediaId.toLongOrNull() ?: 0L,
                        title = metadata.title?.toString(),
                        artist = metadata.artist?.toString(),
                        albumId = 0L,
                        data = item.localConfiguration?.uri?.toString() ?: ""
                    ))
                }
                _currentQueue.value = list
            }

            observeCurrentMediaItem()
        }, MoreExecutors.directExecutor())
    }

    fun playNewList(songs: List<Song>, startIndex: Int) {
        repository.setCurrentQueue(songs)
        _currentQueue.value = songs
        
        val ctrl = _controller.value ?: return
        ctrl.stop()
        ctrl.clearMediaItems()
        ctrl.setMediaItems(songs.map { 
            MediaItem.Builder()
                .setUri(ContentUris.withAppendedId(
                    MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, it.id
                ))
                .setMediaId(it.id.toString())
                .setMediaMetadata(
                    androidx.media3.common.MediaMetadata.Builder()
                        .setTitle(it.title)
                        .setArtist(it.artist)
                        .setArtworkUri(ContentUris.withAppendedId(
                            Uri.parse("content://media/external/audio/albumart"), it.albumId
                        ))
                        .build()
                )
                .build() 
        }, startIndex, 0L)
        ctrl.prepare()
        ctrl.play()
    }

    private fun observeCurrentMediaItem() {
        val ctrl = _controller.value ?: return
        
        updateCurrentSong(ctrl.currentMediaItem)

        ctrl.addListener(object : Player.Listener {
            override fun onMediaItemTransition(mediaItem: MediaItem?, reason: Int) {
                updateCurrentSong(mediaItem)
            }

            override fun onPlaybackStateChanged(playbackState: Int) {
                if (playbackState == Player.STATE_READY) {
                    updateCurrentSong(ctrl.currentMediaItem)
                }
            }
        })
    }

    private fun updateCurrentSong(mediaItem: MediaItem?) {
        val mediaId = mediaItem?.mediaId?.toLongOrNull()
        if (mediaId == null) {
            _currentSong.value = null
            return
        }
        val song = _currentQueue.value.find { it.id == mediaId }
        _currentSong.value = song
        checkFavorite(mediaId)
    }

    fun toggleFavorite(song: Song) {
        viewModelScope.launch {
            val current = _isFavorite.value
            if (current) {
                repository.removeFavorite(song)
            } else {
                repository.addFavorite(song)
            }
        }
    }

    fun checkFavorite(songId: Long) {
        viewModelScope.launch {
            repository.isFavorite(songId).collect {
                _isFavorite.value = it
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        controllerFuture?.let {
            MediaController.releaseFuture(it)
        }
    }
}