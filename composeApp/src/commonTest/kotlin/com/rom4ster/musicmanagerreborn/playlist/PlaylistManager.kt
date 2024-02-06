@file:Suppress("INLINE_FROM_HIGHER_PLATFORM")
package com.rom4ster.musicmanagerreborn.playlist

import com.rom4ster.musicmanagerreborn.database.ClassMaps
import com.rom4ster.musicmanagerreborn.database.Database
import com.rom4ster.musicmanagerreborn.database.Database.QueryDataResult
import com.rom4ster.musicmanagerreborn.database.PropertyEquality
import com.rom4ster.musicmanagerreborn.database.Song
import com.rom4ster.musicmanagerreborn.database.SongEntry
import com.rom4ster.musicmanagerreborn.database.UserPlaylist
import com.rom4ster.musicmanagerreborn.utils.generateUUID
import com.rom4ster.musicmanagerreborn.ytdlp.*
import io.kotest.core.spec.style.FreeSpec
import io.mockk.*
import org.koin.core.context.startKoin
import org.koin.core.qualifier.named
import org.koin.dsl.module
import org.koin.test.KoinTest
import org.koin.test.mock.MockProvider
import org.koin.test.mock.declareMock
import kotlin.reflect.KProperty1


class PlaylistManager : FreeSpec(), KoinTest {


    /*
    * val ytWrapper: YTDLP by inject()
val songDb: Database by inject(named(InjectableModules.SONG_DATABASE_NAME))
val playlistDb: Database by inject(named(InjectableModules.USER_PLAYLIST_DATABASE_NAME))
    * */
init {


    MockProvider.register {
        mockkClass(it)
    }
    val userPlaylistId = "whoCares"

    val playlists = setOf("https://www.youtube.com/playlist?list=PLbpi6ZahtOH437hZ79FoVgvGE2URCpmyH")
    val nochange = "nochange".generateUUID().toString()
    val queryRet = listOf(
        UserPlaylist(
            id=userPlaylistId,
            name="name",
            links= playlists,
            songs= setOf(
                SongEntry(
                    "change",
                    1,
                ),
                SongEntry(
                    nochange,
                    2
                )
            )
        )
    ).map {
        QueryDataResult(
            result = "",
            serializedResult = it
        )
    }
    declareMock<YTDLP> {
        every { getInfo(any()) } returns VideoInformation("", "", "")

        every { getBatchInfo(any()) } returns
                listOf(
                    VideoInformation("add", "sup", "holo"),
                    VideoInformation(nochange, "sup", "holo"),
                )
        justRun { downloadPlaylist(any()) }
        justRun { download(any()) }

    }

    val songdb = declareMock<Database>(named(InjectableModules.SONG_DATABASE_NAME)) {
         justRun { update(any(), any()) }
         justRun { add(any()) }

    }

    val userPlaylistDb = declareMock<Database>(named(InjectableModules.USER_PLAYLIST_DATABASE_NAME)) {
        justRun { update(any(), any()) }
        justRun { add(any()) }
        every { query<UserPlaylist>(
            PropertyEquality(
                UserPlaylist::id,
                userPlaylistId
            ),
            ClassMaps.propertiesOf(UserPlaylist::class).toList(),
            UserPlaylist.serializer()
        ) } returns queryRet
    }

    "Playlist Manager Test" {

        diff(userPlaylistId)

        verify(exactly = 1) { songdb.add(
            Song(
            "add",
            "sup",
            "$YT_FILE_DIR/add",
            "holo"
        )
        ) }

        val pl = queryRet.first().serializedResult ?: throw Exception()
        val pl2 = pl.copy(
            songs = pl.songs.toMutableSet().apply { this.add(
                SongEntry(
                "add",3
            )
            )
            this.remove(                SongEntry(
                "change",
                1,
            ))
            }
        )
        val pl3 = pl2.copy(
            removedSongs = setOf(
                SongEntry(
                    "change",
                    1,
                )
            ),
            songs = pl2.songs.toMutableSet().apply {
                this.remove(
                    SongEntry(
                    "change",
                    1,
                )
                )
            }
        )


        verifySequence  {
            playlistDb.query(    PropertyEquality(
                UserPlaylist::id,
                userPlaylistId
            ),
                ClassMaps.propertiesOf(UserPlaylist::class).toList(),
                UserPlaylist.serializer())
            playlistDb.update(userPlaylistId, pl2, any())
            playlistDb.update(userPlaylistId, pl3, any())
        }






    }
}

}