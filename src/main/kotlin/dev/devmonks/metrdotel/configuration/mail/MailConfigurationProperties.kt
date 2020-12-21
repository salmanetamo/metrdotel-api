package dev.devmonks.metrdotel.configuration.mail

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding

@ConstructorBinding
@ConfigurationProperties(prefix = "email")
class MailConfigurationProperties(
        val host: String,
        val port: Int,
        val username: String,
        val password: String,
        val protocol: String,
        val auth: Boolean,
        val starttlsEnable: Boolean,
        val debug: Boolean,
        val from: String,
)