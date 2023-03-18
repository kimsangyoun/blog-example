package com.example.blogapi

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class BlogExampleApplication

fun main(args: Array<String>) {
    runApplication<BlogExampleApplication>(*args)
}