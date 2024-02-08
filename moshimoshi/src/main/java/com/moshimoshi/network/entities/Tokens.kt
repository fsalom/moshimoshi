package com.moshimoshi.network.entities

import java.io.Serializable

data class Tokens (
    val accessToken: Token,
    val refreshToken: Token
): Serializable