import com.couchbase.lite.Database

object InjectableModules {

    private const val DATBASE_NAME = "appDb"

    fun module() = org.koin.dsl.module {
        single<Database> {
            Database(DATBASE_NAME)
        }

        single<com.rom4ster.musicmanagerreborn.database.Database> {
            com.rom4ster.musicmanagerreborn.database.Database()
        }
    }
}