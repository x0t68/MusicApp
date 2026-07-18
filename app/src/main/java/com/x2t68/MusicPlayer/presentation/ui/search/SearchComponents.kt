package com.x2t68.MusicPlayer.presentation.ui.search

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomSearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
    onSearch: (String) -> Unit,
    active: Boolean,
    onActiveChange: (Boolean) -> Unit,
    suggestions: List<String>,
    modifier: Modifier = Modifier
) {
    SearchBar(
        query = query,
        onQueryChange = onQueryChange,
        onSearch = onSearch,
        active = active,
        onActiveChange = onActiveChange,
        placeholder = { Text("Search songs...", color = Color.White.copy(alpha = 0.7f)) },
        leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = Color.White) },
        trailingIcon = {
            if (active || query.isNotEmpty()) {
                IconButton(onClick = {
                    if (query.isNotEmpty()) onQueryChange("")
                    else onActiveChange(false)
                }) {
                    Icon(Icons.Default.Close, contentDescription = null, tint = Color.White)
                }
            }
        },
        modifier = modifier,
        colors = SearchBarDefaults.colors(
            containerColor = Color.White.copy(alpha = 0.15f),
            inputFieldColors = TextFieldDefaults.colors(
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White,
                cursorColor = Color.White
            )
        )
    ) {
        Box(modifier = Modifier
            .fillMaxSize()
            .background(Color.Transparent)) {
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(suggestions) { suggestion ->
                    ListItem(
                        headlineContent = { Text(suggestion, color = Color.White) },
                        leadingContent = { Icon(Icons.Default.Search, contentDescription = null, tint = Color.White) },
                        modifier = Modifier.clickable {
                            onSearch(suggestion)
                        },
                        colors = ListItemDefaults.colors(containerColor = Color.Transparent)
                    )
                }
            }
        }
    }
}
