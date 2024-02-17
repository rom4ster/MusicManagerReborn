package com.rom4ster.musicmanagerreborn.ui.state

import com.rom4ster.musicmanagerreborn.database.*
import com.rom4ster.musicmanagerreborn.utils.EXPRESSIONCONSTANTS
import com.rom4ster.musicmanagerreborn.utils.allProps
import com.rom4ster.musicmanagerreborn.utils.generateUUID
import com.rom4ster.musicmanagerreborn.utils.names
import com.rom4ster.musicmanagerreborn.utils.types
import inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.ListSerializer
import org.koin.core.qualifier.named
import kotlin.reflect.full.memberProperties
import kotlin.reflect.full.primaryConstructor


val songDb: Database by inject(named(InjectableModules.SONG_DATABASE_NAME))
val playlistDb: Database by inject(named(InjectableModules.USER_PLAYLIST_DATABASE_NAME))








data class HomeState(
    val display: String
)


@Serializable
data class PlaylistState(
    val id: String,
    val name: String,
    val links: Set<String>,
    val songs: Map<SongEntry, SongState> = mapOf()
) {

    constructor(name: String, links: Set<String>) : this(name.generateUUID().toString(), name, links)
    companion object {

        val names = PlaylistState::class.names()
        val types = PlaylistState::class.types()


        @Serializable
        data class  PlaylistStateInternal(
            val id: String,
            val name: String,
            val links: Set<String>,
            val songs: Set<SongEntry>
        )

        fun add(vararg states: PlaylistState) {
            states.forEach {
                UserPlaylist(
                    it.id,
                    it.name,
                    it.links,
                ).let { pl ->
                    println("add" +pl)
                    //playlistDb.add(pl)
                }
            }
        }

        fun update(state: PlaylistState) {
            val removed = playlistDb.query(
                PropertyEquality(UserPlaylist::id, state.id),
                UserPlaylist::removedSongs,
                serializer = ListSerializer(SongEntry.serializer())
            ).map {
                it.serializedResult as SongEntry
            }

            UserPlaylist(
                state.id,
                state.name,
                state.links,
                removed.toSet(),
                state.songs.keys
            ).let {
                println("update ${it.id} $it")
                //playlistDb.update(it.id, it)
            }
        }

        fun delete(vararg states: PlaylistState) {
            states.forEach {
                //playlistDb.remove(it.id, UserPlaylist(it.id, it.name,it.links))
                println("remove" + it)
            }
        }
        fun getById(id: String)  = (
                playlistDb.query(
            PropertyEquality(UserPlaylist::id, id),
            UserPlaylist::id,
            UserPlaylist::name,
            UserPlaylist::links,
            UserPlaylist::songs,
            serializer = PlaylistStateInternal.serializer()
            )
            .first()
            .serializedResult as PlaylistStateInternal
                ).let {
                    PlaylistState(
                        it.id,
                        it.name,
                        it.links,
                        it.songs.associateWith { entry ->
                            SongState.getById(entry.id)
                        }
                    )
            }

        fun getAll() = playlistDb.query(
            ConstantObject(EXPRESSIONCONSTANTS.TRUE),
            UserPlaylist::id,
            UserPlaylist::name,
            UserPlaylist::links,
            UserPlaylist::songs,
            serializer = PlaylistStateInternal.serializer()
            ).map {
                val data = it.serializedResult as PlaylistStateInternal
            PlaylistState(
                data.id,
                data.name,
                data.links,
                data.songs.associateWith { entry ->
                    SongState.getById(entry.id)
                }
            )
        }

    }
}



@Serializable
data class SongState(
    val id: String,
    val name: String,
    val filePath: String?,
    val uploader: String?,
    val info: AlbumInfo? = null,
    val metadata: SongMetadata? = null,
) {

    companion object {

        fun getById(id: String): SongState = songDb.query(
            PropertyEquality(Song::id, id),
            *allProps<Song>(),
            serializer =  SongState.serializer()
        )
            .first()
            .serializedResult as SongState

        fun add(vararg states: SongState) {
            states.forEach {
                Song(
                    it.id,
                    it.name,
                    it.filePath,
                    it.uploader,
                    it.info,
                    it.metadata
                ).let { song ->
                    println("add" +song)
                    //songDb.add(song)
                }
            }
        }

        fun delete(vararg states: SongState) {
            states.forEach {
                songDb.remove(
                    it.id,
                    Song(
                        it.id,
                        it.name,
                        it.filePath,
                        it.uploader,
                        it.info,
                        it.metadata
                    )
                )
                println("remove" + it)
            }
        }
    }
}



