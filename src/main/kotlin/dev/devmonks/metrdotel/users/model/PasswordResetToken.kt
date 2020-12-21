package dev.devmonks.metrdotel.users.model

import java.time.LocalDateTime

data class PasswordResetToken(
        val id: String,
        val email: String,
        val token: String,
        val expiresAt: LocalDateTime
)