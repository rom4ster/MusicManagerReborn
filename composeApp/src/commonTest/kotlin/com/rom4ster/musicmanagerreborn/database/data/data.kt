package com.rom4ster.musicmanagerreborn.database.data

import com.rom4ster.musicmanagerreborn.database.AlbumInfo
import com.rom4ster.musicmanagerreborn.database.Song


val TEST_SONG = Song(
    "testId",
    "testName",
    null,
    null,
)

val TEST_SONG_WITH_INFO = TEST_SONG.copy(
    id="testIdAlbum",
    info = AlbumInfo(
        "Edna",
        "Today",
    ),

)