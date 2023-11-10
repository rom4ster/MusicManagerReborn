package com.rom4ster.musicmanagerreborn.database


import inject
import kotbase.Database
import kotbase.Document
import kotbase.MutableDocument
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.serialization.serializer


class Database {



    private val database: Database by inject()
    private val json: Json by inject()





     fun <T : AbstractEntity> add(entity: T) {

        val document = MutableDocument()
        document.setJSON(json.encodeToString(entity.serializer))
        database.save(document)
    }



}