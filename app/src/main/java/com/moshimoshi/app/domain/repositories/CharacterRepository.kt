package com.moshimoshi.app.domain.repositories

import com.moshimoshi.app.domain.entities.Character

interface CharacterRepository {
    suspend fun getCharacters(): List<Character>
}