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
data class Song(
    val id: String,
    val name: String,
    val filePath: String?,
    val uploader: String?,
    val ignoreSync: Boolean = false,
    val info: AlbumInfo? = null,
    val metadata: SongMetadata? = null
)

@Serializable
data class UserPlaylist(
    val id: String,
    @SerialName("removed-songs")
    val removedSongs: List<String> = listOf(),
    val songs: List<String> = listOf(),
)