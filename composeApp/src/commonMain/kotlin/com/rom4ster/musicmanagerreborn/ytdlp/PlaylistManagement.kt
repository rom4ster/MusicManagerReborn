package com.rom4ster.musicmanagerreborn.ytdlp

import InjectableModules
import com.benasher44.uuid.uuidFrom
import com.rom4ster.musicmanagerreborn.database.*
import com.rom4ster.musicmanagerreborn.error.DuplicateKeyException
import com.rom4ster.musicmanagerreborn.utils.generateUUID
import inject
import org.koin.core.qualifier.named


val ytWrapper: YTDLP by inject()
val songDb: Database by inject(named(InjectableModules.SONG_DATABASE_NAME))
val playlistDb: Database by inject(named(InjectableModules.USER_PLAYLIST_DATABASE_NAME))




fun getSongsFromPlaylists(vararg links: String) =
    links.flatMap { link ->
        ytWrapper.getBatchInfo(link)
    }

fun getPlaylist(userPlaylistId: String) = playlistDb.query(
    PropertyEquality(
        UserPlaylist::id,
        userPlaylistId
    ),
    ClassMaps.propertiesOf(UserPlaylist::class).toList(),
    UserPlaylist.serializer()


).apply {
    require(this.size == 1) { "Should not have found more than one playlist" }
}.first().serializedResult.let { playlist ->
    requireNotNull(playlist) { "playlist should not be null" }
    playlist
}



fun diff(userPlaylistId: String) {

    val playlist = getPlaylist(userPlaylistId) // get playlist to diff
    val remote = getSongsFromPlaylists(*playlist.links.toTypedArray()).toMutableList() //get song info in playlist
    val local = playlist.songs.toMutableList() //get stored song information
    val localIds = local.map { it.id }

    val playlistSongs = playlist.songs.toMutableSet()



    // remove intersecting songs from local/remote lists
    val removeTheseFromRemote = mutableListOf<VideoInformation>()

    remote.forEach {
        if (localIds.contains(it.id)) {
            removeTheseFromRemote.add(it) //queue shared songs for removal from both lists
            local.find { l -> l.id == it.id }.let { res -> local.remove(res) }

        }
    }
    removeTheseFromRemote.forEach { remote.remove(it) }


    // Leftover songs in remote are new, so add them

    remote.forEach {
        //add new songs to global list
        try {
            songDb.add(
                Song(
                    it.id,
                    it.title,
                    "$YT_FILE_DIR/${it.id}",
                    it.uploader
                )
            )
        }
        catch (_: DuplicateKeyException) {
            // if song already exists in global list then
            // there is no issue
        }
        // during add, download song file
        ytWrapper.download("https://www.youtube.com/watch?v=${it.id}")

        // add song to playlist and update database with change
        playlistSongs.add(
            SongEntry(
                it.id,
                playlistSongs.maxBy { s -> s.ordinal }.ordinal +1
            )
        )

        playlistDb.update(
            playlist.id,
            playlist.copy(
            songs = playlistSongs
        )
        )

        // any remaining songs in local list that are not supposed to be ignored must be moved to removed songs list
        // global songs do not need to removed here
        val playlistRemovedSongs = playlist.removedSongs.toMutableSet()

        local.forEach { entry ->
            if (!entry.ignoreSync) {
                playlistSongs.remove(entry)
                playlistRemovedSongs.add(entry)
            }
        }


        playlistDb.update(
            playlist.id,
            playlist.copy(removedSongs = playlistRemovedSongs, songs = playlistSongs)
        )


    }



}


