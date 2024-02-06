package com.rom4ster.musicmanagerreborn

import App
import InjectableModules
import PlatformModules
import android.app.NotificationManager
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.rom4ster.musicmanagerreborn.audio.AudioService
import com.rom4ster.musicmanagerreborn.notification.createNotificationChannel
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin


val NOTIFICATION_NAME = "player"
val NOTIF_ID = "10001"
class MainActivity : ComponentActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        createNotificationChannel(applicationContext, NOTIF_ID, NOTIFICATION_NAME, NotificationManager.IMPORTANCE_MAX)
        startService(
            Intent(applicationContext, AudioService::class.java)
        )
        startKoin {
            androidContext(this@MainActivity)
            modules(InjectableModules.module())
            modules(PlatformModules.module())
        }

        setContent {
            App()
        }
    }
}

@Preview
@Composable
fun AppAndroidPreview() {
    App()
}