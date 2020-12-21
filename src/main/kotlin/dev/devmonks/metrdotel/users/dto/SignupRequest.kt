package dev.devmonks.metrdotel.users.dto

import io.swagger.annotations.ApiModel
import io.swagger.annotations.ApiModelProperty

@ApiModel(description = "represent the container object for user signup request")
data class SignupRequest (
        @ApiModelProperty("first name of user")
        val firstName: String,
        @ApiModelProperty("last name of user")
        val lastName: String,
        @ApiModelProperty("email of user")
        val email: String,
        @ApiModelProperty("password of user")
        val password: String,
        @ApiModelProperty("confirmation password of user")
        val confirmPassword: String
)