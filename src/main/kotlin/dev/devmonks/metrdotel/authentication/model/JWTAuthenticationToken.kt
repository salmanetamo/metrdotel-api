package dev.devmonks.metrdotel.authentication.model

import com.auth0.jwt.interfaces.DecodedJWT
import org.springframework.security.core.Authentication
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import java.util.*
import java.util.stream.Collectors

class JWTAuthenticationToken(private val jwt: DecodedJWT): Authentication {

    private var authenticated: Boolean = true

    override fun getName(): String? {
        return null
    }

    override fun getAuthorities(): MutableCollection<out GrantedAuthority> {
        return jwt.claims["roles"]!!.asList(String::class.java).stream()
                .map { s: String -> SimpleGrantedAuthority(s) }
                .collect(Collectors.toList())
    }

    override fun getCredentials(): Any {
        return jwt
    }

    override fun getDetails(): DecodedJWT {
        return jwt
    }

    override fun getPrincipal(): Any {
        return jwt.subject
    }

    override fun isAuthenticated(): Boolean {
        return authenticated && jwt.expiresAt.after(Date())
    }

    @Throws(IllegalArgumentException::class)
    override fun setAuthenticated(p0: Boolean) {
        if (!authenticated) {
            this.authenticated = false
        }
    }
}