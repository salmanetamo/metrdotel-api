package dev.devmonks.metrdotel.users.service

import dev.devmonks.metrdotel.error.exception.EntityNotFoundException
import dev.devmonks.metrdotel.shared.Constants
import dev.devmonks.metrdotel.shared.filestorage.FileStorageConstants
import dev.devmonks.metrdotel.shared.filestorage.service.FileStorageService
import dev.devmonks.metrdotel.users.event.NewActivationTokenEvent
import dev.devmonks.metrdotel.users.event.PasswordResetEvent
import dev.devmonks.metrdotel.users.event.UserSignupEvent
import dev.devmonks.metrdotel.users.model.ActivationToken
import dev.devmonks.metrdotel.users.model.PasswordResetToken
import dev.devmonks.metrdotel.users.model.User
import dev.devmonks.metrdotel.users.repository.IUserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.ApplicationEventPublisher
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Component
import org.springframework.web.multipart.MultipartFile
import java.time.LocalDateTime
import java.util.*

@Component
class UserService @Autowired constructor(
        private val userRepository: IUserRepository,
        private val passwordEncoder: PasswordEncoder,
        private val publisher: ApplicationEventPublisher,
        private val fileStorageService: FileStorageService
        ): IUserService {

    override fun createUser(firstName: String, lastName: String, email: String, password: String): Boolean {
        val user = this.userRepository.loadUserDetailsByUsername(email)
        if (user !== null) {
            throw BadCredentialsException("User with $email already exists")
        }
        this.userRepository.createUser(
                id = UUID.randomUUID().toString(),
                firstName = firstName,
                lastName = lastName,
                email = email,
                password = passwordEncoder.encode(password)
        )
        this.createActivationToken(email)
        this.publisher.publishEvent(UserSignupEvent(this.getUserByEmail(email)))
        return true
    }

    override fun getAllUsers(): List<User> {
        return this.userRepository.getAllUsers()
    }

    override fun getUserByEmail(email: String): User {
        val user = this.userRepository.getUserByEmail(email)
        if (user === null) {
            throw EntityNotFoundException(User::class.java, "email", email)
        }
        return user
    }

    override fun getUserById(id: String): User {
        val user = this.userRepository.getUserById(id)
        if (user === null) {
            throw EntityNotFoundException(User::class.java, "id", id)
        }
        return user
    }

    override fun addProfilePicture(id: String, pictureFile: MultipartFile): Boolean {
        this.getUserById(id)
        val fileName: String = this.fileStorageService.storeFile(pictureFile, FileStorageConstants.PROFILE_PIC)
        return this.userRepository.addProfilePicture(id, fileName)
    }

    override fun resendActivationToken(email: String): Boolean {
        this.getUserByEmail(email)
        this.createActivationToken(email)
        this.publisher.publishEvent(
                NewActivationTokenEvent(
                        this.getUserByEmail(email)
                )
        )
        return true
    }

    override fun activateAccount(token: String): Boolean {
        val activationToken = this.userRepository.getActivationToken(token)
        if (activationToken !== null) {
            if (LocalDateTime.now().isBefore(activationToken.expiresAt)) {
                this.userRepository.deleteActivationToken(activationToken.email )
                return this.userRepository.enableUser(activationToken.email)
            }
            throw BadCredentialsException("The token is expired")
        }
        throw BadCredentialsException("The token is not valid")
    }

    override fun sendPasswordResetToken(email: String): Boolean {
        this.getUserByEmail(email)
        this.userRepository.createPasswordResetToken(
                PasswordResetToken(
                        id = UUID.randomUUID().toString(),
                        token = UUID.randomUUID().toString(),
                        expiresAt = LocalDateTime.now()
                                .plusHours(Constants.PASSWORD_RESET_TOKEN_EXPIRATION_IN_HOURS.value.toLong()),
                        email = email
                )
        )
        this.publisher.publishEvent(
                PasswordResetEvent(
                        this.getUserByEmail(email)
                )
        )
        return true
    }

    override fun verifyPasswordResetToken(token: String): Boolean {
        val passwordResetToken = this.userRepository.getPasswordResetToken(token)
        if (passwordResetToken !== null) {
            if (LocalDateTime.now().isBefore(passwordResetToken.expiresAt)) {
                return true
            }
            throw BadCredentialsException("The token is expired")
        }
        throw BadCredentialsException("The token is not valid")
    }

    override fun resetPassword(email: String, password: String): Boolean {
        this.userRepository.deletePasswordResetToken(email)
        return this.userRepository.resetPassword(
                email = email,
                password = passwordEncoder.encode(password)
        )
    }

    fun createActivationToken(email: String): Boolean {
        return this.userRepository.createActivationToken(
                ActivationToken(
                        id = UUID.randomUUID().toString(),
                        token = UUID.randomUUID().toString(),
                        expiresAt = LocalDateTime.now()
                                .plusHours(Constants.ACTIVATION_TOKEN_EXPIRATION_IN_HOURS.value.toLong()),
                        email = email
                )
        )
    }
}