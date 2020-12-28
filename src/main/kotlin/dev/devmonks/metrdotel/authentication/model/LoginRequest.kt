package dev.devmonks.metrdotel.authentication.model

import dev.devmonks.metrdotel.error.validator.Password
import io.swagger.annotations.ApiModel
import io.swagger.annotations.ApiModelProperty
import javax.validation.constraints.Email
import javax.validation.constraints.NotBlank

@ApiModel(description = "represent the container object for user credentials")
data class LoginRequest(
        @ApiModelProperty("username of user")
        @field:NotBlank(message = "Email cannot be blank")
        @Email(message = "Invalid email address")
        val email: String,
        @ApiModelProperty("passport of user")
        @field:NotBlank(message = "Password cannot be blank")
        @field:Password(message = "Invalid password")
        val password: String
)