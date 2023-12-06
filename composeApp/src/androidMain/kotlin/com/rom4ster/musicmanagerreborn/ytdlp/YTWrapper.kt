package com.rom4ster.musicmanagerreborn.ytdlp

import android.content.Context
import android.util.Log
import com.yausername.ffmpeg.FFmpeg
import com.yausername.youtubedl_android.YoutubeDL
import com.yausername.youtubedl_android.YoutubeDLException
import com.yausername.youtubedl_android.YoutubeDLRequest
import com.yausername.youtubedl_android.mapper.VideoInfo

class YTWrapper(context: Context) : YTDLP {




    init {
        try {
            YoutubeDL.getInstance().init(context)
            FFmpeg.getInstance().init(context)
        } catch (e: YoutubeDLException) {
            throw NoSuchElementException("Could Not Create YTWrapper class")
        }
    }


    private fun VideoInfo.toVideoInformation() = VideoInformation(
        this.id ?: "",
        this.title ?: "",
        this.uploader ?: ""
    )
    override fun getInfo(link: String)  = YoutubeDL.getInstance().getInfo(link).toVideoInformation()
    override fun getBatchInfo(link: String)  = YoutubeDLRequest(link)
        .addOption(YT_GET_ID)
        .addOption(YT_FLAT_PLAYLIST)
        .addOption(YT_NO_WARNINGS).let {
            YoutubeDL
                .getInstance()
                .execute(it)
                .out
                .let {
                    o -> Log.i("ROMTAG", o)
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
            .addOption(YT_OUTPUT, YT_OUTPUT_FORMAT)
            .let {
                YoutubeDL.getInstance().execute(it)
                Unit
            }



}