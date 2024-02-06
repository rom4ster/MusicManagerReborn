package com.rom4ster.musicmanagerreborn.audio

import android.app.NotificationManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.os.IBinder
import com.rom4ster.musicmanagerreborn.NOTIFICATION_NAME
import com.rom4ster.musicmanagerreborn.NOTIF_ID
import com.rom4ster.musicmanagerreborn.notification.createNotificationChannel

class Player(private val context: Context) : AudioPlayer {



    lateinit var audioService: AudioService
    var prepared = false

    init {
        val serviceConnection: ServiceConnection = object : ServiceConnection {
            override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
                audioService = (service as AudioService.AudioBinder).service
            }

            override fun onServiceDisconnected(name: ComponentName?) {

            }




        }

        context.bindService(
            Intent(context, AudioService::class.java),
            serviceConnection,
            Context.BIND_AUTO_CREATE
        ).takeIf {
            it
        } ?: NoSuchMethodError("Service failed to Initialize" )








    }
    override fun load(filePath: String) {
        if (audioService.mediaPlayer.isPlaying) {
            audioService.mediaPlayer.stop()
        }
        audioService.mediaPlayer.setDataSource(filePath)
        prepared = true
    }

    override fun play() {
        if (prepared && !audioService.mediaPlayer.isPlaying) {
            audioService.mediaPlayer.start()
        }
    }

    override fun stop() {
        if (prepared) {
            audioService.mediaPlayer.stop()
        }
    }

    override fun pause() {
        if (prepared && audioService.mediaPlayer.isPlaying) {
            audioService.mediaPlayer.pause()
        }
    }

    val mediaPlayer = MediaPlayer().apply {
        setAudioAttributes(
            AudioAttributes.Builder()
                .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                .setUsage(AudioAttributes.USAGE_MEDIA)
                .build()
        )
    }
}