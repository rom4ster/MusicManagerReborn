package com.rom4ster.musicmanagerreborn.ytdlp


data class VideoInformation(
    val id: String,
    val title: String,
    val uploader: String,
)

interface YTDLP {
    fun getInfo(link: String): VideoInformation
    fun getBatchInfo(link: String): Collection<VideoInformation>

    fun download(link: String)
    fun downloadPlaylist(link: String)
}



