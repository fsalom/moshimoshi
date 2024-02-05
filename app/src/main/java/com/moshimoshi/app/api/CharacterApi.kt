package com.moshimoshi.app.api

import com.moshimoshi.app.data.dto.CharacterDTO
import com.moshimoshi.network.retrofit.Authenticated
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface CharacterApi {

    @GET("character/{id}")
    suspend fun getCharacter(@Path("id") id: Int): Response<CharacterDTO>
}