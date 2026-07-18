package com.x2t68.MusicPlayer.presentation.ui

import android.content.ContentUris
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.x2t68.MusicPlayer.R
import com.x2t68.MusicPlayer.data.Song
import com.x2t68.MusicPlayer.presentation.viewmodel.PlayerViewModel
import com.x2t68.MusicPlayer.presentation.viewmodel.ViewModelFactory
import com.x2t68.MusicPlayer.theme.WaveformBar
import kotlinx.coroutines.delay
import kotlin.random.Random

@Composable
fun PlayerScreen(
    songList: List<Song>,
    initialIndex: Int = 0,
    isNewSelection: Boolean = false,
    onBack: () -> Unit
) {
    val context = LocalContext.current
    val viewModel: PlayerViewModel = viewModel(factory = ViewModelFactory(context))

    val controller by viewModel.controller.collectAsState()
    val isFavorite by viewModel.isFavorite.collectAsState()
    val currentQueue by viewModel.currentQueue.collectAsState()

    var currentIndex by remember { mutableIntStateOf(initialIndex) }
    var isPlaying by remember { mutableStateOf(false) }
    var isShuffle by remember { mutableStateOf(false) }
    var repeatMode by remember { mutableIntStateOf(Player.REPEAT_MODE_OFF) }

    var elapsed by remember { mutableLongStateOf(0L) }
    var duration by remember { mutableLongStateOf(0L) }

    val waveform = remember { getWaveform() }
    var waveformProgress by remember { mutableFloatStateOf(0f) }

    val activeList = if (songList.isNotEmpty()) songList else currentQueue
    val currentSong = activeList.getOrNull(currentIndex)

    LaunchedEffect(Unit) {
        viewModel.initWithContext(context, songList, initialIndex, isNewSelection)
    }

    LaunchedEffect(controller) {
        val ctrl = controller ?: return@LaunchedEffect

        currentIndex = ctrl.currentMediaItemIndex
        isShuffle = ctrl.shuffleModeEnabled
        repeatMode = ctrl.repeatMode
        isPlaying = ctrl.isPlaying
        duration = ctrl.duration.coerceAtLeast(0L)
        elapsed = ctrl.currentPosition.coerceAtLeast(0L)
    }

    DisposableEffect(controller) {
        val listener = object : Player.Listener {
            override fun onIsPlayingChanged(isPlay: Boolean) {
                isPlaying = isPlay
            }
            override fun onPlaybackStateChanged(state: Int) {
                if (state == Player.STATE_READY) {
                    duration = controller?.duration?.coerceAtLeast(0L) ?: 0L
                }
            }
            override fun onMediaItemTransition(mediaItem: MediaItem?, reason: Int) {
                controller?.currentMediaItemIndex?.let {
                    currentIndex = it
                    activeList.getOrNull(it)?.let { song ->
                        viewModel.checkFavorite(song.id)
                    }
                }
            }
            override fun onShuffleModeEnabledChanged(shuffleModeEnabled: Boolean) {
                isShuffle = shuffleModeEnabled
            }
            override fun onRepeatModeChanged(newRepeatMode: Int) {
                repeatMode = newRepeatMode
            }
        }
        controller?.addListener(listener)
        onDispose { controller?.removeListener(listener) }
    }

    LaunchedEffect(isPlaying, currentIndex) {
        while (isPlaying) {
            val currentPos = controller?.currentPosition ?: 0L
            elapsed = currentPos
            waveformProgress = if (duration > 0) currentPos.toFloat() / duration.toFloat() else 0f
            delay(250)
        }
    }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Image(
            painter = painterResource(R.drawable.mainscreenbgs),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        currentSong?.let { song ->
            val albumUri = ContentUris.withAppendedId(
                Uri.parse("content://media/external/audio/albumart"), song.albumId
            )

            AsyncImage(
                model = ImageRequest.Builder(context)
                    .data(albumUri)
                    .build(),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxSize()
                    .blur(24.dp),
                contentScale = ContentScale.Crop,
                alpha = 0.35f,
                error = painterResource(R.drawable.baseline_music_note_24),
                placeholder = painterResource(R.drawable.baseline_music_note_24)
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 48.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = onBack,
                    modifier = Modifier.size(48.dp).background(Color(0x20ffffff), shape = CircleShape)
                ) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null, tint = Color.White)
                }
                Spacer(modifier = Modifier.weight(1f))
                IconButton(
                    onClick = { viewModel.toggleFavorite(song) },
                    modifier = Modifier.size(48.dp).background(Color(0x20ffffff), shape = CircleShape)
                ) {
                    Icon(
                        imageVector = if (isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                        contentDescription = null,
                        tint = if (isFavorite) Color.Red else Color.White
                    )
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.TopCenter)
                    .padding(top = 110.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                AsyncImage(
                    model = albumUri,
                    contentDescription = null,
                    modifier = Modifier
                        .size(240.dp)
                        .clip(CircleShape)
                        .background(Color(0x15ffffff), shape = CircleShape),
                    contentScale = ContentScale.Crop,
                    error = painterResource(R.drawable.baseline_music_note_24),
                    placeholder = painterResource(R.drawable.baseline_music_note_24)
                )

                Text(
                    text = song.title.orEmpty(),
                    fontSize = 26.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    color = Color.White,
                    modifier = Modifier
                        .padding(top = 32.dp)
                        .padding(horizontal = 24.dp),
                    maxLines = 1
                )
                Text(
                    text = song.artist.orEmpty(),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Medium,
                    textAlign = TextAlign.Center,
                    color = Color.White.copy(alpha = 0.7f),
                    modifier = Modifier.padding(top = 8.dp).padding(horizontal = 24.dp),
                    maxLines = 1
                )
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
                    .padding(horizontal = 24.dp)
                    .padding(bottom = 160.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                WaveformBar(
                    values = waveform,
                    progress = waveformProgress,
                    modifier = Modifier.fillMaxWidth().height(60.dp)
                ) { percent ->
                    val seek = (percent * duration).toLong()
                    controller?.seekTo(seek)
                    elapsed = seek
                    waveformProgress = percent
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp)
                        .padding(horizontal = 4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = formatTime((elapsed / 1000).toInt()), color = Color.White.copy(alpha = 0.8f), fontSize = 13.sp)
                    Text(text = formatTime((duration / 1000).toInt()), color = Color.White.copy(alpha = 0.8f), fontSize = 13.sp)
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 54.dp),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = {
                    val nextMode = when(repeatMode) {
                        Player.REPEAT_MODE_OFF -> Player.REPEAT_MODE_ONE
                        else -> Player.REPEAT_MODE_OFF
                    }
                    controller?.repeatMode = nextMode
                }) {
                    Icon(
                        painter = painterResource(R.drawable.repeat_2_svgrepo_com),
                        contentDescription = null,
                        tint = if (repeatMode != Player.REPEAT_MODE_OFF) Color(0xff9c27b0) else Color.White
                    )
                }

                IconButton(onClick = { controller?.seekToPrevious() }) {
                    Icon(painterResource(R.drawable.skip_backward_svgrepo_com), contentDescription = null, tint = Color.White)
                }

                IconButton(
                    onClick = { if (isPlaying) controller?.pause() else controller?.play() },
                    modifier = Modifier.size(64.dp)
                ) {
                    Icon(
                        painter = painterResource(if (isPlaying) R.drawable.pause_svgrepo_com else R.drawable.play_1003_svgrepo_com),
                        contentDescription = null,
                        modifier = Modifier.size(48.dp),
                        tint = Color.White
                    )
                }

                IconButton(onClick = { controller?.seekToNext() }) {
                    Icon(painterResource(R.drawable.skip_forward_svgrepo_com), contentDescription = null, tint = Color.White)
                }

                IconButton(onClick = {
                    controller?.shuffleModeEnabled = !isShuffle
                }) {
                    Icon(
                        painter = painterResource(R.drawable.shuffle_svgrepo_com),
                        contentDescription = null,
                        tint = if (isShuffle) Color(0xff9c27b0) else Color.White
                    )
                }
            }
        }
    }
}

fun getWaveform(): IntArray {
    val random = Random(System.currentTimeMillis())
    return IntArray(50) { 5 + random.nextInt(50) }
}

fun formatTime(seconds: Int): String = String.format("%02d:%02d", seconds / 60, seconds % 60)
