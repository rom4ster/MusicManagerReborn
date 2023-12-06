import com.rom4ster.musicmanagerreborn.ytdlp.YTDLP
import com.rom4ster.musicmanagerreborn.ytdlp.YTWrapper
import org.koin.dsl.bind


object PlatformModules {


    fun module() = org.koin.dsl.module {
        single {
            YTWrapper("./ffmpeg") //FIXME make this bound via PERSISTED config file
        } bind YTDLP::class
    }
}