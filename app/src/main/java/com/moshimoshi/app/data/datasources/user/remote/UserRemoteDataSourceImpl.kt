package com.moshimoshi.app.data.datasources.user.remote

import com.moshimoshi.app.data.datasources.user.UserDataSource
import com.moshimoshi.app.data.datasources.user.remote.endpoints.UserApi
import com.moshimoshi.network.MoshiMoshi

class UserRemoteDataSourceImpl(private val moshi: MoshiMoshi): UserDataSource {
    private val eventService = moshi.create(UserApi::class.java)
    override suspend fun getMe() {
        moshi.load {
            eventService.getMe()
        }
    }
}