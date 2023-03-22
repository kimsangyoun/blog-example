package com.example.domainredis.entity

import org.springframework.data.annotation.Id
import org.springframework.data.redis.core.RedisHash
import java.time.LocalDateTime

@RedisHash(value = "search", timeToLive = 30)
class Search
{
    @Id
    private val keyword: String? = null
    private val createdAt: LocalDateTime = LocalDateTime.now()
}