package com.moshimoshi.network.entities
import java.io.Serializable

data class Token(
    var value: String = "",
    var timestampExpires: Long = 0,
): Serializable {
    var isValid: Boolean = false
        get() = timestampExpires > 2
}