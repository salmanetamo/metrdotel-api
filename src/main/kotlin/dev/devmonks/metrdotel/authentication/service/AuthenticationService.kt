package dev.devmonks.metrdotel.authentication.service


import dev.devmonks.metrdotel.authentication.model.UserDetails
import dev.devmonks.metrdotel.users.repository.IUserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Component

@Component
class AuthenticationService @Autowired constructor(
        private val userRepository: IUserRepository,
        private val passwordEncoder: PasswordEncoder
        ) : IAuthenticationService {
    override fun login(username: String, password: String): UserDetails? {
        val user = this.userRepository.loadUserDetailsByUsername(username)
        if (user !== null) {
            return if (passwordEncoder.matches(password, user.password)) {
                user
            } else null
        }
        throw UsernameNotFoundException("Username not found")
    }
}