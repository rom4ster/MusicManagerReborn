package com.rom4ster.musicmanagerreborn.notification

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Context.NOTIFICATION_SERVICE





@SuppressLint("NewApi")
fun createNotificationChannel(context: Context, id: String, name: String, importance: Int) {
    NotificationChannel(id, name, importance).let {
        (context.getSystemService(NOTIFICATION_SERVICE) as NotificationManager)
            .createNotificationChannel(it)
    }
}