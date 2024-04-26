package com.moshimoshi.app.data.repositories.user

import com.moshimoshi.app.data.datasources.user.UserDataSource
import com.moshimoshi.app.domain.repositories.UserRepository

class UserRepositoryImpl(val remote: UserDataSource): UserRepository {
    override suspend fun getMe() {
        remote.getMe()
    }

}
