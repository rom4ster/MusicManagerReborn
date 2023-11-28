package com.rom4ster.musicmanagerreborn.database

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.builtins.PairSerializer
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildSerialDescriptor
import kotlinx.serialization.descriptors.listSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.serializer
import kotlin.reflect.KClass
import kotlin.reflect.full.declaredMembers
import kotlin.reflect.full.memberProperties


object Serializers {
    @OptIn(InternalSerializationApi::class)
    val serializersClassMap = mapOf(
        Song::class to Song::class.serializer(),
        SongMetadata::class to SongMetadata::class.serializer(),
        AlbumInfo::class to AlbumInfo::class.serializer(),
        UserPlaylist::class to UserPlaylist::class.serializer(),
        CurrentList::class to CurrentList::class.serializer(),
    )
    val serializers = serializersClassMap.mapKeys {
        it.key.qualifiedName
    }









}









