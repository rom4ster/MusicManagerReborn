package com.rom4ster.musicmanagerreborn.utils

import androidx.compose.ui.unit.dp
import inject


val contextProvider: AppContextProvider by inject()
actual fun getScreenDim(): DpDimensions = contextProvider.context.resources.displayMetrics.let {
    DpDimensions(
        (it.heightPixels / it.density).dp,
        (it.widthPixels / it.density).dp
    )
}