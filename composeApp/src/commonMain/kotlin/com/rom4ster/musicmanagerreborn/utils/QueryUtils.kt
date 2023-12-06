package com.rom4ster.musicmanagerreborn.utils

import com.rom4ster.musicmanagerreborn.database.AbstractEntity
import com.rom4ster.musicmanagerreborn.database.QueryObject
import kotbase.Expression
import kotbase.QueryBuilder
import kotbase.SelectResult
import kotlin.reflect.KProperty1



enum class OPERATOR(val operatorFunction: (exp: Expression, other: Expression) -> Expression) {
    EQUAL({exp, other -> exp.equalTo(other) }),
    GREATER_THAN({exp, other -> exp.greaterThan(other) }),
    LESS_THAN({exp, other -> exp.lessThan(other) }),
    AND({exp, other -> exp.and(other) }),
    OR({exp, other -> exp.or(other) }),
    NOTEQUAL({exp, other -> exp.notEqualTo(other) }),
    NOT({exp, other -> exp.isNot(other) }),
}

enum class EXPRESSIONCONSTANTS(val exp: Expression) {
    TRUE(true.asValueExpression()),
    FALSE(true.asValueExpression()),
}



fun <T : Any> KProperty1<T, Any?>.asExpression() = Expression.property(this.name)

fun <T> T.asValueExpression() = try {
    Expression.value(this)
} catch ( e: IllegalArgumentException) {
    Expression.value(this.toString())
}









