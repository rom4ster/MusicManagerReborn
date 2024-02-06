package com.rom4ster.musicmanagerreborn.utils

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.annotation.InternalVoyagerApi
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.currentOrThrow
import renderHack
import kotlin.NoSuchElementException


val backStack = ArrayDeque<Screen>()
val forwardStack = ArrayDeque<Screen>()

fun <T> ArrayDeque<T>.push(item: T) = this.addLast(item)
fun <T> ArrayDeque<T>.pop(): T = this.removeLast()

fun <T> ArrayDeque<T>.peek(): T = this.last()


object BackPressureHandler {
    private val callbacks = mutableSetOf<OnBackPress>()
    fun register(onBackPress: OnBackPress) { callbacks += onBackPress }
    fun unregister(onBackPress: OnBackPress) { callbacks.remove(onBackPress) }

    fun execute() { for(callback in callbacks) callback.onBackPressed() }
}

fun interface OnBackPress {
    fun onBackPressed()
}


@Composable
infix fun Navigator.add(screens: List<Screen>) {
//    LocalNavigator.currentOrThrow.items.first().let {
//        backStack.push(it)
//    }
//    screens.forEach {
//        backStack.push(it)
//    }
//    if (!backStack.isEmpty()) {
//        backStack.pop()
//    }
    this.push(screens)
}

@OptIn(InternalVoyagerApi::class)
@Composable
fun previousScreen() {

    LocalNavigator.currentOrThrow.let {
        it.lastItemOrNull?.let {s ->
            it.popUntil { scr -> scr == s }
        }
    }

}

@Composable
fun nextScreen() {
    try {
        forwardStack.peek()
    } catch (e: NoSuchElementException) {
        renderHack.compareAndSet(renderHack.value, 0)
        return
    }
    val next = forwardStack.pop()
    backStack.push(next)
    Navigator(next)
}