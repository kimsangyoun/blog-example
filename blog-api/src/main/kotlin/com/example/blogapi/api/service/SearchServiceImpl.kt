package com.example.blogapi.api.service

import com.example.blogapi.aop.SearchOperation
import com.example.blogapi.api.dto.RankResponseDto
import com.example.blogdomain.entity.search.Search
import com.example.blogdomain.repository.search.SearchRepository
import org.springframework.stereotype.Service
import java.util.stream.Collectors

@Service
class SearchServiceImpl(
    val searchRepository: SearchRepository,
    val searchService: com.example.domainredis.service.SearchService
): SearchService {
    override fun findTopKeyword(rank: Int): MutableList<RankResponseDto> {
        return searchService.searchTopRank(rank).stream().map {
            RankResponseDto.of(it)
        }.collect(Collectors.toList())
    }

    @SearchOperation
    override fun saveSearch(contents:String) {
        searchRepository.save(Search(contents))
    }

}