package com.rom4ster.musicmanagerreborn.database

import com.rom4ster.musicmanagerreborn.error.UnregisteredSerializationException
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import kotlinx.serialization.serializer
import java.lang.reflect.Type
import kotlin.reflect.KClass
import kotlin.reflect.KProperty
import kotlin.reflect.jvm.internal.impl.descriptors.Visibilities.Private


@JvmInline
value class AbstractEntityValidationErrors(
    val errors: Map<KProperty<*>, String>
)



@Serializable
abstract class AbstractEntity {

    constructor(clazz: KClass<out AbstractEntity>?)  {
        this.clazz = clazz
                requireNotNull(this.clazz) {
            "MUST SPECIFY CLASS WHEN CONSTRUCTING THIS OBJECT CRY ABOUT IT"
        }
    }


    @Transient private var clazz: KClass<out AbstractEntity>? = null
//    init {
//        requireNotNull(this.clazz) {
//            "MUST SPECIFY CLASS WHEN CONSTRUCTING THIS OBJECT CRY ABOUT IT"
//        }
//    }



    @Transient
    val validate: (KProperty<*>) -> AbstractEntityValidationErrors = { AbstractEntityValidationErrors(mapOf()) }


    val serializer get() =  Serializers.serializers[clazz?.qualifiedName] ?: throw UnregisteredSerializationException()



}

