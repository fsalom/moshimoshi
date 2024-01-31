package com.moshimoshi.network.storage

import com.moshimoshi.network.entities.Token

interface TokenStorageInterface {
    val accessToken: Token
    val refreshToken: Token
    val idToken: Token
}