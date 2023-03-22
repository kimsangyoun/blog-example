package com.example.blogapi.api.dto

import javax.validation.constraints.Max
import javax.validation.constraints.Min
import javax.validation.constraints.NotNull

data class BlogSearchRequestDto(
    @field:NotNull(message = "검색어는 필수값입니다.")
    val query: String?,
    @field:Min(value = 1, message = "페이지 번호는 1 이상이여야 합니다.")
    @field:Max(value = 50, message = "페이지 번호는 50 이하이여야 합니다.")
    val pageNumber: Int?= 10,

    @field:Min(value = 1, message = "페이지 크기는 1 이상이여야 합니다.")
    @field:Max(value = 50, message = "페이지 크기는 50 이하이여야 합니다.")
    val pageSize: Int?= 10,
    val sort: String?
)
