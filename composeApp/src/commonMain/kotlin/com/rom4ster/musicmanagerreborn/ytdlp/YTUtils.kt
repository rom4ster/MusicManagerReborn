package com.rom4ster.musicmanagerreborn.ytdlp

import com.rom4ster.musicmanagerreborn.database.Song
import getFileDirectory


const val YT_VIDEO_LINK_BASE = "https://www.youtube.com/watch?v="
const val YT_GET_ID = "--get-id"
const val YT_FLAT_PLAYLIST = "--flat-playlist"
const val YT_NO_WARNINGS = "--no-warnings"

// -f bestaudio --extract-audio --audio-format mp3 --audio-quality 0
const val YT_FORMAT = "--format"
const val YT_BESTAUDIO = "bestaudio"
const val YT_EXTRACT_AUDIO = "--extract-audio"
const val YT_AUDIO_FORMAT = "--audio-format"
const val YT_MP3 = "mp3"
const val YT_AUDIO_QUALITY = "--audio-quality"

const val YT_OUTPUT = "--output"
const val YT_PATH = "--paths"
const val FFMPEG_LOCATION = "--ffmpeg-location"

val YT_FILE_DIR = "${getFileDirectory()}/songs"
val YT_DELETED_DIR = "$YT_FILE_DIR/deleted"


// "$YT_FILE_DIR/${this.title.asTitle()}${this.id.asId()}"

const val YT_OUTPUT_FORMAT = """%(id)s"""

fun Song.makeFileName() = this.id

infix fun Song.asFilePathWithExtension(extension: String): String =
    extension
        .replace("""^\.*""".toRegex(), "")
        .let { "${this.makeFileName()}.$it" }

enum class YTAudioNum(val num: Int) {
    BEST(0)
}

enum class LinkType {
    PLAYLIST,
    VIDEO
}


fun validateLink(link: String, type: LinkType) =
    when (type) {
        LinkType.PLAYLIST -> {
            link.takeIf { it.matches(Regex(""".*/playlist\?list=.*""")) } ?: throw IllegalArgumentException()
        }

        LinkType.VIDEO -> {
            link.takeIf { it.matches(Regex(""".*/watch\?v=.*""")) } ?: throw IllegalArgumentException()
        }
    }




