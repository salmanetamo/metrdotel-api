package dev.devmonks.metrdotel.configuration.security

import dev.devmonks.metrdotel.authentication.model.JWTAuthenticationToken
import org.slf4j.LoggerFactory
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.authentication.AuthenticationServiceException
import org.springframework.security.core.Authentication
import org.springframework.security.core.AuthenticationException
import org.springframework.stereotype.Component
import java.time.LocalDateTime
import java.time.ZoneOffset

@Component
class TokenAuthenticationManager: AuthenticationManager, AuthenticationProvider {
    private val logger = LoggerFactory.getLogger(TokenAuthenticationManager::class.java)

    @Throws(AuthenticationException::class)
    override fun authenticate(authentication: Authentication): Authentication {
        if (authentication !is JWTAuthenticationToken) {
            logger.error("Cannot authenticate " + authentication.javaClass)
            throw AuthenticationServiceException("Cannot authenticate " + authentication.javaClass)
        }

        if (LocalDateTime.ofEpochSecond(authentication.details.expiresAt.time, 0, ZoneOffset.UTC)
                        .isBefore(LocalDateTime.now())) {
            logger.error("Authentication error")
            throw AuthenticationServiceException("Token has expired")
        }
        return authentication
    }

    override fun supports(authentication: Class<*>?): Boolean {
        return authentication == JWTAuthenticationToken::class.java
    }

}
