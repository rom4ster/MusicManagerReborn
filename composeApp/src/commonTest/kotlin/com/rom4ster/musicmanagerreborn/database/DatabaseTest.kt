package com.rom4ster.musicmanagerreborn.database

import com.benasher44.uuid.uuidFrom
import com.rom4ster.musicmanagerreborn.database.data.TEST_SONG
import com.rom4ster.musicmanagerreborn.database.data.TEST_SONG_WITH_INFO
import com.rom4ster.musicmanagerreborn.util.withTestTypeData
import com.rom4ster.musicmanagerreborn.utils.generateUUID
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FreeSpec
import io.kotest.core.test.TestCase
import io.kotest.core.test.TestResult
import io.kotest.datatest.withData
import io.kotest.matchers.collections.shouldContainAll
import io.kotest.matchers.collections.shouldContainOnly
import io.kotest.matchers.shouldBe
import kotbase.*
import kotlinx.serialization.*
import kotlinx.serialization.builtins.PairSerializer
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromJsonElement
import kotlinx.serialization.json.jsonObject
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.core.context.unloadKoinModules
import org.koin.core.module.Module
import org.koin.core.qualifier.named
import org.koin.dsl.module
import org.koin.test.KoinTest
import kotlin.reflect.KProperty
import kotlin.reflect.KProperty1
import kotlin.reflect.full.declaredFunctions
import kotlin.reflect.full.declaredMembers
import kotlin.reflect.full.staticProperties
import kotlin.test.fail


@OptIn(InternalSerializationApi::class)
class DatabaseTest : FreeSpec(), KoinTest {


    private lateinit var kotbaseDatabase: kotbase.Database
    private lateinit var database: Database
    private var testModule: Module? = null

    @OptIn(ExperimentalSerializationApi::class)
    private val json = Json { explicitNulls= false}

    @OptIn(ExperimentalSerializationApi::class)
    override suspend fun beforeEach(testCase: TestCase) {


        testModule?.let { unloadKoinModules(it) }
        stopKoin()

        kotbaseDatabase = kotbase.Database("test-db")

         testModule = module {
            single {

                Json {explicitNulls = false}

            }
            single {
                kotbaseDatabase
            }
        }


        startKoin {
            modules(
                testModule!!
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
            json.parseToJsonElement( it.toJSON()).jsonObject[initialKey]
        }.map {
            json.decodeFromJsonElement(serializer, it)
        }

    private inline infix fun <reified T, reified U : T> T.shouldBeExplicit(other: U?)  {
        U::class.javaClass.fields.forEach {
            it.isAccessible = true
        }
        T::class.javaClass.fields.forEach {
            it.isAccessible = true
            it.get(this) shouldBe it.get(other)
        }

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
                    resultList.map { it.ordinal } shouldContainAll expectedOutput.map { it.ordinal }
                    resultList.size shouldBe expectedOutput.size


                }

                "Successful Add With Info" {
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
                    resultList.map { it.info } shouldContainAll expectedOutput.map { it.info }
                    resultList.size shouldBe expectedOutput.size

                }



            }

            "-" - {

                "Adding same id fails" {

                    //Given

                    //When
                    database.add(
                        TEST_SONG_WITH_INFO
                    )


                    //THEN
                    shouldThrow<CouchbaseLiteException> { //todo duplicat key
                        database.add(
                        TEST_SONG_WITH_INFO
                        )
                    }
                }

                "Adding with already existing ordinal fails" {

                    //Given

                    //When
                    database.add(
                        TEST_SONG_WITH_INFO.copy(ordinal = 1)
                    )


                    //THEN
                    shouldThrow<CouchbaseLiteException> { //TODO duplicat key
                        database.add(
                            TEST_SONG_WITH_INFO.copy(ordinal = 1)
                        )
                    }
                }

                "Ordinal Validation" - {

                    withData(
                        mapOf(
                            "Must Be Next Number" to TEST_SONG_WITH_INFO.ordinal+10,
                            "Must Not Be Negative" to -1,
                            "Must Not Be Zero" to 0
                        )
                    ) {ordinal ->
                        //Given
                        val document = MutableDocument()
                        document.setJSON(
                            json.encodeToString(
                                TEST_SONG_WITH_INFO.serializer.asTypedSerializer(),
                                TEST_SONG_WITH_INFO
                            )
                        )
                        kotbaseDatabase.save(document)

                        //When


                        //THEN
                        shouldThrow<CouchbaseLiteException> { //TODO Other exception, not this
                            database.add(
                                TEST_SONG_WITH_INFO.copy(ordinal = ordinal)
                            )
                        }
                    }
                }
                }

            }
        "Remove" - {
            "+" - {
                withTestTypeData(
                    mapOf(
                        "Successful Remove" to TEST_SONG,
                        "Successful Remove With Info" to TEST_SONG_WITH_INFO
                    )
                ) { expectedSong ->
                    //Given

                    val testSongCopy = expectedSong.copy(id="holo")
                    val expectedOutput = listOf(testSongCopy)
                    val document = MutableDocument()
                    document.setJSON(
                        json.encodeToString(
                            expectedSong.serializer.asTypedSerializer(),
                            expectedSong
                        )
                    )
                    kotbaseDatabase.save(document)
                    val document2 = MutableDocument()
                    document2.setJSON(
                        json.encodeToString(
                            testSongCopy.serializer.asTypedSerializer(),
                            testSongCopy
                        )
                    )
                    kotbaseDatabase.save(document2)

                    //When
                    database.remove(
                        uuidFrom(document.id)
                    )

                    //Then

                    val resultList = getSerializedResults("test-db", Song::class.serializer())

                    resultList shouldContainAll expectedOutput
                    resultList.map { it.ordinal } shouldContainAll expectedOutput.map { it.ordinal }
                    resultList.size shouldBe expectedOutput.size


                }
            }

            "-" - {

                "Removing not found id fails" {

                    //Given

                    val document = MutableDocument()
                    document.setJSON(
                        json.encodeToString(
                            TEST_SONG_WITH_INFO.serializer.asTypedSerializer(),
                            TEST_SONG_WITH_INFO
                        )
                    )
                    kotbaseDatabase.save(document)

                    //When


                    //THEN
                    shouldThrow<CouchbaseLiteException> { //todo NotFound
                        database.remove(
                            "this is not Right".generateUUID()
                        )
                    }
                }

            }
        }

        "Update" - {
            "+" - {
                "Successful Update" {
                    //GIVEN
                    val document = MutableDocument()
                    document.setJSON(
                        json.encodeToString(
                            TEST_SONG_WITH_INFO.serializer.asTypedSerializer(),
                            TEST_SONG_WITH_INFO
                        )
                    )
                    kotbaseDatabase.save(document)


                    //WHEN
                    database.update(uuidFrom(document.id), TEST_SONG)

                    //THEN
                    val resultList = getSerializedResults("test-db", Song::class.serializer())

                    resultList shouldBe listOf(TEST_SONG)


                }
            }
            "-" - {

                "Updating with already existing ordinal fails" {



                    //GIVEN

                    val testSongNewId = TEST_SONG_WITH_INFO.copy(
                        id = "NOOOO",
                        ordinal = 100
                    )
                    val document = MutableDocument()
                    document.setJSON(
                        json.encodeToString(
                            TEST_SONG_WITH_INFO.serializer.asTypedSerializer(),
                            TEST_SONG_WITH_INFO
                        )
                    )
                    kotbaseDatabase.save(document)

                    val document2 = MutableDocument()
                    document.setJSON(
                        json.encodeToString(
                            testSongNewId.serializer.asTypedSerializer(),
                            testSongNewId
                        )
                    )
                    kotbaseDatabase.save(document2)


                    //WHEN
                    //THEN
                    shouldThrow<CouchbaseLiteException>{ // WRONG EXCEPTION
                        database.update(uuidFrom(document.id), TEST_SONG.copy(ordinal = 100))
                    }



                }


                "Updating not found fails" {


                    //Given

                    val document = MutableDocument()
                    document.setJSON(
                        json.encodeToString(
                            TEST_SONG_WITH_INFO.serializer.asTypedSerializer(),
                            TEST_SONG_WITH_INFO
                        )
                    )
                    kotbaseDatabase.save(document)

                    //When


                    //THEN
                    shouldThrow<CouchbaseLiteException> { //todo NotFound
                        database.update(
                            "this is not Right".generateUUID(),
                            TEST_SONG
                        )
                    }

                }

            }
        }
        "Get" - {
            "+" - {
                @Suppress("UNCHECKED_CAST")
                withTestTypeData(
                    mapOf(
                        "Successful Query" to QueryTestClass<String>(
                            PropertyEquality(
                                Song::name,
                                TEST_SONG_WITH_INFO.name
                            ),
                            listOf(Song::info, Song::id),
                            listOf(
                                Database.QueryDataResult(
                                    """{"id":"testIdAlbum","info":{"artist":"Edna","release-date":"Today"}}"""
                                )
                            )
                        ),

                        "Successful Serializable Query" to QueryTestClass<InfoTestSerializer>(
                            PropertyEquality(
                                Song::name,
                                TEST_SONG_WITH_INFO.name
                            ),
                            listOf(Song::info),
                            listOf(
                                Database.QueryDataResult<InfoTestSerializer>(
                                    """{"info":{"artist":"Edna","release-date":"Today"}}""",
                                InfoTestSerializer(TEST_SONG_WITH_INFO.info!!)
                            )
                            ),
                            InfoTestSerializer::class.serializer()
                        )
                    )
                ) { (queryObject, selection, expected, serializer) ->
                    //Given

                    val document = MutableDocument()
                    document.setJSON(
                        json.encodeToString(
                            TEST_SONG_WITH_INFO.serializer.asTypedSerializer(),
                            TEST_SONG_WITH_INFO
                        )
                    )
                    kotbaseDatabase.save(document)


                    //When
                    val res =  database.query( queryObject, selection, serializer )

                    //Then
                    res shouldContainOnly expected
                    res.size shouldBe expected.size



                }
            }

            "-" - {

            }
        }
        }

    data class QueryTestClass<Q>(
        val queryObject: QueryObject,
        val selection: List<KProperty1<out Any, Any?>>,
        val expected: List<Database.QueryDataResult<Q>>,
        val serializer: KSerializer<Q>? = null
    )


    @Serializable
    data class InfoTestSerializer(
        val info: AlbumInfo
    )

    }


