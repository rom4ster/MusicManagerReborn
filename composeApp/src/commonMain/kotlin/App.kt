import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.text.toLowerCase
import cafe.adriel.voyager.navigator.CurrentScreen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.rom4ster.musicmanagerreborn.database.SongEntry
import com.rom4ster.musicmanagerreborn.ui.screens.Home
import com.rom4ster.musicmanagerreborn.ui.screens.Playlist
import com.rom4ster.musicmanagerreborn.ui.screens.Playlists
import com.rom4ster.musicmanagerreborn.ui.state.HomeState
import com.rom4ster.musicmanagerreborn.ui.state.PlaylistState
import com.rom4ster.musicmanagerreborn.ui.state.SongState
import com.rom4ster.musicmanagerreborn.utils.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import org.jetbrains.compose.resources.ExperimentalResourceApi
import java.util.*


val renderHack = MutableStateFlow(0)

@OptIn(ExperimentalResourceApi::class)
@Composable

fun App() {

    MaterialTheme {
//        var greetingText by remember { mutableStateOf("Hello World!") }
//        var showImage by remember { mutableStateOf(false) }
//        Column(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
//            Button(onClick = {
//                greetingText = "Compose: ${Greeting().greet()} "
//                showImage = !showImage
//            }) {
//                Text(greetingText)
//            }
//            AnimatedVisibility(showImage) {
//                Image(
//                    painterResource("compose-multiplatform.xml"),
//                    null
//                )
//            }
//        }
//    }
    val t = MutableStateFlow(HomeState("NO SEE ME PLZ"))
    val p = MutableStateFlow(listOf(
        PlaylistState(
            "fake",
            "fakeName",
            setOf(),
            mapOf(
               SongEntry(
                   "fakeid",
                   1,
               ) to SongState(
                   "fakeid",
                   "fakeSong",
                   "boo",
                   "lolo"
               )
            )
        )
    ).let {
        val l = mutableListOf(it.first())
        (0 until 20).map {_ ->
            l += it.first()
        }
        l.toList()
    })

//    Home(
//        t
//    ).Content()


    lateinit var appNav: Navigator

    Playlists(
        p
    ).apply { this.initialized = true }.let { it ->

        //if (backStack.isEmpty()) { backStack.add(it) }
        val platform = getPlatform().name


        if (platform.lowercase(Locale.getDefault()).contains("java") || platform == "Desktop") {
            Navigator(it) { navigator ->
                DisposableEffect("h") {
                    val observer = OnBackPress {
                        val curr = navigator.lastItem
                        navigator.popUntil { s -> s.key != curr.key }
                    }
                    BackPressureHandler.register(observer)

                    onDispose {
                        BackPressureHandler.unregister(observer)
                    }
                }
                CurrentScreen()

            }
        } else {
           Navigator(
               it,
               onBackPressed = {
                   val curr = appNav.lastItem
                   appNav.popUntil { s -> s.key != curr.key }
               }
               ) {
               nav ->
               appNav = nav
               CurrentScreen()
           }
        }
    }
}


//    when (st) {
//        0 -> {
//            while(backStack.size > 0) {
//                backStack.pop()
//            }
//            while(forwardStack.size > 0) {
//                forwardStack.pop()
//            }
//
//        }
//        1 -> previousScreen()
//        2 -> nextScreen()
//        else -> {
//
//
//
//        }
//    }



}