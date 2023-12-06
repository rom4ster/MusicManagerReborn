package com.rom4ster.musicmanagerreborn.database

import com.rom4ster.musicmanagerreborn.utils.EXPRESSIONCONSTANTS
import com.rom4ster.musicmanagerreborn.utils.OPERATOR
import com.rom4ster.musicmanagerreborn.utils.asExpression
import com.rom4ster.musicmanagerreborn.utils.asValueExpression
import kotbase.Expression
import kotlin.reflect.KProperty
import kotlin.reflect.KProperty1

interface QueryObject {
    fun resolver(): Expression
}
data class PropertyEquality<T : Any, V>(
    val property: KProperty1<T, V>,
    val value: V
) : QueryObject {
    override fun resolver(): Expression =
        OPERATOR.EQUAL.operatorFunction(this.property.asExpression(), this.value.asValueExpression())

}

data class ConstantObject(
    val constant: EXPRESSIONCONSTANTS
) : QueryObject {
    override fun resolver(): Expression = constant.exp
}
