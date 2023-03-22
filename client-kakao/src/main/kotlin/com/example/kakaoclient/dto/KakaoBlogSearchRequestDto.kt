package com.example.kakaoclient.dto

data class KakaoBlogSearchRequestDto(
    val query: String,
    val sort: String?,
    val page: Int?,
    val size: Int?
)

