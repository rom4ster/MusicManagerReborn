package com.rom4ster.musicmanagerreborn.database

import kotbase.Expression
import kotlin.reflect.KProperty

interface QueryObject {
    fun resolver(): Expression
}
data class PropertyEquality<T : AbstractEntity, V>(
    val property: KProperty<T>,
    val value: V
) : QueryObject {
    override fun resolver(): Expression =
        OPERATOR.EQUAL.operatorFunction(this.property.asExpression(), this.value.asValueExpression())

}
