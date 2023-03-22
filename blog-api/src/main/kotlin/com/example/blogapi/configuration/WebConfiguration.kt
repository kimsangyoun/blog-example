package com.example.blogapi.configuration

import org.springframework.context.annotation.Configuration
import org.springframework.data.domain.PageRequest
import org.springframework.data.web.PageableHandlerMethodArgumentResolver
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer


@Configuration
class WebConfiguration : WebMvcConfigurer {
    override fun addViewControllers(registry: ViewControllerRegistry) {
        registry.addRedirectViewController("/","/swagger-ui/index.html")
    }
    override fun addArgumentResolvers(argumentResolvers: MutableList<HandlerMethodArgumentResolver?>) {
        val pageableArgumentResolver = PageableHandlerMethodArgumentResolver()
        pageableArgumentResolver.setOneIndexedParameters(true)
        pageableArgumentResolver.setMaxPageSize(10)
        pageableArgumentResolver.setFallbackPageable(PageRequest.of(1, 10))
        argumentResolvers.add(pageableArgumentResolver)
    }

}