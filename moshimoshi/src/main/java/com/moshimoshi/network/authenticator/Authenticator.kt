package com.moshimoshi.network.authenticator

import com.moshimoshi.network.entities.Parameter
import com.moshimoshi.network.storage.TokenStorage
import okhttp3.Request

interface Authenticator {
    var tokenStore: TokenStorage
    fun authorize(request: Request.Builder ): Request.Builder
    suspend fun login(username: String, password: String)
    suspend fun getCurrentToken(parameters: List<Parameter>): String?
    suspend fun isLogged(): Boolean
    suspend fun logout()
}