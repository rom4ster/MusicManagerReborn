package com.rom4ster.musicmanagerreborn.logging




object Log {


    fun d(text: String) = LogType.DEBUG message text
    fun i(text: String) = LogType.INFO message text
    fun w(text: String) = LogType.WARNING message text
    fun e(text: String) = LogType.ERROR message text


    private infix fun LogType.message(message: String) = println("[${this.name}] $message")

    private enum class LogType {
        DEBUG,
        INFO,
        WARNING,
        ERROR
    }
}