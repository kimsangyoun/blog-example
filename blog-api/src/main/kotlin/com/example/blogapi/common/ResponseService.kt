package com.example.blogapi.common

import com.example.blogapi.common.dto.CommonResult
import com.example.blogapi.common.dto.ListResult
import com.example.blogapi.common.dto.SingleResult
import org.springframework.stereotype.Service

@Service
class ResponseService {
    enum class ResponseCode(var code: Int, var msg: String, var status: String) {
        SUCCESS(200, "성공하였습니다." , "SUCCESS"),
        FAIL(500, "실패했습니다", "FAIL")
    }

    fun <T> getSingleResult(data: T, code: Int = ResponseCode.SUCCESS.code, additionalData: Map<String, Any>? = null): SingleResult<T> = SingleResult(
        ResponseCode.SUCCESS.status,
        code,
        ResponseCode.SUCCESS.msg,
        data
    )

    fun <T> getListResult(list: List<T>, code: Int = ResponseCode.SUCCESS.code, additionalData: Map<String, Any>? = null): ListResult<T> = ListResult(
        ResponseCode.SUCCESS.status,
        code,
        ResponseCode.SUCCESS.msg,
        list
    )

    fun getFailResult(code: Int, msg: String?): CommonResult = CommonResult(
        ResponseCode.FAIL.status,
        code,
        msg
    )

}