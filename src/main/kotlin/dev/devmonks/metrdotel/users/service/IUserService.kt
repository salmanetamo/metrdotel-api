package dev.devmonks.metrdotel.users.service

import dev.devmonks.metrdotel.users.model.User
import org.springframework.web.multipart.MultipartFile

interface IUserService {
    fun createUser(firstName: String, lastName: String, email: String, password: String): Boolean
    fun getAllUsers(): List<User>
    fun getUserByEmail(email: String): User
    fun getUserById(id: String): User
    fun addProfilePicture(id: String, pictureFile: MultipartFile): Boolean

    fun resendActivationToken(email: String): Boolean
    fun activateAccount(token: String): Boolean

    fun sendPasswordResetToken(email: String): Boolean
    fun verifyPasswordResetToken(token: String): Boolean
    fun resetPassword(email: String, password: String): Boolean
}