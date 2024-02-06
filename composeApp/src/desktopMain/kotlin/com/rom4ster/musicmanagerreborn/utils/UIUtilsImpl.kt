package com.rom4ster.musicmanagerreborn.utils

import androidx.compose.ui.window.WindowState


val windowState: WindowState = WindowState()
actual fun getScreenDim(): DpDimensions = with(windowState.size) { DpDimensions(this.width, this.height ) }
