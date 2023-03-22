package com.example.blogapi

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication


@ConfigurationPropertiesScan
@SpringBootApplication(
    scanBasePackages = ["com.example"]
)
class BlogExampleApplication


fun main(args: Array<String>) {
    runApplication<BlogExampleApplication>(*args)
}