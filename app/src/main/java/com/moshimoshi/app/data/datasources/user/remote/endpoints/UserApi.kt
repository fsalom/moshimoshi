package com.moshimoshi.app.data.datasources.user.remote.endpoints

import com.moshimoshi.app.data.datasources.character.remote.dto.CharacterDTO
import com.moshimoshi.network.retrofit.Authenticated
import retrofit2.Response
import retrofit2.http.GET

interface UserApi {
    @Authenticated
    @GET("user/me")
    suspend fun getMe(): Response<CharacterDTO>
}