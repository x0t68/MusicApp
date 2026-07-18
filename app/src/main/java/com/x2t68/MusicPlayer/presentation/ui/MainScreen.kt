package com.x2t68.MusicPlayer.presentation.ui

import androidx.activity.compose.BackHandler
import android.Manifest
import android.os.Build
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Place
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.x2t68.MusicPlayer.R
import com.x2t68.MusicPlayer.data.Song
import com.x2t68.MusicPlayer.presentation.viewmodel.MusicViewModel
import com.x2t68.MusicPlayer.presentation.viewmodel.ViewModelFactory
import com.x2t68.MusicPlayer.presentation.ui.drawer.AboutDrawerContent
import com.x2t68.MusicPlayer.presentation.ui.search.CustomSearchBar
import com.x2t68.MusicPlayer.theme.SongList
import kotlinx.coroutines.launch

@OptIn(ExperimentalPermissionsApi::class, ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    onSongClick: (songs: List<Song>, position: Int) -> Unit
) {
    val context = LocalContext.current
    val viewModel: MusicViewModel = viewModel(factory = ViewModelFactory(context))
    
    val songs by viewModel.songs.collectAsState()
    val favorites by viewModel.favorites.collectAsState()
    val folders by viewModel.folders.collectAsState()
    val albums by viewModel.albums.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()
    val searchResults by viewModel.searchResults.collectAsState()
    val suggestions by viewModel.suggestions.collectAsState()

    var isSearchActive by remember { mutableStateOf(false) }

    val permission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        Manifest.permission.READ_MEDIA_AUDIO
    } else {
        Manifest.permission.READ_EXTERNAL_STORAGE
    }
    val permissionState = rememberPermissionState(permission)

    var selectedTab by remember { mutableIntStateOf(0) }
    val tabs = listOf("Songs", "Folders", "Albums", "Favorites")

    var currentFolderSongs by remember { mutableStateOf<List<Song>?>(null) }
    var currentAlbumSongs by remember { mutableStateOf<List<Song>?>(null) }

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    BackHandler(enabled = drawerState.isOpen) {
        scope.launch { drawerState.close() }
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            AboutDrawerContent(
                context = context,
                onItemClick = { scope.launch { drawerState.close() } }
            )
        }
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            Image(
                painter = painterResource(R.drawable.mainscreenbgs),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxSize()
                    .then(if (drawerState.isOpen) Modifier.blur(10.dp) else Modifier),
                contentScale = ContentScale.Crop
            )

            Column(Modifier.fillMaxSize()) {
                Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 40.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                IconButton(
                    onClick = { scope.launch { drawerState.open() } },
                    modifier = Modifier.padding(top = 8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Menu,
                        contentDescription = "Menu",
                        tint = Color.White
                    )
                }

                CustomSearchBar(
                    query = searchQuery,
                    onQueryChange = { viewModel.onSearchQueryChanged(it) },
                    onSearch = { 
                        viewModel.performSearch(it)
                        isSearchActive = false 
                    },
                    active = isSearchActive,
                    onActiveChange = { isSearchActive = it },
                    suggestions = suggestions,
                    modifier = Modifier
                        .fillMaxWidth(0.85f)
                        .heightIn(max = if (isSearchActive) 400.dp else 56.dp)
                )
            }

                Spacer(modifier = Modifier.height(24.dp))

                if (!isSearchActive && searchQuery.isNotEmpty()) {
                    CategorySongsView(
                        title = "Search Results",
                        songs = searchResults,
                        onBack = { viewModel.onSearchQueryChanged("") },
                        onSongClick = onSongClick
                    )
                } else if (!isSearchActive) {
                    if (permissionState.status.isGranted) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 8.dp),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            tabs.forEachIndexed { index, title ->
                                val isSelected = selectedTab == index
                                Surface(
                                    onClick = {
                                        selectedTab = index
                                        currentFolderSongs = null
                                        currentAlbumSongs = null
                                    },
                                    shape = RoundedCornerShape(16.dp),
                                    color = if (isSelected) Color.White.copy(alpha = 0.2f) else Color.Transparent,
                                    border = if (isSelected) BorderStroke(1.dp, Color.White) else null,
                                    modifier = Modifier.height(40.dp)
                                ) {
                                    Box(
                                        contentAlignment = Alignment.Center,
                                        modifier = Modifier.padding(horizontal = 16.dp)
                                    ) {
                                        Text(
                                            text = title,
                                            color = if (isSelected) Color.White else Color.White.copy(alpha = 0.6f),
                                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                            fontSize = 14.sp
                                        )
                                    }
                                }
                            }
                        }
                    }

                    if (!permissionState.status.isGranted) {
                        Button(
                            onClick = { permissionState.launchPermissionRequest() },
                            modifier = Modifier
                                .padding(20.dp)
                                .align(Alignment.CenterHorizontally)
                    ) {
                        Text(text = "Grant Permission")
                    }
                    } else {
                        when (selectedTab) {
                            0 -> SongList(songs = songs, onSongClick = { pos -> onSongClick(songs, pos) }, modifier = Modifier.weight(1f))
                            1 -> {
                                if (currentFolderSongs == null) {
                                    FoldersList(folders) { currentFolderSongs = it }
                                } else {
                                    CategorySongsView(
                                        title = "Folder Content",
                                        songs = currentFolderSongs!!,
                                        onBack = { currentFolderSongs = null },
                                        onSongClick = onSongClick
                                    )
                                }
                            }
                            2 -> {
                                if (currentAlbumSongs == null) {
                                    AlbumsList(albums) { currentAlbumSongs = it }
                                } else {
                                    CategorySongsView(
                                        title = "Album Content",
                                        songs = currentAlbumSongs!!,
                                        onBack = { currentAlbumSongs = null },
                                        onSongClick = onSongClick
                                    )
                                }
                            }
                            3 -> SongList(songs = favorites, onSongClick = { pos -> onSongClick(favorites, pos) }, modifier = Modifier.weight(1f))
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun FoldersList(folders: Map<String, List<Song>>, onFolderClick: (List<Song>) -> Unit) {
    val folderNames = remember(folders) { folders.keys.toList() }
    LazyColumn(modifier = Modifier.fillMaxSize()) {
        items(folderNames, key = { it }) { folderName ->
            ListItem(
                headlineContent = { Text(folderName, color = Color.White) },
                supportingContent = { Text("${folders[folderName]?.size ?: 0} songs", color = Color.LightGray) },
                leadingContent = { Icon(Icons.Default.Place, contentDescription = null, tint = Color.White) },
                modifier = Modifier.clickable { onFolderClick(folders[folderName] ?: emptyList()) },
                colors = ListItemDefaults.colors(containerColor = Color.Transparent)
            )
        }
    }
}

@Composable
fun AlbumsList(albums: Map<String, List<Song>>, onAlbumClick: (List<Song>) -> Unit) {
    val albumNames = remember(albums) { albums.keys.toList() }
    LazyColumn(modifier = Modifier.fillMaxSize()) {
        items(albumNames, key = { it }) { albumName ->
            ListItem(
                headlineContent = { Text(albumName, color = Color.White) },
                supportingContent = { Text("${albums[albumName]?.size ?: 0} songs", color = Color.LightGray) },
                leadingContent = { Icon(Icons.AutoMirrored.Filled.List, contentDescription = null, tint = Color.White) },
                modifier = Modifier.clickable { onAlbumClick(albums[albumName] ?: emptyList()) },
                colors = ListItemDefaults.colors(containerColor = Color.Transparent)
            )
        }
    }
}

@Composable
fun CategorySongsView(
    title: String,
    songs: List<Song>,
    onBack: () -> Unit,
    onSongClick: (List<Song>, Int) -> Unit
) {
    Column(modifier = Modifier.fillMaxSize()) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(16.dp)
        ) {
            IconButton(onClick = onBack) {
                Icon(Icons.Default.ArrowBack, contentDescription = null, tint = Color.White)
            }
            Text(title, color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Bold)
        }
        SongList(songs = songs, onSongClick = { pos -> onSongClick(songs, pos) }, modifier = Modifier.weight(1f))
    }
}
