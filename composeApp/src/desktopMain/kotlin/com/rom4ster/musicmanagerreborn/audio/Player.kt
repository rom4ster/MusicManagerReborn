package com.rom4ster.musicmanagerreborn.audio

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import launch
import java.io.File

class Player : AudioPlayer {

    val audioLibrary: AudioLibrary = AudioLibrary()
    override fun load(filePath: String) {
        audioLibrary.open(File(filePath))
    }

    override fun play() {
        if (audioLibrary.isOpened || audioLibrary.isPaused) {
            launch(Dispatchers.IO) {
                audioLibrary.play()


            }
        }
        if (audioLibrary.isPaused) {
            audioLibrary.resume()
        }
    }

    override fun stop() {
        if (audioLibrary.isPausedOrPlaying) {
            audioLibrary.stop()
        }
    }

    override fun pause() {
        if (audioLibrary.isPlaying) {
            audioLibrary.pause()
        }
    }


}