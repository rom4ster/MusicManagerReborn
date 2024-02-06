package com.rom4ster.musicmanagerreborn.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import com.rom4ster.musicmanagerreborn.ui.sqlib.SquircleShape
import com.rom4ster.musicmanagerreborn.ui.state.HomeState
import com.rom4ster.musicmanagerreborn.ui.state.PlaylistState
import com.rom4ster.musicmanagerreborn.utils.GRID_MIN_CELL_WIDTH
import com.rom4ster.musicmanagerreborn.utils.PLAYLISTS_GRID_ADD_BUTTON_BOTTOM_PADDING
import com.rom4ster.musicmanagerreborn.utils.PLAYLISTS_GRID_ADD_BUTTON_END_PADDING
import com.rom4ster.musicmanagerreborn.utils.PLAYLIST_CARD_WIDTH
import com.rom4ster.musicmanagerreborn.utils.getScreenDim
import kotlinx.coroutines.flow.MutableStateFlow

class Playlist(var state: MutableStateFlow<PlaylistState>) : Screen {







    @Composable
    override fun Content() {

        val state by state.collectAsState()
        val songs = state.songs.toList().sortedBy { it.first.ordinal }
       Box(
           modifier = Modifier.fillMaxWidth().fillMaxHeight()
       ) {
            LazyColumn(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                items(songs.size) { index ->
                    val song = songs[index]
                    Card(modifier = Modifier.fillMaxWidth(PLAYLIST_CARD_WIDTH)) {
                        Column {
                            Text(song.second.name)
                            song.second.uploader?.let {
                                Text(it)
                            }
                        }
                    }
                }
            }
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
                    .padding(
                        end = PLAYLISTS_GRID_ADD_BUTTON_END_PADDING.dp,
                        bottom = PLAYLISTS_GRID_ADD_BUTTON_BOTTOM_PADDING.dp
                    )
            ) {
                Button(
                    modifier = Modifier.align(Alignment.End).size(50.dp),
                    shape = SquircleShape(), //TODO hopefully this guy https://github.com/stoyan-vuchev/squircle-shape/issues can update his lib to actually work until then custom impl
                    contentPadding = PaddingValues(0.dp),
                    onClick = {

                    }
                ) {
                    Icon(Icons.Default.Add, "addIcon", modifier = Modifier)
                }
            }
        }

    }
}