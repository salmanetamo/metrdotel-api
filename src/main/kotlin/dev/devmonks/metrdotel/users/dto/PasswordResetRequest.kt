package dev.devmonks.metrdotel.users.dto

data class PasswordResetRequest(val email: String, val password: String, val confirmPassword: String)