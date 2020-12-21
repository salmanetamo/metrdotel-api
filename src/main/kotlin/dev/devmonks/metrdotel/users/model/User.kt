package dev.devmonks.metrdotel.users.model

import java.time.LocalDateTime

data class User (
        val id: String,
        val firstName: String,
        val lastName: String,
        val email: String,
        val profilePicture: String,
        val enabled: Boolean,
        val createdAt: LocalDateTime,
        val updatedAt: LocalDateTime,
        val activationToken: ActivationToken?,
        val passwordResetToken: PasswordResetToken?
        )