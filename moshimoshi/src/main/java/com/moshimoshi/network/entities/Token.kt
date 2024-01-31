package com.moshimoshi.network.entities
import java.io.Serializable

data class Token (
    val value: String = "",
    val expiresIn: Long = 0,
): Serializable