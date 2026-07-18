package com.x2t68.MusicPlayer.presentation.ui.drawer

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun AboutDrawerContent(
    context: Context,
    onItemClick: () -> Unit = {}
) {
    ModalDrawerSheet(
        modifier = Modifier.width(280.dp),
        drawerContainerColor = MaterialTheme.colorScheme.surface,
        drawerContentColor = MaterialTheme.colorScheme.onSurface
    ) {
        Spacer(Modifier.height(24.dp))
        Text(
            "About App",
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )
        HorizontalDivider(
            modifier = Modifier.padding(vertical = 8.dp),
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f)
        )
        
        Spacer(Modifier.height(16.dp))

        NavigationDrawerItem(
            label = { Text("Developed by Taha Munla Ali") },
            selected = false,
            onClick = { onItemClick() },
            icon = { Icon(Icons.Default.Info, contentDescription = null) },
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
        )
        
        Spacer(Modifier.height(12.dp))

        NavigationDrawerItem(
            label = { Text("GitHub") },
            selected = false,
            onClick = {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/x0t68"))
                context.startActivity(intent)
                onItemClick()
            },
            icon = { Icon(Icons.Default.MoreVert, contentDescription = null) },
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
        )
        
        Spacer(Modifier.height(12.dp))

        NavigationDrawerItem(
            label = { Text("LinkedIn") },
            selected = false,
            onClick = {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.linkedin.com/in/taha-m-3768111b3/"))
                context.startActivity(intent)
                onItemClick()
            },
            icon = { Icon(Icons.Default.MoreVert, contentDescription = null) },
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
        )
    }
}
