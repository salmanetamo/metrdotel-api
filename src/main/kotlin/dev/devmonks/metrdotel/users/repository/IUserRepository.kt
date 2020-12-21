package dev.devmonks.metrdotel.users.repository

import dev.devmonks.metrdotel.authentication.model.UserDetails
import dev.devmonks.metrdotel.users.model.ActivationToken
import dev.devmonks.metrdotel.users.model.PasswordResetToken
import dev.devmonks.metrdotel.users.model.User

interface IUserRepository {
    fun loadUserDetailsByUsername(username: String): UserDetails?
    fun getUserByEmail(email: String): User?
    fun createUser(id: String, firstName: String, lastName: String, email: String, password: String): Boolean
    fun getAllUsers(): List<User>
    fun getUserById(id: String): User?
    fun addProfilePicture(id: String, fileName: String): Boolean

    fun createActivationToken(activationToken: ActivationToken): Boolean
    fun getActivationToken(token: String): ActivationToken?
    fun getActivationTokenByEmail(email: String): ActivationToken?
    fun deleteActivationToken(email: String): Boolean
    fun enableUser(email: String): Boolean

    fun createPasswordResetToken(passwordResetToken: PasswordResetToken): Boolean
    fun getPasswordResetToken(token: String): PasswordResetToken?
    fun getPasswordResetTokenByEmail(email: String): PasswordResetToken?
    fun deletePasswordResetToken(email: String): Boolean
    fun resetPassword(email: String, password: String): Boolean
}