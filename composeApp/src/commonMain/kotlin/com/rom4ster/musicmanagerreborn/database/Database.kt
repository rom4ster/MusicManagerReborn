package com.rom4ster.musicmanagerreborn.database


import com.rom4ster.musicmanagerreborn.error.DatabaseErrors
import inject
import kotbase.*
import kotbase.Database
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.serialization.serializer
import javax.swing.text.html.parser.Entity
import kotlin.reflect.KMutableProperty
import kotlin.reflect.KProperty




enum class OPERATOR(val operatorFunction: (exp: Expression, other: Expression) -> Expression) {
    EQUAL({exp, other -> exp.equalTo(other) }),
    GREATER_THAN({exp, other -> exp.greaterThan(other) }),
    LESS_THAN({exp, other -> exp.lessThan(other) }),
    AND({exp, other -> exp.and(other) }),
    OR({exp, other -> exp.or(other) }),
    NOTEQUAL({exp, other -> exp.notEqualTo(other) }),
    NOT({exp, other -> exp.isNot(other) }),
}




 fun <T : AbstractEntity> KProperty<T>.asExpression() = Expression.property(this.name)

 fun <T> T.asValueExpression() = Expression.value(this)

@Suppress("UNCHECKED_CAST") // Supression necessary to counter type erasure without inline funcs
 private fun <T : AbstractEntity>  KSerializer<out Any>.asTypedSerializer() = this as KSerializer<T>



class Database {



    private val database: Database by inject()
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
        database.save(document)
    }


    fun <T: AbstractEntity> query(selection: List<KProperty<T>>, queryObject: QueryObject) {
        QueryBuilder
            .select(
                *selection.map { SelectResult.property(it.name) }.toTypedArray()
            )
            .from(
                DataSource.database(database)
            )
            .where(
                queryObject.resolver()
            )
    }

    fun <T: AbstractEntity> remove(entity: T) {
        val document = MutableDocument()
        document.setJSON(json.encodeToString(entity.serializer))
        database.delete(document)
    }

    fun <T : AbstractEntity> update(oldEntity: T, entity: T, validationProps: List<KProperty<*>> = listOf()) {

        val document = MutableDocument()
        document.setJSON(json.encodeToString(entity.serializer))
        validationProps.map {
            entity.validate(it)
        }.let {
            require(it.isEmpty()) {
                DatabaseErrors.operationFailure("update")
            }
        }
        val oldDocument = MutableDocument()
        oldDocument.setJSON(json.encodeToString(oldEntity.serializer))

        // TRANSACTION
        database.delete(oldDocument)
        database.save(document)
    }





}