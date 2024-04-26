package com.moshimoshi.app.domain.usecases.character

import com.moshimoshi.app.domain.entities.Character

interface CharacterUseCases {
    suspend fun getCharacters(): List<Character>
}