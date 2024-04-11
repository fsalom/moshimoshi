package com.moshimoshi.network.authenticator

import com.moshimoshi.network.authenticationcard.AuthenticationCard
import com.moshimoshi.network.entities.NetworkError
import com.moshimoshi.network.entities.Parameter
import com.moshimoshi.network.entities.Tokens
import com.moshimoshi.network.storage.TokenStorage
import kotlinx.coroutines.runBlocking
import okhttp3.Request
import retrofit2.HttpException
import java.io.IOException
import java.net.SocketTimeoutException

data class RefreshFailed(override val message: String) : Exception(message)

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
        try {
            val tokens = card.getCurrentToken(parameters = parameters)
            tokens.accessToken.let { tokenStore.setAccessToken(it) }
            tokens.refreshToken.let { tokenStore.setRefreshToken(it) }
        } catch (error: Exception) {
            handler(error)
        }
    }

    override suspend fun getCurrentToken(): String? {
        try {
            val tokens = checkAndGetTokens()
            tokens.accessToken.let { tokenStore.setAccessToken(it) }
            tokens.refreshToken.let { tokenStore.setRefreshToken(it) }

            tokens.accessToken.let {
                return it.value
            }
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
                val tokens = card.refreshAccessToken(refreshToken = refreshToken.value)
                tokens.accessToken.let { tokenStore.setAccessToken(it) }
                tokens.refreshToken.let { tokenStore.setRefreshToken(it) }
                return tokens
            } else {
                throw RefreshFailed(message = "REFRESH NOT VALID")
            }
        } else {
            throw RefreshFailed(message = "REFRESH IS NULL")
        }
    }

    private fun handler(error: Exception): Exception {
        return when (error) {
            is HttpException -> {
                val code = error.response()?.code()
                val body = error.response()?.errorBody()?.string() ?: ""
                NetworkError.Failure(code ?: 0, body)
            }
            is SocketTimeoutException -> NetworkError.Timeout
            is IOException -> NetworkError.NoInternet
            is NetworkError -> error
            else -> NetworkError.Unknown
        }
    }
}