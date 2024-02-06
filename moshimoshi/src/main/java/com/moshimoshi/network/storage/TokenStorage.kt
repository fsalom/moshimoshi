package com.moshimoshi.network.storage

import com.moshimoshi.network.entities.Token

interface TokenStorage {
    suspend fun getAccessToken(): Token?
    suspend fun setAccessToken(token: Token)
    suspend fun getRefreshToken(): Token?
    suspend fun setRefreshToken(token: Token)
    suspend fun getIdToken(): Token?
    suspend fun setIdToken(token: Token)
    suspend fun clear()
}