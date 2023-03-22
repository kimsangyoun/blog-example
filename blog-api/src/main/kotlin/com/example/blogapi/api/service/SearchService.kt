package com.example.blogapi.api.service

import com.example.blogapi.api.dto.RankResponseDto

interface SearchService {
    fun findTopKeyword(rank: Int): MutableList<RankResponseDto>
    fun saveSearch(contents: String)
}