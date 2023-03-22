package com.example.blogapi.api.controller

import com.example.blogapi.common.ResponseService
import com.example.blogapi.common.dto.CommonResult
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpStatus
import org.springframework.validation.BindException
import org.springframework.validation.FieldError
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.context.request.WebRequest
import org.springframework.web.servlet.NoHandlerFoundException
import javax.servlet.http.HttpServletRequest

@RestControllerAdvice
@Configuration
class CommonControllerAdvice(
    val responseService: ResponseService
) {

    @ExceptionHandler(NoHandlerFoundException::class)
    fun handleUrlNotFoundException(
        e: NoHandlerFoundException,
        request: HttpServletRequest
    ): CommonResult {
        return responseService.getFailResult(HttpStatus.NOT_FOUND.value(),e.message ?: "서버 장애가 발생했습니다")
    }

    @ExceptionHandler(java.lang.Exception::class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    fun handleException(
        e: java.lang.Exception, request: HttpServletRequest
    ): CommonResult {
        return responseService.getFailResult(HttpStatus.INTERNAL_SERVER_ERROR.value(),e.message ?: "서버 장애가 발생했습니다")
    }

    @ExceptionHandler(java.lang.IllegalArgumentException::class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    fun methodArgumentNotValidException(request: HttpServletRequest, e: IllegalArgumentException): CommonResult {
        return responseService.getFailResult(HttpStatus.BAD_REQUEST.value(),e.toString())
    }

    @ExceptionHandler(value = [BindException::class])
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    protected fun handleInvalidException(
        e: BindException,
        request: WebRequest?
    ): CommonResult {
        return responseService.getFailResult(HttpStatus.BAD_REQUEST.value(), e.bindingResult.allErrors[0].defaultMessage)
    }

    @ExceptionHandler(MethodArgumentNotValidException::class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    fun methodArgumentNotValidException(request: HttpServletRequest, e: MethodArgumentNotValidException): CommonResult {
        val errors: MutableMap<String, String> = HashMap()
        e.bindingResult.allErrors
            .forEach { c ->
                val field = (c as FieldError).field
                c.getDefaultMessage()?.let { errors[field] = it }
            }
        return responseService.getFailResult(HttpStatus.BAD_REQUEST.value(),errors.toString())
    }
}