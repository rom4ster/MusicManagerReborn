import kotbase.Database
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import org.koin.core.qualifier.named


object InjectableModules {

    internal const val SONG_DATABASE_NAME = "songDb"
    internal const val USER_PLAYLIST_DATABASE_NAME = "playlistDb"

    @OptIn(ExperimentalSerializationApi::class)
    fun module() = org.koin.dsl.module {

        listOf(
            SONG_DATABASE_NAME,
            USER_PLAYLIST_DATABASE_NAME
        ).forEach { name ->

            single<Database>(named(name)) {
                Database(name)
            }

            single<com.rom4ster.musicmanagerreborn.database.Database>(named(name)) {
                com.rom4ster.musicmanagerreborn.database.Database(get(named(name)))
            }
        }


        single {
            Json {explicitNulls = false}
        }
    }
}