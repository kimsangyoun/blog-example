package com.example.kakaoclient.client

import com.example.kakaoclient.configuration.KakaoConfiguration
import io.netty.channel.ChannelOption
import io.netty.handler.logging.LogLevel
import io.netty.handler.ssl.SslContextBuilder
import io.netty.handler.ssl.util.InsecureTrustManagerFactory
import io.netty.handler.timeout.ReadTimeoutHandler
import io.netty.handler.timeout.WriteTimeoutHandler
import io.netty.resolver.DefaultAddressResolverGroup
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.http.client.reactive.ReactorClientHttpConnector
import org.springframework.http.codec.ClientCodecConfigurer
import org.springframework.http.codec.LoggingCodecSupport
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.ClientResponse
import org.springframework.web.reactive.function.client.ExchangeFilterFunction
import org.springframework.web.reactive.function.client.ExchangeStrategies
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Mono
import reactor.netty.http.client.HttpClient
import reactor.netty.tcp.SslProvider.SslContextSpec
import reactor.netty.transport.logging.AdvancedByteBufFormat
import java.time.Duration
import java.util.*
import java.util.concurrent.TimeUnit
import java.util.function.Consumer

@Component
class KakaoClient(
    private val kakaoConfiguration: KakaoConfiguration
) {

    fun createWebClient(): WebClient {
        val exchangeStrategies: ExchangeStrategies = ExchangeStrategies.builder()
            .codecs { configurer: ClientCodecConfigurer ->
                configurer.defaultCodecs().maxInMemorySize(1024 * 1024 * 50)
            }
            .build()

        exchangeStrategies
            .messageWriters().stream()
            .filter { obj: Any? -> LoggingCodecSupport::class.java.isInstance(obj) }
            .forEach { writer -> (writer as LoggingCodecSupport).isEnableLoggingRequestDetails = true }

        val sslContext = SslContextBuilder.forClient()
            .trustManager(InsecureTrustManagerFactory.INSTANCE)
            .build()


        val httpClient: HttpClient = HttpClient.create()
            .wiretap(
                "reactor.netty.http.client.HttpClient",
                LogLevel.DEBUG, AdvancedByteBufFormat.TEXTUAL)
            .secure { t: SslContextSpec -> t.sslContext(sslContext) }
            .resolver(DefaultAddressResolverGroup.INSTANCE)
            .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 3000)
            .responseTimeout(Duration.ofMillis(3000))
            .doOnConnected { conn ->
                conn.addHandlerLast(ReadTimeoutHandler(3000, TimeUnit.MILLISECONDS))
                    .addHandlerLast(WriteTimeoutHandler(3000, TimeUnit.MILLISECONDS))
            }

        return WebClient.builder()
            .clientConnector(
                ReactorClientHttpConnector(
                httpClient)
            )
            .baseUrl(kakaoConfiguration.baseUrl)
            .exchangeStrategies(exchangeStrategies)
            .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .defaultUriVariables(Collections.singletonMap("url", kakaoConfiguration.baseUrl))
            .filters { exchangeFilterFunctions ->
                exchangeFilterFunctions.add(logRequest())
                exchangeFilterFunctions.add(logResponse())
            }
            .build()
    }

    private fun logRequest(): ExchangeFilterFunction? {
        return ExchangeFilterFunction.ofRequestProcessor { clientRequest ->
            //logger.info("Request: {} {}", clientRequest.method(), clientRequest.url())

            clientRequest.headers()
                .forEach { name, values -> values.forEach { value ->/* logger.info("ksy :: {}={}", name, value)*/ } }
            Mono.just(clientRequest)
        }
    }

    private fun logResponse(): ExchangeFilterFunction? {
        return ExchangeFilterFunction.ofResponseProcessor { clientResponse: ClientResponse ->
           // logger.info("Response status: {}", clientResponse.statusCode())
            clientResponse.headers().asHttpHeaders()
                .forEach { name: String?, values: List<String?> ->
                    values.forEach(
                        Consumer { value: String? ->
                            //logger.info("{}={}", name, value)
                        })
                }
            Mono.just(clientResponse)
        }
    }

}