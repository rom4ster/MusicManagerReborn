package com.rom4ster.musicmanagerreborn.error



object DatabaseErrors {
    fun operationFailure(operation: String) = "Could not perform operation: $operation"
}