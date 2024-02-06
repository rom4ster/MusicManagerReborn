package com.rom4ster.musicmanagerreborn.utils

import androidx.compose.ui.unit.Dp
import com.rom4ster.musicmanagerreborn.database.ClassMaps
import kotlinx.coroutines.flow.MutableStateFlow
import java.lang.IllegalArgumentException

inline fun <reified T : Any>  allProps() = ClassMaps.propertiesOf(T::class).toTypedArray()

inline fun <reified T> T.asMutableFlow() = MutableStateFlow(this)

inline fun <T, reified Q> T?.verifiedCast() : Q = this.let {
    requireNotNull(it)
    require( it is Q)
    it
}
infix fun String.pluralizeWithCount(count: Int) = "${this}s".takeIf { count != 1 } ?: this


inline fun <reified T: Enum<T>> Int.asEnumValue() = requireNotNull(enumValues<T>().find { it.ordinal == this }) { "Invalid Enum Value" }

data class DpDimensions(val width: Dp, val height: Dp)

expect fun getScreenDim(): DpDimensions

enum class InputTypes {
    TEXT_FIELD,
    TEXT_FIELD_MULTILINE,

}
