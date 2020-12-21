package dev.devmonks.metrdotel.authentication.model

import io.swagger.annotations.ApiModel
import io.swagger.annotations.ApiModelProperty


@ApiModel(description = "represent the user details returned from authentication")
data class UserDetails (
        @ApiModelProperty("id of user")
        val id: String,
        @ApiModelProperty("first name of user")
        val firstName: String,
        @ApiModelProperty("last name of user")
        val lastName: String,
        @ApiModelProperty("email of user")
        val email: String,
        @ApiModelProperty("flag for whether user is verified")
        val enabled: Boolean = false,
        @ApiModelProperty("password of the user")
        val password: String?
)