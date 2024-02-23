package com.moshimoshi.network.authenticator

import com.moshimoshi.network.entities.Parameter
import com.moshimoshi.network.storage.TokenStorage
import okhttp3.Request

interface Authenticator {
    var tokenStore: TokenStorage
    fun addHeaders(request: Request.Builder): Request.Builder
    fun authorize(request: Request.Builder): Request.Builder
    suspend fun getNewToken(parameters: List<Parameter>)
    suspend fun getCurrentToken(): String?
    suspend fun isLogged(): Boolean
    suspend fun logout()
}