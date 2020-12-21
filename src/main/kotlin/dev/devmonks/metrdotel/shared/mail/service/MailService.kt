package dev.devmonks.metrdotel.shared.mail.service

import dev.devmonks.metrdotel.configuration.mail.MailConfigurationProperties
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.mail.SimpleMailMessage
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.stereotype.Component

@Component
class MailService @Autowired constructor(private val mailSender: JavaMailSender,
                                         private val mailConfigurationProperties: MailConfigurationProperties) {

    fun send(to: String, from: String?, body: String, subject: String) {
        val email = SimpleMailMessage()
        email.setTo(to)
        email.setFrom(from?: this.mailConfigurationProperties.from)
        email.setText(body)
        email.setSubject(subject)

        this.mailSender.send(email)
    }
}