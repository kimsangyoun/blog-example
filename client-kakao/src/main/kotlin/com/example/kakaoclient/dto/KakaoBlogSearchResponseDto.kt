package com.example.kakaoclient.dto

import java.util.*

data class KakaoBlogSearchResponseDto(
    val meta: meta,
    val documents: MutableList<documents>
)

    data class documents(
        val title: String,
        val contents: String,
        val url: String,
        val blogname: String,
        val thumbnail: String,
        val datetime: Date
    )

    data class meta(
        val total_count: Int,
        val pageable_count: Int,
        val is_end: Boolean,
    )


