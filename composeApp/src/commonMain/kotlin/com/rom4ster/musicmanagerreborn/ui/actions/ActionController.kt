package com.rom4ster.musicmanagerreborn.ui.actions

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.navigator.Navigator


/**
 * Interface for controlling actions that a UI can take
 * Meant to be implemented by Screens
 */
interface ActionController<T> {





    @Composable
    fun <Q> doAction(input: T, actionCode: Int, actionData: Q?)

    @Composable  fun <Q,  E : Enum<E>> doAction(input: T, actionCode: E, actionData: Q?) = doAction(input, actionCode.ordinal, actionData)
    @Composable  fun <E : Enum<E>> doAction(input: T, actionCode: E) = doAction(input, actionCode.ordinal, actionData = null)

    @Composable fun doAction(input: T, actionCode: Int) = doAction(input, actionCode, actionData = null)





}







