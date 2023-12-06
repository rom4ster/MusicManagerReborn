import android.content.Context

actual fun getPlatformName(): String = "Android"

@Suppress("StaticFieldLeak")   //FIXME DO SOMETHING ELSE HERE MEMORY LEAKS ARE THE MOST WONDERFULL RIGHT?
private val context: Context by inject()

actual fun getFileDirectory(): String = context.filesDir.path