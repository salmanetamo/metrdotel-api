package dev.devmonks.metrdotel.users.model

import com.fasterxml.jackson.annotation.JsonFormat
import java.time.LocalDateTime

data class User (
        val id: String,
        val firstName: String,
        val lastName: String,
        val email: String,
        val profilePicture: String,
        val enabled: Boolean,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss")
        val createdAt: LocalDateTime,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss")
        val updatedAt: LocalDateTime,
        val activationToken: ActivationToken?,
        val passwordResetToken: PasswordResetToken?
        )