package com.example.domainredis.service

import com.example.domainredis.dto.RankDto
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.data.redis.core.ZSetOperations
import org.springframework.stereotype.Service
import java.util.stream.Collectors
import java.util.concurrent.atomic.AtomicInteger




@Service
class SearchService(
    private val redisTemplate: RedisTemplate<String,String>
) {
    fun searchTopRank(rank: Int):MutableList<RankDto> {
        val index = AtomicInteger()
        val zSetOperations: ZSetOperations<String, String> = redisTemplate.opsForZSet()
        val typedTuples = zSetOperations.reverseRangeWithScores("ranking", 0, rank.toLong())?: mutableListOf()
        return typedTuples.stream().map{ it ->
            RankDto(keyword = it.value!!, count = it.score!!.toInt(), rank = index.incrementAndGet())
        }.collect(Collectors.toList())
    }

    fun saveRank(keyword: String) {
        val score = 0.0
        redisTemplate.opsForZSet().incrementScore("ranking", keyword, 1.0)
        redisTemplate.opsForZSet().incrementScore("ranking", keyword, score)
    }
}