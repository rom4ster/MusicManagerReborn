import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.runtime.Composable
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.key
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.rom4ster.musicmanagerreborn.utils.BackPressureHandler
import com.rom4ster.musicmanagerreborn.utils.MouseListener
import org.jnativehook.GlobalScreen
import org.jnativehook.GlobalScreen.addNativeMouseListener
import org.jnativehook.NativeHookException
import org.koin.core.context.startKoin
import java.util.logging.Level
import java.util.logging.Logger
import kotlin.system.exitProcess




lateinit var mouseListener: MouseListener
fun main() = application {
    configureLogging()
    registerMouseInput()
    startKoin {
        modules(InjectableModules.module())
        modules(PlatformModules.module())
    }
    Window(onCloseRequest = ::exitApplication,         onKeyEvent = {
        if (it.key == Key.Escape){
                            BackPressureHandler.execute()
        }
        false
    }) {
        App()
    }
}

fun configureLogging() {
    Logger.getLogger(GlobalScreen::class.java.getPackage().name).apply {
        level = Level.WARNING
        useParentHandlers = false
    }
}

fun registerMouseInput() {
    try {
        GlobalScreen.registerNativeHook()
    } catch (ex: NativeHookException) {
        System.err.println("There was a problem registering the native hook.")
        System.err.println(ex.message)
        exitProcess(1) // Should this be considered a TERMINAL event, currently the reasoning is, that this is core functionality lol
    }
    mouseListener = MouseListener()

    addNativeMouseListener(mouseListener)

}

@Preview
@Composable
fun AppDesktopPreview() {
    App()
}


