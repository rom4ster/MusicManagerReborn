package com.rom4ster.musicmanagerreborn.database

import com.rom4ster.musicmanagerreborn.database.Database
import com.rom4ster.musicmanagerreborn.database.data.TEST_SONG
import com.rom4ster.musicmanagerreborn.database.data.TEST_SONG_WITH_INFO
import io.kotest.assertions.fail
import io.kotest.core.spec.style.FreeSpec
import io.kotest.core.test.TestCase
import io.kotest.core.test.TestResult
import io.kotest.matchers.collections.shouldContainAll
import io.kotest.matchers.shouldBe
import kotbase.*
import kotlinx.serialization.*
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromJsonElement
import kotlinx.serialization.json.jsonObject
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.dsl.module
import org.koin.test.KoinTest
import kotlin.test.fail


@OptIn(InternalSerializationApi::class)
class DatabaseTest : FreeSpec(), KoinTest {


    private lateinit var kotbaseDatabase: kotbase.Database
    private lateinit var database: Database

    @OptIn(ExperimentalSerializationApi::class)
    private val json = Json { explicitNulls= false}

    @OptIn(ExperimentalSerializationApi::class)
    override suspend fun beforeEach(testCase: TestCase) {


        stopKoin()

        kotbaseDatabase = kotbase.Database("test-db")
        startKoin {
            modules(
                module {
                    single {

                            Json {explicitNulls = false}

                    }
                    single {
                        kotbaseDatabase
                    }
                }
            )
        }

        QueryBuilder.select(
            SelectResult.expression(Meta.id)
        ).from(
            DataSource.database(kotbaseDatabase)
        ).execute().forEach {
            it.getString(0)?.let { id ->
                kotbaseDatabase.getDocument(id)?.let { it1 -> kotbaseDatabase.delete(it1) }
            }
        }
        database = Database()

        super.beforeEach(testCase)

    }

    override suspend fun afterEach(testCase: TestCase, result: TestResult) {
        kotbaseDatabase.delete()
        super.afterEach(testCase, result)
    }


    private fun pass() = (0 shouldBe 0)
    //                    ---\____/---          <-- makes it look kind of like a face lol

    private fun <T> getSerializedResults(initialKey: String, serializer: KSerializer<T>) = QueryBuilder
        .select(SelectResult.all())
        .from(DataSource.database(kotbaseDatabase))
        .execute()
        .mapNotNull {
            json.parseToJsonElement( it.toJSON()).jsonObject["test-db"]
        }.map {
            json.decodeFromJsonElement(serializer, it)
        }

    init {

        "TestCheck" {
            pass()
            //fail("ssp")
        }

        "Add" - {
            "+" - {
                "Successful Add" {
                    //Given

                    val testSongCopy = TEST_SONG.copy(id="holo")
                    val expectedOutput = listOf(
                        TEST_SONG,
                        testSongCopy
                    )
                    //When
                    database.add(
                        TEST_SONG
                    )
                    database.add(
                        testSongCopy
                    )

                    //Then

                    val resultList = getSerializedResults("test-db", Song::class.serializer())

                    resultList shouldContainAll expectedOutput
                    resultList.size shouldBe expectedOutput.size


                }

                "Successful Add With AlbumInfo" {
                    //Given


                    val expectedOutput = listOf(
                        TEST_SONG_WITH_INFO,
                    )
                    //When
                    database.add(
                        TEST_SONG_WITH_INFO
                    )


                    //Then

                    val resultList = getSerializedResults("test-db", Song::class.serializer())

                    resultList shouldContainAll expectedOutput
                    resultList.size shouldBe expectedOutput.size



                }
            }

            "-" - {

            }
        }



    }


}