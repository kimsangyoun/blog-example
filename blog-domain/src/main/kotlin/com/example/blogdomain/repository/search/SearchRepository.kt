package com.example.blogdomain.repository.search

import com.example.blogdomain.entity.search.Search
import org.springframework.data.jpa.repository.JpaRepository

interface SearchRepository : JpaRepository<Search, String>