package dev.devmonks.metrdotel.users.listener

import dev.devmonks.metrdotel.shared.mail.service.MailService
import dev.devmonks.metrdotel.users.event.NewActivationTokenEvent
import dev.devmonks.metrdotel.users.event.PasswordResetEvent
import dev.devmonks.metrdotel.users.event.UserSignupEvent
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.event.EventListener
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Component

@Component
class AuthEventsListener @Autowired constructor(private val mailService: MailService) {
    @EventListener
    @Async
    fun handleUserSignupEvent(event: UserSignupEvent) {
        // TODO: Pass base base url and port number dynamically
        val user = event.user
        this.mailService.send(
                to = user.email,
                subject = "[Metrdotel] Confirm your email",
                body = """
                    Hello ${ user.firstName},
                    
                    You registered successfully. To confirm your registration, please click on the below link.
                    
                    http://localhost:8080/${ event.url }
                """.trimIndent(),
                from = null
        )
    }

    @EventListener
    @Async
    fun handleNewActivationTokenEvent(event: NewActivationTokenEvent) {
        // TODO: Pass base base url and port number dynamically
        val user = event.user
        this.mailService.send(
                to = user.email,
                subject = "[Metrdotel] Activate your account",
                body = """
                    Hello ${ user.firstName},
                    
                    You registered successfully. To confirm your registration, please click on the below link.
                    
                    http://localhost:8080/${ event.url }
                """.trimIndent(),
                from = null
        )
    }

    @EventListener
    @Async
    fun handlePasswordResetEvent(event: PasswordResetEvent) {
        // TODO: Pass base base url and port number dynamically
        val user = event.user
        this.mailService.send(
                to = user.email,
                subject = "[Metrdotel] Reset your password",
                body = """
                    Hello ${ user.firstName},
                    
                    You have requested a password reset. Please click on the below link.
                    
                    http://localhost:8080/${ event.url }
                """.trimIndent(),
                from = null
        )
    }
}