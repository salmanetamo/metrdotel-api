package dev.devmonks.metrdotel.users.controller

import dev.devmonks.metrdotel.authentication.model.UserDetails
import dev.devmonks.metrdotel.users.model.User
import dev.devmonks.metrdotel.users.service.IUserService
import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import io.swagger.annotations.ApiResponse
import io.swagger.annotations.ApiResponses
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile

@CrossOrigin(origins = ["*"])
@RestController
@RequestMapping("/users")
@Api("Operations related to users")
class UserController @Autowired constructor(val userService: IUserService) {

    private val logger = LoggerFactory.getLogger(UserController::class.java)

    @Throws(Exception::class)
    @ApiOperation("Gets a list of all users")
    @ApiResponses(value = [
        ApiResponse(code = 200, message = "Returns list of all users", response = Array<User>::class),
        ApiResponse(code = 403, message = "Invalid user"),
        ApiResponse(code = 500, message = "Internal server error")
    ])
    @GetMapping("")
    fun getAllUsers(): ResponseEntity<*> {
        logger.debug("Get all users request...")
        return ResponseEntity(this.userService.getAllUsers(), HttpStatus.OK)
    }

    @Throws(Exception::class)
    @ApiOperation("Add profile picture")
    @ApiResponses(value = [
        ApiResponse(code = 200, message = "Returns whether profile picture was updated", response = Boolean::class),
        ApiResponse(code = 403, message = "Invalid user"),
        ApiResponse(code = 404, message = "User not found"),
        ApiResponse(code = 500, message = "Internal server error")
    ])
    @PutMapping("/profile-picture", consumes = ["multipart/form-data"])
    fun addProfilePicture(@RequestParam(value = "picture") pictureFile: MultipartFile): ResponseEntity<*> {
        return ResponseEntity(
                this.userService.addProfilePicture(
                        this.userService.getUserByEmail(
                                SecurityContextHolder.getContext().authentication.principal.toString()
                        ).id,
                        pictureFile
                ),
                HttpStatus.OK
        )
    }
}