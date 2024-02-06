package com.moshimoshi.network.authenticationcard.api.dto

import com.google.gson.annotations.SerializedName
import com.moshimoshi.network.entities.Token
import com.moshimoshi.network.entities.Tokens

data class TokensDTO(
    @SerializedName("access_token") val accessToken: String,
    @SerializedName("expires_in") val expiresIn: Int,
    @SerializedName("refresh_token") val refreshToken: String
)

fun TokensDTO.toDomain(): Tokens {
    return Tokens(
        accessToken = Token(value = accessToken, timestampExpires = System.currentTimeMillis() + expiresIn * 1000),
        refreshToken = Token(value = refreshToken, timestampExpires = System.currentTimeMillis() + 648000 * 1000)
    )
}