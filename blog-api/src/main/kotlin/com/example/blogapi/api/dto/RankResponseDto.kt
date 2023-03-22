package com.example.blogapi.api.dto

import com.example.domainredis.dto.RankDto

data class RankResponseDto(
    val key: String,
    val rank: Int,
    val count: Int
) {
    companion object {
        fun of(rank: RankDto) = RankResponseDto(
            key = rank.keyword,
            rank = rank.rank,
            count = rank.count
        )
    }
}