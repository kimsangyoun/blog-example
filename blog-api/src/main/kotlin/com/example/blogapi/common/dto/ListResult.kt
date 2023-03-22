package com.example.blogapi.common.dto

class ListResult<T>(
    override val status: String,
    override val code: Int,
    override val message: String?,
    val data: List<T>?
) : CommonResult(
    status,
    code,
    message
)