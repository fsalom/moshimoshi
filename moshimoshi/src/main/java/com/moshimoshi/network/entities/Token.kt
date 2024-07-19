package com.moshimoshi.network.entities
import java.io.Serializable

data class Token(
    var value: String = "",
    var timestampExpires: Long = 0,
): Serializable {
    val isValid: Boolean
        get() = timestampExpires > System.currentTimeMillis()
}