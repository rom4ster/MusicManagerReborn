package com.rom4ster.musicmanagerreborn.utils

import com.benasher44.uuid.UuidHasher

private const val UUID_VERSION = 5

private const val UUID5_X500_NAMESPACE = "6ba7b814-9dad-11d1-80b4-00c04fd430c8"


data class MMUUIDHasher(override val version: Int) : UuidHasher {

    private val digest = SHA1()
    private val byteStore = byteArrayOf()
    override fun digest(): ByteArray = digest.digest()

    override fun update(input: ByteArray) {
        digest.update(input)
    }

}