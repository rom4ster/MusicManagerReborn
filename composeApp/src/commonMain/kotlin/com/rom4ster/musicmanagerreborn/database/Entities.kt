package com.rom4ster.musicmanagerreborn.database

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlin.reflect.KProperty1


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
    val info: AlbumInfo? = null,
    val metadata: SongMetadata? = null,
) : AbstractEntity(Song::class) {




    override fun hashCode(): Int {
        return this.id.hashCode()
    }

    override fun equals(other: Any?): Boolean = if (other is Song) { songEquals(other) } else {(this.id == other)}

    override fun idProp(): KProperty1<out AbstractEntity, String>  = Song::id


    private fun songEquals(other: Song) : Boolean = (this.id == other.id)

}



@Serializable
data class SongEntry(
    val id: String,
    val ordinal: Int,
    val ignoreSync: Boolean = false,
)


@Serializable
data class UserPlaylist(
    val id: String,
    val name: String,
    val links: Set<String>,
    @SerialName("removed-songs")
    val removedSongs: Set<SongEntry> = setOf(),
    val songs: Set<SongEntry> = setOf(),
) : AbstractEntity(UserPlaylist::class) {
    override fun idProp(): KProperty1<out AbstractEntity, String>  = UserPlaylist::id
}


@Serializable
data class CurrentList(
    val songIds: List<Song>
)
