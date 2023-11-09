package com.rom4ster.musicmanagerreborn.utils

import com.benasher44.uuid.UuidHasher
import com.benasher44.uuid.nameBasedUuidOf
import com.benasher44.uuid.uuidFrom
import org.kotlincrypto.hash.sha1.SHA1

private const val UUID_VERSION = 5

private const val UUID5_X500_NAMESPACE = "6ba7b814-9dad-11d1-80b4-00c04fd430c8"

private fun <T> T.generateUUID() = this generateUUID {it.toString()}

private infix fun <T> T.generateUUID(block: (T)->String) = nameBasedUuidOf(
    uuidFrom(UUID5_X500_NAMESPACE),
    block(this),
    MMUUIDHasher(UUID_VERSION)
)


data class MMUUIDHasher(override val version: Int) : UuidHasher {

    private val digest = SHA1()
    override fun digest(): ByteArray = digest.digest()

    override fun update(input: ByteArray) =
        digest.update(input)


}