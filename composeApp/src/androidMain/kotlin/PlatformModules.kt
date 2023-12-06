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
    }
}