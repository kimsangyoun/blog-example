package com.example.kakaoclient.service

import com.example.kakaoclient.client.KakaoClient
import com.example.kakaoclient.configuration.KakaoConfiguration
import com.example.kakaoclient.dto.KakaoBlogSearchRequestDto
import com.example.kakaoclient.dto.KakaoBlogSearchResponseDto
import com.example.kakaoclient.dto.KakaoErrorDto
import com.example.kakaoclient.exception.CustomKakaoBlogSearchException
import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.stereotype.Service
import org.springframework.util.LinkedMultiValueMap
import org.springframework.web.reactive.function.client.ClientResponse
import reactor.core.publisher.Mono

@Service
class KakaoBlogService(
    private val kakaoClient: KakaoClient,
    private val kakaoConfiguration: KakaoConfiguration
) {
    @Throws(CustomKakaoBlogSearchException::class)
    fun searchBlogByKakaoClient(kakaoBlogSearchResponseDto: KakaoBlogSearchRequestDto): KakaoBlogSearchResponseDto? {
        println("ksy 여기와??")
        val mapperObj = ObjectMapper()
        val webClient = kakaoClient.createWebClient()
        val requestUrl = kakaoConfiguration.path.blog
        val paramMap : HashMap<String,Any> = mapperObj.convertValue(kakaoBlogSearchResponseDto ,object: TypeReference<HashMap<String, Any>>() {} )
        val headersMap = LinkedMultiValueMap<String, String>().apply {
            add(
                HttpHeaders.AUTHORIZATION,
                "KakaoAK " + kakaoConfiguration.restApiKey
            )
        }

        webClient.get()
            .uri { uriBuilder ->
                uriBuilder.path(requestUrl).let { uri ->
                    paramMap.forEach {
                        uri.queryParam(it.key , it.value)
                    }
                }
                uriBuilder.build()
            }
            .accept(MediaType.APPLICATION_JSON)
            .headers { it.addAll(headersMap) }
            .retrieve()
            .onStatus(HttpStatus::is4xxClientError) { response ->
                this.handleError(response)
            }
            .onStatus(HttpStatus::is5xxServerError) { response ->
                this.handleError(response)
            }
            .bodyToMono(KakaoBlogSearchResponseDto::class.java)
            .block().run {
                return this
            }
    }

    fun handleError(clientResponse: ClientResponse): Mono<out Throwable> {
        val errorMessage: Mono<KakaoErrorDto> =
            clientResponse.bodyToMono(KakaoErrorDto::class.java)

        return errorMessage.flatMap { error ->
            return@flatMap Mono.error(CustomKakaoBlogSearchException(error.message, clientResponse.rawStatusCode()))
        }
    }
}