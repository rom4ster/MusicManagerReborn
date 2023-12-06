package com.rom4ster.musicmanagerreborn.database

import kotlin.reflect.KClass
import kotlin.reflect.KProperty1
import kotlin.reflect.full.memberProperties

object ClassMaps {

    // Can add non serializable classes here if need be
    private val classes =
        Serializers.serializersClassMap.keys

    @Suppress("UNCHECKED_CAST")
    private val propertyMap: Map<KClass<out Any>, Collection<KProperty1<Any, Any?>>> =
        classes.associateWith { clazz ->
            clazz.memberProperties.map { it as KProperty1<Any, Any?> }
        }

    fun propertiesOf(clazz: KClass<out Any>) : Collection<KProperty1<Any, Any?>> = propertyMap[clazz] ?: throw IllegalArgumentException("${clazz.qualifiedName} not found")

}