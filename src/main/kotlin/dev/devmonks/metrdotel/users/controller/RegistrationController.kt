package dev.devmonks.metrdotel.users.controller


import dev.devmonks.metrdotel.authentication.model.UserDetails
import dev.devmonks.metrdotel.authentication.service.IAuthenticationService
import dev.devmonks.metrdotel.authentication.service.ITokenService
import dev.devmonks.metrdotel.shared.Constants
import dev.devmonks.metrdotel.users.dto.PasswordResetRequest
import dev.devmonks.metrdotel.users.dto.SignupRequest
import dev.devmonks.metrdotel.users.service.IUserService
import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import io.swagger.annotations.ApiResponse
import io.swagger.annotations.ApiResponses
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import javax.validation.Valid

@CrossOrigin(origins = ["*"])
@RestController
@RequestMapping("/auth")
@Api("Operations related to user registration")
class RegistrationController @Autowired constructor(
        private val userService: IUserService,
        private val authenticationService: IAuthenticationService,
        private val tokenService: ITokenService
        ) {

    private val logger = LoggerFactory.getLogger(RegistrationController::class.java)

    @Throws(Exception::class)
    @ApiOperation("Returns user info for given credentials.")
    @ApiResponses(value = [
        ApiResponse(code = 200, message = "Returns user info for given credentials.", response = UserDetails::class),
        ApiResponse(code = 403, message = "Invalid user"),
        ApiResponse(code = 500, message = "Internal server error")
    ])
    @PostMapping("/signup")
    fun signup(@RequestBody @Valid payload: SignupRequest): ResponseEntity<*> {
        try {
            val userCreated = this.userService.createUser(
                    firstName = payload.firstName,
                    lastName = payload.lastName,
                    email = payload.email,
                    password = payload.password
            )
            if (userCreated) {
                val loggedInUser = this.authenticationService.login(payload.email, payload.password)
                return loggedInUser?.let {
                    return ResponseEntity(this.tokenService.generateJwtToken(it), HttpStatus.CREATED)
                } ?: ResponseEntity.status(403).body("Invalid user")
            }
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Constants.RESPONSE_ERROR_MESSAGE.value)
        } catch (e: Exception) {
            logger.debug("Exception occurred while authenticating user. $e")
            e.printStackTrace()
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Constants.RESPONSE_ERROR_MESSAGE.value)
        }
    }

    @Throws(Exception::class)
    @ApiOperation("Returns confirmation that token was resent.")
    @ApiResponses(value = [
        ApiResponse(code = 200, message = "Returns confirmation that token was resent.", response = String::class),
        ApiResponse(code = 403, message = "Invalid user"),
        ApiResponse(code = 500, message = "Internal server error")
    ])
    @GetMapping("/resend-activation-token")
    fun resendActivationToken(@RequestParam email: String): ResponseEntity<*> {
        try {
            if (this.userService.resendActivationToken(email)) {
                return ResponseEntity.ok("New activation email was sent")
            }
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Constants.RESPONSE_ERROR_MESSAGE.value)
        } catch (e: Exception) {
            logger.debug("Exception occurred while creating new activation token. $e")
            e.printStackTrace()
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Constants.RESPONSE_ERROR_MESSAGE.value)
        }
    }

    @Throws(Exception::class)
    @ApiOperation("Returns confirmation that token was resent.")
    @ApiResponses(value = [
        ApiResponse(code = 200, message = "Returns confirmation that token was resent.", response = String::class),
        ApiResponse(code = 403, message = "Invalid user"),
        ApiResponse(code = 500, message = "Internal server error")
    ])
    @GetMapping("/activate-account")
    fun activateAccount(@RequestParam token: String): ResponseEntity<*> {
        try {
            if (this.userService.activateAccount(token)) {
                return ResponseEntity.ok("Verification successful!")
            }
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Constants.RESPONSE_ERROR_MESSAGE.value)
        } catch (e: Exception) {
            logger.debug("Exception occurred while creating new activation token. $e")
            e.printStackTrace()
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Constants.RESPONSE_ERROR_MESSAGE.value)
        }
    }

    // TODO: Move Password Reset logic to dedicated controller
    @Throws(Exception::class)
    @ApiOperation("Returns confirmation that password reset token was sent.")
    @ApiResponses(value = [
        ApiResponse(code = 200, message = "Returns confirmation that password reset token was sent.", response = String::class),
        ApiResponse(code = 403, message = "Invalid user"),
        ApiResponse(code = 500, message = "Internal server error")
    ])
    @GetMapping("/request-password-reset")
    fun requestPasswordReset(@RequestParam email: String): ResponseEntity<*> {
        try {
            if (this.userService.sendPasswordResetToken(email)) {
                return ResponseEntity.ok("Password reset email was sent")
            }
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Constants.RESPONSE_ERROR_MESSAGE.value)
        } catch (e: Exception) {
            logger.debug("Exception occurred while creating password reset token. $e")
            e.printStackTrace()
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Constants.RESPONSE_ERROR_MESSAGE.value)
        }
    }

    @Throws(Exception::class)
    @ApiOperation("Returns confirmation that token was resent.")
    @ApiResponses(value = [
        ApiResponse(code = 200, message = "Returns confirmation that token was resent.", response = String::class),
        ApiResponse(code = 403, message = "Invalid user"),
        ApiResponse(code = 500, message = "Internal server error")
    ])
    @GetMapping("/password-reset-token")
    fun validatePasswordResetToken(@RequestParam token: String): ResponseEntity<*> {
        try {
            if (this.userService.verifyPasswordResetToken(token)) {
                return ResponseEntity.ok("Verification successful!")
            }
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Constants.RESPONSE_ERROR_MESSAGE.value)
        } catch (e: Exception) {
            logger.debug("Exception occurred while creating new activation token. $e")
            e.printStackTrace()
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Constants.RESPONSE_ERROR_MESSAGE.value)
        }
    }

    @Throws(Exception::class)
    @ApiOperation("Returns confirmation that token was resent.")
    @ApiResponses(value = [
        ApiResponse(code = 200, message = "Returns confirmation that token was resent.", response = String::class),
        ApiResponse(code = 403, message = "Invalid user"),
        ApiResponse(code = 500, message = "Internal server error")
    ])
    @PostMapping("/reset-password")
    fun resetPassword(@RequestParam token: String, @RequestBody @Valid payload: PasswordResetRequest): ResponseEntity<*> {
        try {
            if (this.userService.verifyPasswordResetToken(token)) {
                this.userService.resetPassword(payload.email, payload.password)
                val loggedInUser = this.authenticationService.login(payload.email, payload.password)
                return loggedInUser?.let {
                    return ResponseEntity(this.tokenService.generateJwtToken(it), HttpStatus.CREATED)
                } ?: ResponseEntity.status(403).body("Invalid user")
            }
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Constants.RESPONSE_ERROR_MESSAGE.value)
        } catch (e: Exception) {
            logger.debug("Exception occurred while resetting password. $e")
            e.printStackTrace()
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Constants.RESPONSE_ERROR_MESSAGE.value)
        }
    }
}