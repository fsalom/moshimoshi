package com.moshimoshi.network.authenticator

import com.moshimoshi.network.authenticationcard.AuthenticationCard
import com.moshimoshi.network.entities.Parameter
import com.moshimoshi.network.entities.Tokens
import com.moshimoshi.network.storage.TokenStorage
import kotlinx.coroutines.runBlocking
import okhttp3.Request

data class RefreshFailed(override val message: String) : Exception(message)
data class LoginFailed(override val message: String) : Exception(message)

class AuthenticatorImpl (
    override var tokenStore: TokenStorage,
    private val headers: List<Parameter>,
    private val card: AuthenticationCard): Authenticator {

    override fun addHeaders(request: Request.Builder): Request.Builder {
        return runBlocking {
            headers.forEach {
                request.addHeader(it.key, it.value)
            }
            return@runBlocking request
        }
    }
    override fun authorize(request: Request.Builder ): Request.Builder  {
        return runBlocking {
            val accessToken = getCurrentToken()
            request.addHeader("Authorization", "Bearer $accessToken")
            return@runBlocking request
        }
    }

    override suspend fun getNewToken(parameters: List<Parameter>) {
        var tokens = card.getCurrentToken(parameters = parameters)
        if (tokens != null) {
            tokens.accessToken?.let { tokenStore.setAccessToken(it) }
            tokens.refreshToken?.let { tokenStore.setRefreshToken(it) }
        } else {
            throw LoginFailed(message = "LOGIN FAILED")
        }
    }

    override suspend fun getCurrentToken(): String? {
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

    private suspend fun checkAndGetTokens(): Tokens {
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
}