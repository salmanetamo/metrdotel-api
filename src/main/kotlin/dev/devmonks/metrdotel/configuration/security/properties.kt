package dev.devmonks.metrdotel.configuration.security

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration
import org.springframework.stereotype.Component

@Component
@Configuration
@ConfigurationProperties(prefix ="jwt")
class JWTConfig {
    lateinit var secretkey : String
}