package ru.illine.openai.telegram.bot.config

import com.fasterxml.jackson.databind.ObjectMapper
import com.theokanning.openai.OpenAiApi
import com.theokanning.openai.service.OpenAiService
import okhttp3.ConnectionPool
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import
import org.zalando.logbook.Logbook
import org.zalando.logbook.autoconfigure.LogbookAutoConfiguration
import org.zalando.logbook.okhttp.LogbookInterceptor
import retrofit2.Retrofit
import ru.illine.openai.telegram.bot.config.property.OpenAIProperties
import ru.illine.openai.telegram.bot.model.OpenAIAnswerHistory
import java.io.IOException
import java.util.concurrent.ConcurrentHashMap

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
    fun logbookInterceptor(logbook: Logbook) = LogbookInterceptor(logbook)

    @Bean
    fun okHttpClient(
        connectionPool: ConnectionPool,
        openAIAuthenticationInterceptor: OpenAIAuthenticationInterceptor,
        logbookInterceptor: LogbookInterceptor
    ): OkHttpClient {
        return OkHttpClient.Builder().addInterceptor(openAIAuthenticationInterceptor).addNetworkInterceptor(logbookInterceptor).connectionPool(connectionPool)
            .readTimeout(properties.timeoutInSec.toLong(), properties.timeUnit).build()
    }

    @Bean
    fun retrofit(okHttpClient: OkHttpClient, objectMapper: ObjectMapper) = OpenAiService.defaultRetrofit(okHttpClient, objectMapper)

    @Bean
    fun openAiApi(retrofit: Retrofit) = retrofit.create(OpenAiApi::class.java)

    @Bean
    fun openAi(openAiApi: OpenAiApi) = OpenAiService(openAiApi)

    @Bean
    fun chatToOpenAIAnswers() = ConcurrentHashMap<Long, MutableSet<OpenAIAnswerHistory>>()
}

class OpenAIAuthenticationInterceptor internal constructor(private val token: String) : Interceptor {

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val request: Request = chain.request().newBuilder().header("Authorization", "Bearer $token").build()
        return chain.proceed(request)
    }
}
