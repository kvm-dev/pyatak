package ru.mykvm.pyatak

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform