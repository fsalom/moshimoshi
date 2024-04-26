package com.moshimoshi.app.data.datasources.character.remote

import com.moshimoshi.app.data.datasources.character.CharacterDataSource
import com.moshimoshi.app.data.datasources.character.remote.dto.CharacterDTO
import com.moshimoshi.app.data.datasources.character.remote.endpoints.CharacterApi
import com.moshimoshi.network.MoshiMoshi

class CharacterRemoteDataSourceImpl(private val moshi: MoshiMoshi): CharacterDataSource {
    private val eventService = moshi.create(CharacterApi::class.java)

    override suspend fun getCharacters(): List<CharacterDTO> {
        return moshi.load {
            eventService.getCharacters()
        }
    }
}