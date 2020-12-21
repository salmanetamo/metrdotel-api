package dev.devmonks.metrdotel.authentication.filter

import com.auth0.jwt.exceptions.JWTVerificationException
import dev.devmonks.metrdotel.authentication.model.JWTAuthenticationToken
import dev.devmonks.metrdotel.authentication.service.ITokenService
import dev.devmonks.metrdotel.configuration.security.TokenAuthenticationManager
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Component
class AuthenticationFilter @Autowired constructor(private val tokenAuthenticationManager: TokenAuthenticationManager,
                                                  private val tokenService: ITokenService): OncePerRequestFilter() {

    private val loggerAuth = LoggerFactory.getLogger(AuthenticationFilter::class.java)

    override fun doFilterInternal(request: HttpServletRequest, response: HttpServletResponse, filterChain: FilterChain) {
        try{
            val decodedJWT = tokenService.getDecodedToken(request)
            if (decodedJWT != null) {
                val auth: Authentication = tokenAuthenticationManager.authenticate(JWTAuthenticationToken(decodedJWT))
                if (auth.isAuthenticated) {
                    SecurityContextHolder.getContext().authentication = auth
                }
            }
            filterChain.doFilter(request, response)
        } catch (ignored: JWTVerificationException) {
            loggerAuth.error(ignored.toString())
        } catch (e: Exception) {
            loggerAuth.error("Error while setting the authentication context $e")
        }
    }

}