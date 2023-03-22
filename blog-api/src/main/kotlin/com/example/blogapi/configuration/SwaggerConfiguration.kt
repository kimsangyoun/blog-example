package com.example.blogapi.configuration

import io.swagger.v3.oas.annotations.OpenAPIDefinition
import io.swagger.v3.oas.annotations.servers.Server
import io.swagger.v3.oas.models.ExternalDocumentation
import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Info
import io.swagger.v3.oas.models.info.License
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@OpenAPIDefinition(servers = [Server(url = "/", description = "local Server URL")])
@Configuration
class SwaggerConfiguration {
    @Bean
    fun searchOpenAPI(): OpenAPI? {

        return OpenAPI()
            .info(
                Info().title("Search Api For Blog")
                    .description("블로그 검색 API")
                    .version("v0.0.1")
                    .license(License().name("SANGYOUNKIM").url(""))
            )
            .externalDocs(
                ExternalDocumentation()
                    .description("Blog Search Wiki Documentation")
                    .url("")
            )
    }

}