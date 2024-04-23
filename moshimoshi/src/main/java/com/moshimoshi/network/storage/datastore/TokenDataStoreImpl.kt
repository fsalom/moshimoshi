package com.moshimoshi.network.storage.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import com.moshimoshi.network.entities.Token
import com.moshimoshi.network.storage.TokenStorage
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import java.io.IOException

class TokenDataStoreImpl(uniqueIdentifier: String, private val dataStore: DataStore<Preferences>): TokenStorage {
    private val tag = ""
    private var uniqueIdentifierKey = "UniqueIdentifier"
    private var accessTokenKey = "AccessTokenKey"
    private var accessTokenDateKey = "AccessTokenDateKey"
    private var refreshTokenKey = "RefreshTokenKey"
    private var refreshTokenDateKey = "RefreshTokenDateKey"
    private var idTokenKey = "IdTokenKey"
    private var idTokenDateKey = "IdTokenDateKey"

    init {
        uniqueIdentifierKey = uniqueIdentifier
        setKeysFor(uniqueIdentifier = uniqueIdentifier)
    }

    private inline val Preferences.accessToken
        get() = this[stringPreferencesKey(accessTokenKey)] ?: ""

    private inline val Preferences.expires
        get() = this[longPreferencesKey(accessTokenDateKey)] ?: 0

    private inline val Preferences.refreshToken
        get() = this[stringPreferencesKey(refreshTokenKey)] ?: ""

    private inline val Preferences.refreshTokenExpires
        get() = this[longPreferencesKey(refreshTokenDateKey)] ?: 0

    private inline val Preferences.idToken
        get() = this[stringPreferencesKey(idTokenKey)] ?: ""

    private inline val Preferences.idTokenExpires
        get() = this[longPreferencesKey(idTokenDateKey)] ?: 0

    override suspend fun clear() {
        dataStore.edit { preferences ->
            preferences.remove(longPreferencesKey(uniqueIdentifierKey))
            preferences.remove(longPreferencesKey(accessTokenKey))
            preferences.remove(longPreferencesKey(accessTokenDateKey))
            preferences.remove(longPreferencesKey(refreshTokenKey))
            preferences.remove(longPreferencesKey(refreshTokenDateKey))
            preferences.remove(longPreferencesKey(idTokenKey))
            preferences.remove(longPreferencesKey(idTokenDateKey))
        }
    }

    private val accessTokenFlow: Flow<Token> = dataStore.data
        .catch {
            // throws an IOException when an error is encountered when reading data
            if (it is IOException) {
                emit(emptyPreferences())
            } else {
                throw it
            }
        }
        .map { preferences ->
            Token(
                value = preferences.accessToken,
                timestampExpires = preferences.expires
            )
        }
        .distinctUntilChanged()

    private val refreshTokenFlow: Flow<Token> = dataStore.data
        .catch {
            // throws an IOException when an error is encountered when reading data
            if (it is IOException) {
                emit(emptyPreferences())
            } else {
                throw it
            }
        }
        .map { preferences ->
            Token(
                value = preferences.refreshToken,
                timestampExpires = preferences.refreshTokenExpires
            )
        }
        .distinctUntilChanged()

    private val idTokenFlow: Flow<Token> = dataStore.data
        .catch {
            // throws an IOException when an error is encountered when reading data
            if (it is IOException) {
                emit(emptyPreferences())
            } else {
                throw it
            }
        }
        .map { preferences ->
            Token(
                value = preferences.idToken,
                timestampExpires = preferences.idTokenExpires
            )
        }
        .distinctUntilChanged()

    private fun setKeysFor(uniqueIdentifier: String) {
        accessTokenKey = "shared.$uniqueIdentifier.accessTokenKey"
        accessTokenDateKey = "shared.$uniqueIdentifier.accessTokenDateKey"
        refreshTokenKey = "shared.$uniqueIdentifier.refreshTokenKey"
        refreshTokenDateKey = "shared.$uniqueIdentifier.refreshTokenDateKey"
        idTokenKey = "shared.$uniqueIdentifier.idTokenKey"
        idTokenDateKey = "shared.$uniqueIdentifier.idTokenDateKey"
    }

    override suspend fun getAccessToken(): Token? {
        return accessTokenFlow.first()
    }

    override suspend fun setAccessToken(token: Token) {
        dataStore.edit { it[stringPreferencesKey(accessTokenKey)] = token.value }
        dataStore.edit { it[longPreferencesKey(accessTokenDateKey)] = token.timestampExpires }
    }

    override suspend fun getRefreshToken(): Token? {
        return refreshTokenFlow.first()
    }

    override suspend fun setRefreshToken(token: Token) {
        dataStore.edit { it[stringPreferencesKey(refreshTokenKey)] = token.value }
        dataStore.edit { it[longPreferencesKey(refreshTokenDateKey)] = token.timestampExpires }
    }

    override suspend fun getIdToken(): Token? {
        return idTokenFlow.first()
    }

    override suspend fun setIdToken(token: Token) {
        dataStore.edit { it[stringPreferencesKey(idTokenKey)] = token.value }
        dataStore.edit { it[longPreferencesKey(idTokenDateKey)] = token.timestampExpires }
    }
}