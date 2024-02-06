import com.rom4ster.musicmanagerreborn.audio.AudioPlayer
import com.rom4ster.musicmanagerreborn.audio.Player
import com.rom4ster.musicmanagerreborn.utils.AppContextProvider
import com.rom4ster.musicmanagerreborn.ytdlp.YTDLP
import com.rom4ster.musicmanagerreborn.ytdlp.YTWrapper
import kotlinx.serialization.ExperimentalSerializationApi
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.bind

object PlatformModules {


    fun module() = org.koin.dsl.module {
        single {
            YTWrapper(androidContext())
        } bind YTDLP::class

        factory {
            AppContextProvider(androidContext())
        }

        factory {
            Player(androidContext())
        } bind AudioPlayer::class



    }
}