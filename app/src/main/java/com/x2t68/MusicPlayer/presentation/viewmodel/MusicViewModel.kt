package com.x2t68.MusicPlayer.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.x2t68.MusicPlayer.data.Song
import com.x2t68.MusicPlayer.data.repository.MusicRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MusicViewModel(private val repository: MusicRepository) : ViewModel() {

    private val _songs = MutableStateFlow<List<Song>>(emptyList())
    val songs: StateFlow<List<Song>> = _songs.asStateFlow()

    private val _favorites = MutableStateFlow<List<Song>>(emptyList())
    val favorites: StateFlow<List<Song>> = _favorites.asStateFlow()

    private val _folders = MutableStateFlow<Map<String, List<Song>>>(emptyMap())
    val folders: StateFlow<Map<String, List<Song>>> = _folders.asStateFlow()

    private val _albums = MutableStateFlow<Map<String, List<Song>>>(emptyMap())
    val albums: StateFlow<Map<String, List<Song>>> = _albums.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _searchResults = MutableStateFlow<List<Song>>(emptyList())
    val searchResults: StateFlow<List<Song>> = _searchResults.asStateFlow()

    private val _suggestions = MutableStateFlow<List<String>>(emptyList())
    val suggestions: StateFlow<List<String>> = _suggestions.asStateFlow()

    init {
        loadSongs()
        observeFavorites()
    }

    fun onSearchQueryChanged(query: String) {
        _searchQuery.value = query
        if (query.isBlank()) {
            _suggestions.value = emptyList()
            _searchResults.value = emptyList()
        } else {
            val filteredSuggestions = _songs.value
                .mapNotNull { it.title }
                .filter { it.contains(query, ignoreCase = true) }
                .distinct()
                .take(5)
            _suggestions.value = filteredSuggestions

            _searchResults.value = _songs.value.filter {
                it.title?.contains(query, ignoreCase = true) == true ||
                it.artist?.contains(query, ignoreCase = true) == true
            }
        }
    }

    fun performSearch(query: String) {
        _searchQuery.value = query
        _suggestions.value = emptyList()
        _searchResults.value = _songs.value.filter {
            it.title?.contains(query, ignoreCase = true) == true ||
            it.artist?.contains(query, ignoreCase = true) == true
        }
    }

    private fun loadSongs() {
        viewModelScope.launch {
            val allSongs = withContext(Dispatchers.IO) {
                repository.getAllSongs()
            }
            _songs.value = allSongs
            _folders.value = allSongs.groupBy { it.folder ?: "Unknown" }
            _albums.value = allSongs.groupBy { it.album ?: "Unknown" }
        }
    }

    private fun observeFavorites() {
        viewModelScope.launch {
            repository.getFavorites().collect {
                _favorites.value = it
            }
        }
    }
}