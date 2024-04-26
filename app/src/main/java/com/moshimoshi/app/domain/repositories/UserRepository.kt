package com.moshimoshi.app.domain.repositories

interface UserRepository {
    suspend fun getMe()
}