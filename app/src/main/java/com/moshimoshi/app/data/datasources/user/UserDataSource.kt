package com.moshimoshi.app.data.datasources.user

interface UserDataSource {
    suspend fun getMe()
}