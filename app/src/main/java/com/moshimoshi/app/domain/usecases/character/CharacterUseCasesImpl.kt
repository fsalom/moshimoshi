package com.moshimoshi.app.domain.usecases.character

import com.moshimoshi.app.domain.entities.Character
import com.moshimoshi.app.domain.repositories.CharacterRepository

class CharacterUseCasesImpl(private val repository: CharacterRepository): CharacterUseCases {
    override suspend fun getCharacters(): List<Character> {
        return repository.getCharacters()
    }
}