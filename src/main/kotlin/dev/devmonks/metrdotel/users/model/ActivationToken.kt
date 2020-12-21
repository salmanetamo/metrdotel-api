package dev.devmonks.metrdotel.users.model

import java.time.LocalDateTime

data class ActivationToken(
        val id: String,
        val email: String,
        val token: String,
        val expiresAt: LocalDateTime
)