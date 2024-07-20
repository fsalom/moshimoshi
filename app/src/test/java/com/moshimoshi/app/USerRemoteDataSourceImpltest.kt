package com.moshimoshi.app

import com.moshimoshi.app.data.datasources.character.remote.CharacterRemoteDataSourceImpl
import com.moshimoshi.app.data.datasources.character.remote.dto.CharacterDTO
import com.moshimoshi.app.data.datasources.character.remote.endpoints.CharacterApi
import com.moshimoshi.app.data.datasources.user.remote.UserRemoteDataSourceImpl
import com.moshimoshi.app.data.datasources.user.remote.dto.UserDTO
import com.moshimoshi.app.data.datasources.user.remote.endpoints.UserApi
import com.moshimoshi.network.MoshiMoshi
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import retrofit2.Response

class CharacterRemoteDataSourceImplTest {

    private val mockMoshi: Moshi = mock(Moshi::class.java)
    private val mockCharacterApi: CharacterApi = mock(CharacterApi::class.java)
    private val characterDataSource: CharacterRemoteDataSourceImpl

    init {
        `when`(mockMoshi.create(CharacterApi::class.java)).thenReturn(mockCharacterApi)
        characterDataSource = CharacterRemoteDataSourceImpl(mockMoshi)
    }

    @Test
    fun `getCharacters should return list of CharacterDTO`(): Unit = runBlocking {
        // Given
        val characterList = listOf(CharacterDTO(name = "Test Character"))
        val mockResponse = Response.success(characterList)

        // Simulate the response from the API call
        `when`(mockCharacterApi.getCharacters()).thenReturn(mockResponse)

        // Mock the load method to return the expected result
        `when`(mockMoshi.load(any())).thenAnswer { invocation ->
            val function = invocation.arguments[0] as suspend () -> Response<List<CharacterDTO>>
            function()
        }

        // When
        val result = characterDataSource.getCharacters()

        // Then
        assertEquals(characterList, result)
        verify(mockCharacterApi).getCharacters()
    }
}