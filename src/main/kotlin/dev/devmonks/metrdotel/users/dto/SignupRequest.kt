package dev.devmonks.metrdotel.users.dto

import dev.devmonks.metrdotel.error.validator.Password
import io.swagger.annotations.ApiModel
import io.swagger.annotations.ApiModelProperty
import javax.validation.constraints.Email
import javax.validation.constraints.NotBlank

@ApiModel(description = "represent the container object for user signup request")
data class SignupRequest(
        @ApiModelProperty("first name of user")
        val firstName: String,
        @ApiModelProperty("last name of user")
        val lastName: String,
        @ApiModelProperty("email of user")
        @field:NotBlank(message = "Email cannot be blank")
        @Email(message = "Invalid email address")
        val email: String,
        @ApiModelProperty("password of user")
        @field:NotBlank(message = "Password cannot be blank")
        @Password(message = "Invalid password")
        val password: String,
        @ApiModelProperty("confirmation password of user")
        @field:NotBlank(message = "Confirmation Password cannot be blank")
        @field:Password(message = "Invalid confirmation password")
        val confirmPassword: String
)