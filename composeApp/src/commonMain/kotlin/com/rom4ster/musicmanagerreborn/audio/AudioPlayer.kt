package com.rom4ster.musicmanagerreborn.audio

interface AudioPlayer {

    fun load(filePath: String)
    fun play()
    fun stop()
    fun pause()
}