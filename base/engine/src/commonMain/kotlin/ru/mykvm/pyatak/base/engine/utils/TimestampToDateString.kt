package ru.mykvm.pyatak.base.engine.utils

import kotlinx.datetime.TimeZone
import kotlinx.datetime.number
import kotlinx.datetime.toLocalDateTime
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

@OptIn(ExperimentalTime::class)
fun Long.timestampToDateString(): String{
    val date = Instant.fromEpochMilliseconds(this*1000).toLocalDateTime(TimeZone.currentSystemDefault())
    val day = if(date.day <10){"0${date.day}"}else{"${date.day}"}
    val month = if(date.month.number <10){"0${date.month.number}"}else{"${date.month.number}"}
    return "$day.$month.${date.year}"
}