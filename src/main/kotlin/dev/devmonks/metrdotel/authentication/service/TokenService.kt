package dev.devmonks.metrdotel.authentication.service

import com.auth0.jwt.JWT
import com.auth0.jwt.JWTCreator
import com.auth0.jwt.JWTVerifier
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.exceptions.JWTVerificationException
import com.auth0.jwt.interfaces.DecodedJWT
import dev.devmonks.metrdotel.authentication.model.UserDetails
import dev.devmonks.metrdotel.authentication.util.Constants
import dev.devmonks.metrdotel.configuration.security.JWTConfig
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import java.lang.Exception
import java.util.*
import javax.servlet.http.HttpServletRequest

@Component
class TokenService(val jwtConfig: JWTConfig): ITokenService {

    private val logger = LoggerFactory.getLogger(TokenService::class.java)

    override fun getSecretKey(): String {
        return jwtConfig.secretkey
    }

    override fun getDecodedToken(request: HttpServletRequest): DecodedJWT? {
        val token = getTokenText(request)
        return getDecodedTokenFromTokenText(token)
    }

    override fun getEncryptedSecretKey(): Algorithm {
        return Algorithm.HMAC256(getSecretKey())
    }

    override fun getDecodedTokenFromTokenText(token: String): DecodedJWT? {
        return try {
            if (token != "") {
                val algorithm = getEncryptedSecretKey()
                val jwtVerifier: JWTVerifier = JWT.require(algorithm)
                        .withIssuer(Constants.TOKEN_ISSUER.value).build()
                jwtVerifier.verify(token)
            } else null
        } catch (e: JWTVerificationException) {
            logger.error("Token verification failed", e)
            null
        } catch (e: Exception) {
            logger.error("Error in fetching decoded token", e)
            null
        }
    }

    override fun createBasicToken(): JWTCreator.Builder {
        return JWT.create().withIssuer(Constants.TOKEN_ISSUER.value)
    }

    override fun signToken(tokenBuilder: JWTCreator.Builder): String {
        return tokenBuilder.sign(getEncryptedSecretKey())
    }

    override fun generateJwtToken(userDetails: UserDetails): String {
        return this.signToken(
                this.createBasicToken()
                        .withSubject(userDetails.email)
                        .withExpiresAt(Date(Date().time + Constants.TOKEN_EXPIRY_TIME.value.toInt()))
                        .withClaim("firstName", userDetails.firstName)
                        .withClaim("lastName", userDetails.lastName)
        )
    }

    private fun getTokenText(request: HttpServletRequest): String {
        val header = request.getHeader(Constants.TOKEN_HEADER.value)
        return if (!(header == null || "" == header) && header.startsWith(Constants.TOKEN_PREFIX.value)) {
            header.split(Constants.TOKEN_PREFIX.value)[1]
        } else {
            logger.info("Authorization header not present in request")
            ""
        }
    }
}