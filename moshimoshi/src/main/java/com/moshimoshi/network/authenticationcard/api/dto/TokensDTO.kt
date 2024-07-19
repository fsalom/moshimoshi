package com.moshimoshi.network.authenticationcard.api.dto

import com.google.gson.annotations.SerializedName
import com.moshimoshi.network.entities.Token
import com.moshimoshi.network.entities.Tokens

data class TokensDTO(
    @SerializedName("access_token") val accessToken: String,
    @SerializedName("expires_in") val expiresIn: Int,
    @SerializedName("refresh_token") val refreshToken: String?
)

fun TokensDTO.toDomain(): Tokens {
    return Tokens(
        accessToken = Token(value = accessToken, timestampExpires = System.currentTimeMillis() + expiresIn * 1000),
        //Refresh token expiration time by default is 365 days
        refreshToken = refreshToken?.let { Token(value = it, timestampExpires = System.currentTimeMillis() + 31536000 * 1000) }
    )
}