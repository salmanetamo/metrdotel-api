package dev.devmonks.metrdotel.users.event

import dev.devmonks.metrdotel.users.model.User
import org.springframework.context.ApplicationEvent


class PasswordResetEvent (val user: User, ) : ApplicationEvent(user) {
    var url: String = "auth/password-reset?token=${ user.passwordResetToken?.token }"
}
