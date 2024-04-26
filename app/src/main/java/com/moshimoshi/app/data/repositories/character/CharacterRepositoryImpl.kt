package com.moshimoshi.app.data.repositories.character

import com.moshimoshi.app.data.datasources.character.CharacterDataSource
import com.moshimoshi.app.data.datasources.character.remote.dto.CharacterDTO
import com.moshimoshi.app.domain.entities.Character
import com.moshimoshi.app.domain.repositories.CharacterRepository

class CharacterRepositoryImpl(private val remote: CharacterDataSource): CharacterRepository {
    override suspend fun getCharacters(): List<Character> {
        val characters = remote.getCharacters()
        return characters.map { it.toDomain() }
    }

    private fun CharacterDTO.toDomain(): Character {
        return Character(
            name = this.name ?: ""
        )
    }
}