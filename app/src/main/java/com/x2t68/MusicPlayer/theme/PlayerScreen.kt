package com.x2t68.MusicPlayer.theme

import android.R.attr.contentDescription
import android.content.ContentUris
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.x2t68.MusicPlayer.R
import com.x2t68.MusicPlayer.data.Song
import kotlin.random.Random
import kotlinx.coroutines.delay

@Composable
fun PlayerScreen(
    songList: List<Song>,
    initialIndex: Int = 0,
    onBack: () -> Unit
) {
    val context = LocalContext.current
    val exoPlayer = remember { ExoPlayer.Builder(context).build() }
    var currentIndex by rememberSaveable { mutableStateOf(initialIndex) }
    var isShuffle by rememberSaveable { mutableStateOf(false) }
    var isRepeat by rememberSaveable { mutableStateOf(false) }
    var isPlaying by remember { mutableStateOf(false) }
    var elapsed by remember { mutableStateOf(0L) }
    var duration by remember { mutableStateOf(0L) }
    var shuffledList by remember { mutableStateOf(songList) }

    val waveform = remember { getWaveform() }
    var waveformProgress by remember { mutableStateOf(0f) } // var بدل val

    LaunchedEffect(currentIndex, isShuffle) {
        val list = if (isShuffle) shuffledList else songList
        val song = list.getOrNull(currentIndex) ?: return@LaunchedEffect
        exoPlayer.setMediaItem(MediaItem.fromUri(song.data))
        exoPlayer.prepare()
        exoPlayer.playWhenReady = true
    }

    DisposableEffect(exoPlayer) {
        val listener = object : _root_ide_package_.androidx.media3.common.Player.Listener {
            override fun onIsPlayingChanged(isPlay: Boolean) {
                isPlaying = isPlay
            }

            override fun onPlaybackStateChanged(playbackState: Int) {
                if (playbackState == Player.STATE_READY) {
                    duration = exoPlayer.duration
                }
                if (playbackState == Player.STATE_ENDED) {
                    currentIndex = (currentIndex + 1) % (if (isShuffle) shuffledList.size
                    else songList.size)
                }
            }
        }
        exoPlayer.addListener(listener)
        onDispose {
            exoPlayer.release()
        }
    }

    // تحديث progress
    LaunchedEffect(isPlaying) {
        while (isPlaying) {
            waveformProgress = if (duration > 0) elapsed.toFloat() / duration.toFloat() else 0f
            delay(500)
        }
    }

    val song = (if (isShuffle) shuffledList else songList).getOrNull(currentIndex)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(
                        Color(0xff191c1f),
                        Color(0xff3c3c38)
                    )
                )
            )
    ) {
        song?.let {
            val albumUri = ContentUris.withAppendedId(
                Uri.parse("content://media/external/audio/albumart"), it.albumId
            )

            // الخلفية
            AsyncImage(
                model = ImageRequest.Builder(context)
                    .data(albumUri)
                    .build(),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxSize()
                    .blur(18.dp),
                contentScale = ContentScale.Crop,
                alpha = 0.40f,
                error = painterResource(R.drawable.baseline_music_note_24),
                placeholder = painterResource(R.drawable.baseline_music_note_24)
            )

            // الشريط العلوي (Back + Favorite)
            Row(Modifier.padding(horizontal = 16.dp, vertical = 48.dp)) {
                IconButton(
                    onBack,
                    modifier = Modifier
                        .size(48.dp)
                        .background(Color(0x30ffffff), shape = CircleShape)
                ) {
                    Icon(Icons.Default.ArrowBack, contentDescription = null, tint = Color.White)
                }
                Spacer(modifier = Modifier.weight(1f))
                IconButton(
                    onClick = {},
                    modifier = Modifier
                        .size(48.dp)
                        .background(Color(0x30ffffff), shape = CircleShape)
                ) {
                    Icon(Icons.Default.FavoriteBorder, contentDescription = null
                        ,
                        tint = Color.White)
                }
            }

            // صورة الألبوم + النصوص
            Column(
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(top = 90.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                AsyncImage(
                    model = ContentUris.withAppendedId(
                        Uri.parse("content://media/external/audio/albumart"),
                        it.albumId
                    ) ?: R.drawable.baseline_music_note_24,
                    contentDescription = null,
                    modifier = Modifier
                        .size(220.dp)
                        .clip(CircleShape)
                        .background(Color(0x30ffffff), shape = CircleShape),
                    contentScale = ContentScale.Crop,
                    error = painterResource(R.drawable.baseline_music_note_24),
                    placeholder = painterResource(R.drawable.baseline_music_note_24)
                )


                Text(
                    song?.title.orEmpty(),
                    fontSize = 30.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    color = Color.White,
                    modifier = Modifier.padding(top = 32.dp)
                )
                Text(
                    song.artist.orEmpty(),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    color = Color.White,
                    modifier = Modifier.padding(top = 16.dp)
                )
            }
Column(modifier = Modifier
    .fillMaxWidth()
    .align (Alignment.Center)
    .padding(horizontal = 24.dp)
    .padding(top = 320.dp),
    horizontalAlignment = Alignment.CenterHorizontally
) {
    WaveformBar(
        values = waveform,
        progress = waveformProgress,
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp)
    ) { percent->
        val seek=(percent*duration).toLong()
        exoPlayer.seekTo(seek)
        elapsed=seek
        waveformProgress=percent
    }
    Row (
        Modifier.fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 6.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ){

    }
Text(formatTime((elapsed/1000).toInt()),
    color = Color.White,
    fontSize = 13.sp)
    Text(formatTime((duration/1000).toInt()),
        color = Color.White,
        fontSize = 13.sp)


}
            // الأزرار بالأسفل
            Row(
                Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 54.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Repeat
                IconButton(onClick = {
                    isRepeat = !isRepeat
                    exoPlayer.repeatMode =
                        if (isRepeat) Player.REPEAT_MODE_ONE else Player.REPEAT_MODE_OFF
                }) {
                    Icon(
                        painter = painterResource(R.drawable.repeat_2_svgrepo_com),
                        contentDescription = null,
                        tint = if (isRepeat) Color(0xff9c27b0) else Color.White
                    )
                }



                // Previous
                IconButton(onClick = {
                    val list = if (isShuffle) shuffledList else songList
                    currentIndex = if (currentIndex - 1 < 0) list.size - 1 else currentIndex - 1
                }) {
                    Icon(
                        painter = painterResource(R.drawable.skip_backward_svgrepo_com),
                        contentDescription = null,
                        tint = Color.White
                    )
                }

                // Play / Pause
                IconButton(onClick = {
                    if (exoPlayer.isPlaying) exoPlayer.pause() else exoPlayer.play()
                }) {
                    Icon(
                        painterResource(
                            if (isPlaying) R.drawable.pause_svgrepo_com else
                                R.drawable.play_1003_svgrepo_com
                        ),
                        contentDescription = null,
                        tint = Color(0xff1a1a1a)
                    )
                }

                // Next
                IconButton(onClick = {
                    val list = if (isShuffle) shuffledList else songList
                    currentIndex = (currentIndex +1) % list.size
                }) {
                    Icon(
                        painter = painterResource(R.drawable.skip_forward_svgrepo_com),
                        contentDescription = null,
                        tint = Color.White
                    )
                }

                // Shuffle
                IconButton(onClick = {
                    isShuffle = !isShuffle
                    shuffledList = if (isShuffle) songList.shuffled() else songList
                }) {
                    Icon(
                        painter = painterResource(R.drawable.shuffle_svgrepo_com),
                        contentDescription = null,
                        tint = Color.White
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

fun formatTime(seconds:Int): String= String.format("%02d:%2d", seconds / 60, seconds % 60)