package dev.devmonks.metrdotel.authentication.controller

import dev.devmonks.metrdotel.authentication.model.LoginRequest
import dev.devmonks.metrdotel.authentication.model.UserDetails
import dev.devmonks.metrdotel.authentication.service.IAuthenticationService
import dev.devmonks.metrdotel.authentication.service.ITokenService
import dev.devmonks.metrdotel.shared.Constants
import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import io.swagger.annotations.ApiResponse
import io.swagger.annotations.ApiResponses
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.web.bind.annotation.*
import javax.validation.Valid


@CrossOrigin(origins = ["*"])
@RestController
@RequestMapping("/auth")
@Api("Operations related to authentication")
class AuthenticationController @Autowired constructor(
        private val authenticationService: IAuthenticationService,
        private val tokenService: ITokenService
) {

    private val logger = LoggerFactory.getLogger(AuthenticationController::class.java)

    @Throws(Exception::class)
    @ApiOperation("Returns user info for given credentials.")
    @ApiResponses(value = [
        ApiResponse(code = 200, message = "Returns user info for given credentials.", response = UserDetails::class),
        ApiResponse(code = 403, message = "Invalid user"),
        ApiResponse(code = 500, message = "Internal server error")
    ])
    @PostMapping("/login")
    fun login(@RequestBody @Valid payload: LoginRequest): ResponseEntity<*> {
        logger.debug("login user is ${payload.email}")
        try {
            val loggedInUser = this.authenticationService.login(payload.email, payload.password)
            return loggedInUser?.let {
                return ResponseEntity(mapOf("token" to this.tokenService.generateJwtToken(it)), HttpStatus.CREATED)
            } ?: ResponseEntity.status(403).body("Invalid user")
        } catch (e: UsernameNotFoundException) {
            e.printStackTrace()
            logger.debug("Username not found $e")
            return ResponseEntity.status(403).body("Invalid user")
        } catch (e: Exception) {
            e.printStackTrace()
            logger.debug("Exception occurred while authenticate user. $e")
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Constants.RESPONSE_ERROR_MESSAGE.value)
        }
    }
}