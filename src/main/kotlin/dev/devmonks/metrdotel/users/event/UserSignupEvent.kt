package dev.devmonks.metrdotel.users.event

import dev.devmonks.metrdotel.users.model.User
import org.springframework.context.ApplicationEvent


class UserSignupEvent(val user: User) : ApplicationEvent(user) {
    val url: String = "auth/activate-account?token=${user.activationToken?.token}"
}