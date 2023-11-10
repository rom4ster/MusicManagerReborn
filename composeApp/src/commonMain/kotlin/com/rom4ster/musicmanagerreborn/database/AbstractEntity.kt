package com.rom4ster.musicmanagerreborn.database

import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import kotlinx.serialization.serializer
import java.lang.reflect.Type
import kotlin.reflect.KClass
import kotlin.reflect.KProperty



@JvmInline
value class AbstractEntityValidationErrors(
    val errors: Map<KProperty<*>, String>
)



@Serializable
abstract class AbstractEntity(private val clazz: KClass<out AbstractEntity>) {
    @Transient
    val validate: (KProperty<*>) -> AbstractEntityValidationErrors = { AbstractEntityValidationErrors(mapOf()) }

    @Transient
    val serializer = Serializers.serializers[clazz]



}

