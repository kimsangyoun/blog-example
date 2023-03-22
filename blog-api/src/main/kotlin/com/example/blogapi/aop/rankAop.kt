package com.example.blogapi.aop

import com.example.domainredis.service.SearchService
import org.aspectj.lang.JoinPoint
import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.annotation.Before
import org.aspectj.lang.reflect.MethodSignature
import org.springframework.stereotype.Component


@Aspect
@Component
class rankAop(
    private val searchService: SearchService
) {
    @Before("@annotation(com.example.blogapi.aop.SearchOperation) && args(keyword, ..)")
    @Throws(Throwable::class)
    fun withMetricsService(joinPoint: JoinPoint, keyword:String) {
        searchService.saveRank(keyword)
    }
}