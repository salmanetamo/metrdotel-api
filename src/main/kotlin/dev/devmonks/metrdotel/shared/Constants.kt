package dev.devmonks.metrdotel.shared

enum class Constants constructor(val value: String) {
    RESPONSE_ERROR_MESSAGE("Something went wrong!"),

    DATE_TIME_FORMAT("dd-MM-yyyy hh:mm:ss"),

    ACTIVATION_TOKEN_EXPIRATION_IN_HOURS("24"),

    PASSWORD_RESET_TOKEN_EXPIRATION_IN_HOURS("24")
}