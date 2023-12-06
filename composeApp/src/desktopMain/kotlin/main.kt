import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.runtime.Composable
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import org.koin.core.context.startKoin

fun main() = application {
    startKoin {
        modules(InjectableModules.module())
        modules(PlatformModules.module())
    }
    Window(onCloseRequest = ::exitApplication) {
        App()
    }
}

@Preview
@Composable
fun AppDesktopPreview() {
    App()
}