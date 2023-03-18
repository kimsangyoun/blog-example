package com.example.blogapi.api.controller.v1.search

import org.springframework.data.domain.Pageable
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/search")
class SearchController {
    @GetMapping
    fun find(
        pageable: Pageable,
        @RequestParam(required = true) query: String
    ): ResponseEntity<String> {
        return ResponseEntity.ok(query + pageable.sort)
    }

}