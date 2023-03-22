package com.example.kakaoclient.exception

class CustomKakaoBlogSearchException (
    message: String,
    code: Int
): RuntimeException() {
    override val message: String = message
    val code: Int = code
}