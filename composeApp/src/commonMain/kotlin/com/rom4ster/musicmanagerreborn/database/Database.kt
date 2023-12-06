package com.rom4ster.musicmanagerreborn.database


import com.benasher44.uuid.UUID
import com.benasher44.uuid.Uuid
import com.rom4ster.musicmanagerreborn.error.DatabaseErrors
import com.rom4ster.musicmanagerreborn.error.DuplicateKeyException
import com.rom4ster.musicmanagerreborn.error.KeyNotFoundException
import com.rom4ster.musicmanagerreborn.utils.EXPRESSIONCONSTANTS
import inject
import kotbase.*
import kotbase.Database
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.serialization.serializer
import java.util.*
import javax.swing.text.html.parser.Entity
import kotlin.reflect.KMutableProperty
import kotlin.reflect.KProperty
import kotlin.reflect.KProperty1




@Suppress("UNCHECKED_CAST") // Supression necessary to counter type erasure without inline funcs
 internal fun <T : AbstractEntity>  KSerializer<out Any>.asTypedSerializer() = this as KSerializer<T>



class Database(
    val database: Database
) {








    private val json: Json by inject()





     fun <T : AbstractEntity> add(entity: T, validationProps: List<KProperty<*>> = listOf()) {

        val document = MutableDocument()
        document.setJSON(json.encodeToString(entity.serializer.asTypedSerializer(), entity))
         validationProps.map {
             entity.validate(it)
         }.let {
             require(it.isEmpty()) {
                 DatabaseErrors.operationFailure("add")
             }
         }

         val idPropValue = document.getString(entity.idProp().name)
         QueryBuilder
             .select(
             SelectResult.property(entity.idProp().name)
             )
             .from(
                 DataSource.database(database)
             )
             .where(
                 PropertyEquality(
                     entity.idProp(),
                     idPropValue
                 ).resolver()
             )
             .execute()
             .allResults().takeIf { it.isEmpty() } ?: throw DuplicateKeyException(
                 "Duplicate key $idPropValue"
             )
        database.save(document)
    }


    fun <Q> query(queryObject: QueryObject, selection: List<KProperty1<out Any, Any?>>, serializer: KSerializer<Q>? = null ): List<QueryDataResult<Q>> {
       return QueryBuilder
            .select(
                *selection.map { SelectResult.property(it.name) }.toTypedArray()
            )
            .from(
                DataSource.database(database)
            )
            .where(
                queryObject.resolver()
            )
            .execute()
            .allResults().map {
                if (serializer != null) {
                    QueryDataResult(
                        it.toJSON(),
                        json.decodeFromString(serializer,it.toJSON())
                    )
                } else {
                    QueryDataResult(
                        it.toJSON()
                    )
                }
            }
    }

    inline fun <reified T : Any> getAll(serializer: KSerializer<out Any>? = null) =
        query(ConstantObject(EXPRESSIONCONSTANTS.TRUE), selections = ClassMaps.propertiesOf(T::class).toTypedArray(), serializer = serializer)

    fun <T : Any> query(queryObject: QueryObject, vararg selections: KProperty1<T, Any?>, serializer: KSerializer<out Any>? = null) =
        query(
            queryObject,  listOf<KProperty1<out Any, Any?>>(*selections), serializer
        )






    fun  remove(id: Uuid) {
        val document = database.getDocument(id.toString()) ?: throw KeyNotFoundException("$id not found")
        database.delete(document)
    }

    fun <T : AbstractEntity> update(id: Uuid, entity: T, validationProps: List<KProperty<*>> = listOf()) {

        val oldDocument = database.getDocument(id.toString()) ?: throw KeyNotFoundException("$id not found")
        val document = oldDocument.toMutable()
        document.setJSON(json.encodeToString(entity.serializer.asTypedSerializer(), entity))
        validationProps.map {
            entity.validate(it)
        }.let {
            require(it.isEmpty()) {
                DatabaseErrors.operationFailure("update")
            }
        }


        database.save(document)
    }



    data class QueryDataResult<Q> (
        val result: String,
        val serializedResult: Q? = null
    )


}