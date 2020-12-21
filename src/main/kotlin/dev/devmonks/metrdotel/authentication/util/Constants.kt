package dev.devmonks.metrdotel.authentication.util

enum class Constants  constructor(val value: String) {
    SESSION_ID("sessionId"),
    TOKEN_ISSUER("auth0"),
    TOKEN_HEADER("Authorization"),
    TOKEN_PREFIX("Bearer "),
    TOKEN_EXPIRY_TIME("3600000")
}