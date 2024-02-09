package com.moshimoshi.network.authenticationcard
import com.moshimoshi.network.entities.Parameter
import com.moshimoshi.network.entities.Tokens

interface AuthenticationCard {
    suspend fun getCurrentToken(parameters: List<Parameter>): Tokens?
    suspend fun refreshAccessToken(refreshToken: String): Tokens?
    suspend fun logout()
}