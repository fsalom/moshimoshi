package com.moshimoshi.network.authenticator

import com.moshimoshi.network.authenticationcard.AuthenticationCard
import com.moshimoshi.network.entities.Parameter
import com.moshimoshi.network.entities.Token
import com.moshimoshi.network.entities.Tokens
import com.moshimoshi.network.storage.TokenStorage
import kotlinx.coroutines.runBlocking
import okhttp3.Request

data class RefreshFailed(override val message: String) : Exception(message)

class AuthenticatorImpl (
    override var tokenStore: TokenStorage,
    val card: AuthenticationCard): Authenticator {

    override fun authorize(request: Request.Builder ): Request.Builder  {
        return runBlocking {
            val accessToken = getCurrentToken(emptyList())
            request .addHeader("Authorization", "Bearer $accessToken")
            return@runBlocking request
        }
    }

    override suspend fun login(username: String, password: String) {
        var parameters = mutableListOf<Parameter>()
        parameters.add(Parameter(key = "username", value = username))
        parameters.add(Parameter(key = "password", value = password))
        var tokens = card.getCurrentToken(parameters = parameters)
        if (tokens != null) {
            tokens.accessToken?.let { tokenStore.setAccessToken(it) }
            tokens.refreshToken?.let { tokenStore.setRefreshToken(it) }
        }
    }

    suspend fun checkAndGetTokens(): Tokens {
        val accessToken = tokenStore.getAccessToken()
        val refreshToken = tokenStore.getRefreshToken()
        return if (accessToken != null && accessToken.isValid && refreshToken != null) {
            Tokens(
                accessToken = accessToken,
                refreshToken = refreshToken)
        } else if (refreshToken != null) {
            if (refreshToken.isValid) {
                var tokens = card.refreshAccessToken(refreshToken = refreshToken.value)
                if (tokens != null) {
                    tokens.accessToken?.let { tokenStore.setAccessToken(it) }
                    tokens.refreshToken?.let { tokenStore.setRefreshToken(it) }
                    return tokens
                }
                throw RefreshFailed(message = "TOKENS IS NULL")
            } else {
                throw RefreshFailed(message = "REFRESH NOT VALID")
            }
        } else {
            throw RefreshFailed(message = "REFRESH IS NULL")
        }
    }

    override suspend fun getCurrentToken(parameters: List<Parameter>): String? {
        try {
            val tokens = checkAndGetTokens()
            if (tokens != null) {
                tokens.accessToken?.let { tokenStore.setAccessToken(it) }
                tokens.refreshToken?.let { tokenStore.setRefreshToken(it) }
            }

            if (tokens != null) {
                tokens.accessToken?.let {
                    return it.value
                }
            }
            return tokens.accessToken.value
        } catch (e: Exception) {
            logout()
            return null
        }
    }

    override suspend fun isLogged(): Boolean {
        return tokenStore.getAccessToken()?.isValid ?: false
    }

    override suspend fun logout() {
        tokenStore.clear()
        card.logout()
    }
}