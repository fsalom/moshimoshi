package com.moshimoshi.app.data.datasources.character.remote.endpoints

import com.moshimoshi.app.data.datasources.character.remote.dto.CharacterDTO
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface CharacterApi {
    @GET("character")
    suspend fun getCharacters(): Response<List<CharacterDTO>>
}