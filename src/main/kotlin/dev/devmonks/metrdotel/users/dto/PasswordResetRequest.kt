package dev.devmonks.metrdotel.users.dto

import dev.devmonks.metrdotel.error.validator.Password
import javax.validation.constraints.Email
import javax.validation.constraints.NotBlank

data class PasswordResetRequest(
        @field:NotBlank(message = "Email cannot be blank")
        @field:Email(message = "Invalid email address")
        val email: String,
        @field:NotBlank(message = "Password cannot be blank")
        @field:Password(message = "Invalid password")
        val password: String,
        @field:NotBlank(message = "Confirmation Password cannot be blank")
        @field:Password(message = "Invalid confirmation password")
        val confirmPassword: String
        )