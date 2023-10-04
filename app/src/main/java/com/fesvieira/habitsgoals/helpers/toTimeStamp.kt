package com.fesvieira.habitsgoals.helpers

import java.time.LocalDate
import java.time.ZoneOffset

val LocalDate.toStamp: Long get() {
    return this.atStartOfDay().toEpochSecond(ZoneOffset.UTC)
}