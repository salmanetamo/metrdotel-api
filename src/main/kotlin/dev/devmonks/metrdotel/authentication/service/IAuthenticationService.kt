package dev.devmonks.metrdotel.authentication.service


import dev.devmonks.metrdotel.authentication.model.UserDetails

interface IAuthenticationService {
    fun login(username: String, password: String): UserDetails?
}