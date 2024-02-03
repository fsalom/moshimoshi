package com.moshimoshi.network.authenticator

import com.moshimoshi.network.authenticationcard.AuthenticationCard
import com.moshimoshi.network.entities.Token
import com.moshimoshi.network.entities.Tokens
import com.moshimoshi.network.storage.TokenStorage
import kotlinx.coroutines.runBlocking
import okhttp3.Request

class Authenticator (val tokenStore: TokenStorage, val card: AuthenticationCard) {

    fun authorize(request: Request.Builder ): Request.Builder  {
        return runBlocking {
            val accessToken = getCurrentToken()
            request .addHeader("Authorization", "Bearer $accessToken")
            return@runBlocking request
        }
    }

    suspend fun checkAndGetTokens(): Tokens {
        val accessToken = tokenStore.getAccessToken()
        val refreshToken = tokenStore.getRefreshToken()
        return if (accessToken != null && accessToken.isValid) {
            Tokens(accessToken = accessToken,
                refreshToken = Token(value = "", timestampExpires = 0),
                idToken = Token(value = "", timestampExpires = 0))
        } else if (refreshToken != null) {
            if (refreshToken.isValid) {
                card.refreshAccessToken(refreshToken = refreshToken.value)
            } else {
                card.logout()
                card.getCurrentToken()
            }
        } else {
            card.logout()
            card.getCurrentToken()
        }
    }

    suspend fun getCurrentToken(): String {
        val tokens = checkAndGetTokens()

        if (tokens != null) {
            tokens.accessToken?.let { tokenStore.setAccessToken(it) }
            tokens.refreshToken?.let { tokenStore.setRefreshToken(it) }
            tokens.idToken?.let { tokenStore.setIdToken(it) }
        }

        if (tokens != null) {
            tokens.accessToken?.let {
                return it.value
            }
        }
        return checkAndGetTokens().accessToken.value

    }

    private suspend fun getNewTokens(refreshToken: String): Tokens {
        return card.refreshAccessToken(refreshToken)
    }
}