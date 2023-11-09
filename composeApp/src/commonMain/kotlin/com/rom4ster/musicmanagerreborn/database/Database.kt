package com.rom4ster.musicmanagerreborn.database

import com.couchbase.lite.CouchbaseLite
import com.couchbase.lite.Database
import inject



class Database {

    init {
        CouchbaseLite.init()
    }
    private val database: Database by inject()


    val p = database.name

}