package com.example.blogapi.common.dto

import io.swagger.v3.oas.annotations.media.Schema

open class CommonResult(
    @Schema(
        title = "응답 성공여부",
        example = "true/false"
    )
    open val status: String? = null,
    @Schema(
        title = "응답 코드 번호",
        example = " 200 = 정상, 200 != 비정상"
    )
    open val code: Int = 200,
    @Schema(title = "응답 메시지")
    open val message: String? = null
)