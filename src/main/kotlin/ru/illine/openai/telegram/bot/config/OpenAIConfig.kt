package ru.illine.openai.telegram.bot.config

import com.fasterxml.jackson.databind.ObjectMapper
import com.theokanning.openai.client.OpenAiApi
import com.theokanning.openai.service.OpenAiService
import okhttp3.*
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import
import org.zalando.logbook.autoconfigure.LogbookAutoConfiguration
import org.zalando.logbook.okhttp.GzipInterceptor
import org.zalando.logbook.okhttp.LogbookInterceptor
import retrofit2.Retrofit
import ru.illine.openai.telegram.bot.config.property.OpenAIProperties
import java.io.IOException

@Configuration
@Import(LogbookAutoConfiguration::class) // Spring 3 bug: https://github.com/zalando/logbook/issues/1344
class OpenAIConfig(private val properties: OpenAIProperties) {

    @Bean
    fun objectMapper() = OpenAiService.defaultObjectMapper()

    @Bean
    fun connectionPool(): ConnectionPool {
        return ConnectionPool(properties.maxIdleConnections, properties.keepAliveDuration.toLong(), properties.timeUnit)
    }

    @Bean
    fun openAIAuthenticationInterceptor() = OpenAIAuthenticationInterceptor(properties.token)

    @Bean
    fun openAIOkHttpClient(
        connectionPool: ConnectionPool,
        openAIAuthenticationInterceptor: OpenAIAuthenticationInterceptor,
        logbookInterceptor: LogbookInterceptor,
        gzipInterceptor: GzipInterceptor
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .addNetworkInterceptor(logbookInterceptor)
            .addNetworkInterceptor(gzipInterceptor)
            .addInterceptor(openAIAuthenticationInterceptor)
            .connectionPool(connectionPool)
            .readTimeout(properties.timeoutInSec.toLong(), properties.timeUnit)
            .build()
    }

    @Bean
    fun retrofit(openAIOkHttpClient: OkHttpClient, objectMapper: ObjectMapper) =
        OpenAiService.defaultRetrofit(openAIOkHttpClient, objectMapper)

    @Bean
    fun openAiApi(retrofit: Retrofit) = retrofit.create(OpenAiApi::class.java)

    @Bean
    fun openAi(openAiApi: OpenAiApi) = OpenAiService(openAiApi)
}

class OpenAIAuthenticationInterceptor internal constructor(private val token: String) : Interceptor {

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val request: Request = chain.request().newBuilder().header("Authorization", "Bearer $token").build()
        return chain.proceed(request)
    }
}
