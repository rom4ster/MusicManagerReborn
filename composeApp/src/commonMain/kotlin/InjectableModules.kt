import kotbase.Database
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json


object InjectableModules {

    private const val DATBASE_NAME = "appDb"

    @OptIn(ExperimentalSerializationApi::class)
    fun module() = org.koin.dsl.module {
        single<Database> {
            Database(DATBASE_NAME)
        }

        single<com.rom4ster.musicmanagerreborn.database.Database> {
            com.rom4ster.musicmanagerreborn.database.Database()
        }

        single {
            Json {explicitNulls = false}
        }
    }
}