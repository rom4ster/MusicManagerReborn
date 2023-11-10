package com.rom4ster.musicmanagerreborn.database

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable



@Serializable
data class AlbumInfo(
    val artist: String,
    @SerialName("release-date")
    val releaseDate: String,
    @SerialName("album-name")
    val albumName: String? = null
)

@Serializable
data class SongMetadata(
    val genres: List<String>
)

@Serializable
data class Song (
    val id: String,
    val name: String,
    val filePath: String?,
    val uploader: String?,
    val ignoreSync: Boolean = false,
    val info: AlbumInfo? = null,
    val metadata: SongMetadata? = null,
    val ordinal: Int
) : AbstractEntity(Song::class) {
    override fun hashCode(): Int {
        return this.id.hashCode()
    }

    override fun equals(other: Any?): Boolean = (this.id == other)

}



@Serializable
data class UserPlaylist(
    val id: String,
    @SerialName("removed-songs")
    val removedSongs: Set<String> = setOf(),
    val songs: Set<String> = setOf(),
)


@Serializable
data class CurrentList(
    val songIds: List<Song>
)
