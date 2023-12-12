package com.rom4ster.musicmanagerreborn.audio
import com.goxr3plus.streamplayer.stream.StreamPlayer
import com.goxr3plus.streamplayer.stream.StreamPlayerEvent
import com.goxr3plus.streamplayer.stream.StreamPlayerListener
class AudioLibrary : StreamPlayer(), StreamPlayerListener {
    override fun opened(dataSource: Any?, properties: MutableMap<String, Any>?) {

    }

    override fun progress(
        nEncodedBytes: Int,
        microsecondPosition: Long,
        pcmData: ByteArray?,
        properties: MutableMap<String, Any>?
    ) {

    }

    override fun statusUpdated(event: StreamPlayerEvent?) {

    }
}