package com.moshimoshi.app.domain.usecases.user

import com.moshimoshi.app.domain.repositories.UserRepository

class UserUseCasesImpl(private val repository: UserRepository): UserUseCases {
    override suspend fun getMe() {
        repository.getMe()
    }
}