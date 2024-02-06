package com.rom4ster.musicmanagerreborn.utils

import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.currentOrThrow
import org.jnativehook.mouse.NativeMouseEvent
import org.jnativehook.mouse.NativeMouseInputListener
import renderHack

class MouseListener : NativeMouseInputListener {



    enum class BUTTONS {
        LCLICK,
        RCLICK,
        MCLICK,
        BACK,
        FORWARD
    }
    override fun nativeMouseClicked(p0: NativeMouseEvent?) {

    }

    override fun nativeMousePressed(p0: NativeMouseEvent) {
        val button = p0.button-1
        println(BUTTONS.entries.find { it.ordinal == button }!!.name)
        if (button == BUTTONS.BACK.ordinal) {
            BackPressureHandler.execute()
        }
        if  (button == BUTTONS.RCLICK.ordinal ) {
            rclick.compareAndSet(
                rclick.value,
                p0.x to p0.y
                )
        }
    }

    override fun nativeMouseReleased(p0: NativeMouseEvent?) {

    }

    override fun nativeMouseMoved(p0: NativeMouseEvent?) {

    }

    override fun nativeMouseDragged(p0: NativeMouseEvent?) {

    }
}