package dev.devmonks.metrdotel.configuration.security

import dev.devmonks.metrdotel.authentication.filter.AuthenticationFilter
import dev.devmonks.metrdotel.authentication.service.ITokenService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.http.HttpMethod
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.builders.WebSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.crypto.bcrypt.BCrypt
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter

@EnableWebSecurity
class WebSecurityConfiguration: WebSecurityConfigurerAdapter() {

    @Autowired
    private lateinit var tokenAuthenticationManager: TokenAuthenticationManager

    @Autowired
    private lateinit var tokenService: ITokenService

    override fun configure(auth: AuthenticationManagerBuilder) {
        auth.authenticationProvider(this.tokenAuthenticationManager)
    }

    override fun configure(http: HttpSecurity) {
        http.csrf().disable()
                .authorizeRequests()
                .antMatchers(HttpMethod.POST, "/auth/**").permitAll()
                .antMatchers(HttpMethod.GET, "/auth/**").permitAll()
                .anyRequest().authenticated()
                .and()
                .addFilterAfter(AuthenticationFilter(tokenAuthenticationManager, tokenService), BasicAuthenticationFilter ::class.java)
    }

    override fun configure(web: WebSecurity) {
        web.ignoring().antMatchers(HttpMethod.OPTIONS, "/**")
        web.ignoring().antMatchers("/v2/api-docs/**")
        web.ignoring().antMatchers("/swagger.json")
        web.ignoring().antMatchers("/swagger-ui.html")
        web.ignoring().antMatchers("/swagger-resources/**")
        web.ignoring().antMatchers("/webjars/**")
    }

    @Bean
    fun passwordEncoder(): PasswordEncoder {
        return BCryptPasswordEncoder()
    }
}