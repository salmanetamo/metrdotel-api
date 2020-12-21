package dev.devmonks.metrdotel.configuration.mail

import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.mail.javamail.JavaMailSenderImpl
import java.util.*

@Configuration
@EnableConfigurationProperties(MailConfigurationProperties::class)
class MailConfig (private val mailConfigurationProperties: MailConfigurationProperties) {
    @Bean
    fun javaMailSender(): JavaMailSender {
        val mailSender = JavaMailSenderImpl()
        mailSender.host = mailConfigurationProperties.host
        mailSender.port = mailConfigurationProperties.port
        mailSender.username = mailConfigurationProperties.username
        mailSender.password = mailConfigurationProperties.password
        configureJavaMailProperties(mailSender.javaMailProperties)
        return mailSender
    }

    private fun configureJavaMailProperties(properties: Properties) {
        properties["mail.transport.protocol"] = mailConfigurationProperties.protocol
        properties["mail.smtp.auth"] = mailConfigurationProperties.auth
        properties["mail.smtp.starttls.enable"] = mailConfigurationProperties.starttlsEnable
        properties["mail.debug"] = mailConfigurationProperties.debug
    }
}

