package com.rom4ster.musicmanagerreborn.ui.actions

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.rom4ster.musicmanagerreborn.ui.screens.Playlist
import com.rom4ster.musicmanagerreborn.ui.screens.Playlists
import com.rom4ster.musicmanagerreborn.ui.state.PlaylistState
import com.rom4ster.musicmanagerreborn.utils.add
import com.rom4ster.musicmanagerreborn.utils.asEnumValue
import com.rom4ster.musicmanagerreborn.utils.verifiedCast
import com.rom4ster.musicmanagerreborn.ytdlp.diff
import inject
import kotlinx.coroutines.flow.MutableStateFlow

class PlaylistsActionController : ActionController<Playlists> {




 public enum class Actions {
     OPEN_PLAYLIST,
     LOAD_FROM_DB,
     ADD_PLAYLIST,
     REMOVE_PLAYLIST,
     SYNC_PLAYLIST
 }




    @Composable
    override fun <Q> doAction(input: Playlists, actionCode: Int, actionData: Q?) {
        when(actionCode.asEnumValue<Actions>()) {
             Actions.OPEN_PLAYLIST -> {
                actionData
                    .verifiedCast<Q, PlaylistState>()
                    .let {
                        LocalNavigator.currentOrThrow += Playlist(MutableStateFlow(it)  )
                    }

            }
            Actions.LOAD_FROM_DB -> {
                println("input.playlists.compareAndSet(input.playlists.value, PlaylistState.getAll())")
            }
            Actions.ADD_PLAYLIST -> {
                PlaylistState.add(actionData.verifiedCast())
            }

            Actions.REMOVE_PLAYLIST -> {
                PlaylistState.delete(actionData.verifiedCast())
            }
            Actions.SYNC_PLAYLIST -> {
                println("diff(actionData.verifiedCast())")
            }

        }
    }






}