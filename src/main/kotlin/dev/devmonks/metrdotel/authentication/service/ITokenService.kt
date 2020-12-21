package dev.devmonks.metrdotel.authentication.service

import com.auth0.jwt.JWTCreator
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.interfaces.DecodedJWT
import dev.devmonks.metrdotel.authentication.model.UserDetails
import javax.servlet.http.HttpServletRequest

interface ITokenService {
    fun getSecretKey(): String
    fun getDecodedToken(request: HttpServletRequest): DecodedJWT?
    fun getEncryptedSecretKey(): Algorithm
    fun getDecodedTokenFromTokenText(token: String): DecodedJWT?
    fun createBasicToken(): JWTCreator.Builder
    fun signToken(tokenBuilder: JWTCreator.Builder): String
    fun generateJwtToken(userDetails: UserDetails): String
}