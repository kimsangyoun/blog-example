package com.example.blogapi.common.dto

class SingleResult<T>(
    override val status: String,
    override val code: Int,
    override val message: String?,
    val data: T?
) : CommonResult(
    status,
    code,
    message
)