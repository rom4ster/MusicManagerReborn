package com.rom4ster.musicmanagerreborn.audio

import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.media.MediaPlayer
import android.os.Binder
import android.os.IBinder
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Star
import androidx.core.app.NotificationCompat
import com.rom4ster.musicmanagerreborn.MainActivity
import com.rom4ster.musicmanagerreborn.NOTIF_ID
import inject

class AudioService : Service() {


    val controlPlayer: Player by inject()


    interface AudioBinder {
        val service: AudioService
    }

    lateinit var mediaPlayer: MediaPlayer


    private val binder = object : Binder(), AudioBinder {
        override val service: AudioService = this@AudioService
    }




    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        val notificationIntent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, PendingIntent.FLAG_IMMUTABLE)
        return  NotificationCompat.Builder(this, NOTIF_ID)
            //TODO SET THIS GARBAGE TO SOMETHING MORE LEGIT AND LINK MEDIA CONTROLS
            .setContentTitle("Media Player")
            .setContentText("Test")
            .setContentIntent(pendingIntent)
            .build()
            .apply {
                startForeground(101, this)
            }.let {
                mediaPlayer.start()
                START_NOT_STICKY
            }
    }
    override fun onBind(intent: Intent?): IBinder = binder
    override fun onCreate() {
        super.onCreate()
        mediaPlayer = controlPlayer.mediaPlayer
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer.stop()
        mediaPlayer.release()
    }
}