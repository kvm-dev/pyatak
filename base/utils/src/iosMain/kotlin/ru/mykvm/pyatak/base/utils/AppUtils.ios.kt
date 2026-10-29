package ru.mykvm.pyatak.base.utils

import platform.posix.exit

actual fun closeApp(){
    exit(0)
}