package com.example.kakaoclient.configuration

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding
import org.springframework.context.annotation.Configuration

@Configuration
@ConstructorBinding
@ConfigurationProperties(prefix = "kakao.api")
class KakaoConfiguration {
    lateinit var restApiKey: String
    lateinit var baseUrl: String
    lateinit var path: Path

    data class Path(
        val blog: String
    )
}