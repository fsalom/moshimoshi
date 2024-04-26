package com.moshimoshi.app.data.datasources.character

import com.moshimoshi.app.data.datasources.character.remote.dto.CharacterDTO

interface CharacterDataSource {
    suspend fun getCharacters(): List<CharacterDTO>
}