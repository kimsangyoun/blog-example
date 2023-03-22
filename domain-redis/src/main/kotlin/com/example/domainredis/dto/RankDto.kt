package com.example.domainredis.dto

data class RankDto(
    val keyword: String,
    val count: Int,
    val rank: Int
) {
}