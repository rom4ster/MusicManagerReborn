package com.rom4ster.musicmanagerreborn.ytdlp

import com.rom4ster.musicmanagerreborn.logging.Log
import com.sapher.youtubedl.YoutubeDL
import com.sapher.youtubedl.YoutubeDLRequest
import com.sapher.youtubedl.mapper.VideoInfo

class YTWrapper(val ffmpegPath: String, youtubeDlPath: String = "./youtube-dl") : YTDLP {
    init {
        YoutubeDL.setExecutablePath(youtubeDlPath)
    }

    private fun YoutubeDLRequest.addOption(key: String, value: String? = null) = this.addHelper(key, value)
    private fun YoutubeDLRequest.addOption(key: String, value: Int) = this.addHelper(key, "$value")

    private fun YoutubeDLRequest.addHelper(key: String, value: String?) =
        this.setOption(key.replace("""^--""".toRegex(), ""), value).let { this }

    private fun VideoInfo.toVideoInformation() = VideoInformation(
        this.id ?: "",
        this.title ?: "",
        this.uploader ?: ""
    )
    override fun getInfo(link: String)  = YoutubeDL.getVideoInfo(link).toVideoInformation()

    override fun getBatchInfo(link: String)  = YoutubeDLRequest(link)
        .addOption(YT_GET_ID)
        .addOption(YT_FLAT_PLAYLIST)
        .addOption(YT_NO_WARNINGS).let {
            YoutubeDL

                .execute(it)
                .out
                .let {
                        o -> Log.i(o)
                    o.split("\n")
                }.map {output ->
                    getInfo("${YT_VIDEO_LINK_BASE}$output")
                }
        }

    override fun download(link: String) = LinkType.VIDEO buildDownloadFor link
    override fun downloadPlaylist(link: String) = LinkType.PLAYLIST buildDownloadFor link
    private infix fun LinkType.buildDownloadFor(link: String) =
        YoutubeDLRequest(link)
            .addOption(YT_FORMAT, YT_BESTAUDIO)
            .addOption(YT_EXTRACT_AUDIO)
            .addOption(YT_AUDIO_FORMAT, YT_MP3)
            .addOption(YT_AUDIO_QUALITY, YTAudioNum.BEST.num)
            .addOption(YT_PATH, YT_FILE_DIR)
            .addOption(FFMPEG_LOCATION, ffmpegPath)
            .addOption(YT_OUTPUT, YT_OUTPUT_FORMAT)
            .let {
                YoutubeDL.execute(it)
                Unit
            }

}