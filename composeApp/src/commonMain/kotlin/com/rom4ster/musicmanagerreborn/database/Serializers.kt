package com.rom4ster.musicmanagerreborn.database

import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.serializer
import kotlin.reflect.KClass


object Serializers {
    @OptIn(InternalSerializationApi::class)
    val serializers = mapOf(
        Song::class to Song::class.serializer(),
        SongMetadata::class to SongMetadata::class.serializer(),
        AlbumInfo::class to AlbumInfo::class.serializer(),
        UserPlaylist::class to UserPlaylist::class.serializer(),
        CurrentList::class to CurrentList::class.serializer(),
    ).mapKeys {
        it.key.qualifiedName
    }
}




