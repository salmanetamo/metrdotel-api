package dev.devmonks.metrdotel.authentication.model

import io.swagger.annotations.ApiModel
import io.swagger.annotations.ApiModelProperty

@ApiModel(description = "represent the container object for user credentials")
data class LoginRequest (
        @ApiModelProperty("username of user")
        val email: String,
        @ApiModelProperty("passport of user")
        val password: String
)