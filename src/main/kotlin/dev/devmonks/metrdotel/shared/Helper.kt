package dev.devmonks.metrdotel.shared

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class Helper {
    companion object {
        fun getDateTimeFromString(dateAsString: String): LocalDateTime {
            return LocalDateTime.parse(dateAsString, DateTimeFormatter.ofPattern(Constants.DATE_TIME_FORMAT.value))
        }
    }
}