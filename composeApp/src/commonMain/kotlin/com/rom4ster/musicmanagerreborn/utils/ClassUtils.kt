package com.rom4ster.musicmanagerreborn.utils

import kotlin.reflect.KClass
import kotlin.reflect.full.memberProperties
import kotlin.reflect.full.primaryConstructor


inline fun <reified T : Any>  KClass<T>.names() =  this
    .primaryConstructor
    ?.parameters
    ?.mapIndexed { index, property ->
        index to property
    }
    ?.sortedBy {
        it.first
    }
    ?.map {
        it.second.name
    }
    ?.filterNotNull()
    ?: throw IllegalArgumentException("Unable to get class parameters for ${this.qualifiedName}")


inline fun <reified T : Any> KClass<T>.types() = this
    .memberProperties.associate { prop ->
        prop.name to prop.returnType
    }


inline infix fun <reified T : Any>  KClass<T>.namesFiltering(blacklist: List<String>) =
    this.names().filterNot {
        blacklist.contains(it)
    }

infix fun List<String>.filtering(blacklist: List<String>) = this.filterNot {
    blacklist.contains(it)
}
infix fun List<String>.including(whiteList: List<String>) = this.filter {
    whiteList.contains(it)
}

inline infix fun <reified K : Any, reified V: Any> Map<K,V?>.get(key: K) = this[key] ?:  throw IllegalArgumentException( "Key $key not found in Map" )