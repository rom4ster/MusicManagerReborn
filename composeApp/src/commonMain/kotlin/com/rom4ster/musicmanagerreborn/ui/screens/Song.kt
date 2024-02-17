package com.rom4ster.musicmanagerreborn.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.MusicNote
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import com.rom4ster.musicmanagerreborn.database.AlbumInfo
import com.rom4ster.musicmanagerreborn.ui.state.SongState
import com.rom4ster.musicmanagerreborn.utils.SONG_SQUARE_HEIGHT_MULTIPLIER
import com.rom4ster.musicmanagerreborn.utils.SONG_SQUARE_WIDTH_MULTIPLIER
import com.rom4ster.musicmanagerreborn.utils.getScreenDim
import kotlinx.coroutines.flow.MutableStateFlow

class Song(val state: MutableStateFlow<SongState>) : Screen {
    @Composable
    override fun Content() {
        val state by this.state.collectAsState()
        Column(
            Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            getScreenDim().let {
                (SONG_SQUARE_HEIGHT_MULTIPLIER *it.height.value).coerceAtMost(it.width.value * SONG_SQUARE_WIDTH_MULTIPLIER).let {side ->
                    Box(
                        modifier = Modifier
                            .size(side.dp)
                            .background(
                                color = MaterialTheme.colorScheme.surfaceVariant
                            ).align(Alignment.CenterHorizontally)
                    ) {
                        Image(
                            Icons.Outlined.MusicNote,
                            "Image", //TODO album image...
                            modifier = Modifier.fillMaxSize()
                        )

                    }
                }
            }
            Text(state.name)
            state.info?.let { Text(it.artist) }
            state.uploader?.let { Text(it) }
            state.info?.albumName?.let { Text(it) }
            state.info?.let { Text(it.releaseDate) }
            state.metadata?.genres?.joinToString( ",")?.let { Text(it) }
        }
    }
}