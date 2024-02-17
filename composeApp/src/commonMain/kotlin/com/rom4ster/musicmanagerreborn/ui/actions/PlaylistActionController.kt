package com.rom4ster.musicmanagerreborn.ui.actions

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.rom4ster.musicmanagerreborn.ui.screens.Playlist
import com.rom4ster.musicmanagerreborn.ui.screens.Playlists
import com.rom4ster.musicmanagerreborn.ui.screens.Song
import com.rom4ster.musicmanagerreborn.ui.state.PlaylistState
import com.rom4ster.musicmanagerreborn.ui.state.SongState
import com.rom4ster.musicmanagerreborn.utils.asEnumValue
import com.rom4ster.musicmanagerreborn.utils.verifiedCast
import kotlinx.coroutines.flow.MutableStateFlow

class PlaylistActionController : ActionController<Playlist> {

    public enum class Actions {
        OPEN_SONG,
        LOAD_FROM_DB,
        ADD_SONG,
        REMOVE_SONG,
    }
    @Composable
    override fun <Q> doAction(input: Playlist, actionCode: Int, actionData: Q?) {
        when(actionCode.asEnumValue<Actions>()) {
            Actions.OPEN_SONG -> {
                actionData
                    .verifiedCast<Q, SongState>()
                    .let {
                        LocalNavigator.currentOrThrow += Song(MutableStateFlow(it))
                    }
            }
            Actions.LOAD_FROM_DB -> {
                println("input.state.compareAndSet(input.state.value, PlaylistState.getById(input.state.value.id))")
            }
            Actions.ADD_SONG -> {}
            Actions.REMOVE_SONG -> {}
        }
    }
}