package ru.illine.telegram.bot.equestria.config

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
import ru.illine.telegram.bot.equestria.config.property.GptProperties
import java.io.IOException

@Configuration
@Import(LogbookAutoConfiguration::class) // Spring 3 bug: https://github.com/zalando/logbook/issues/1344
class GptConfig(private val properties: GptProperties) {

    @Bean
    fun objectMapper() = OpenAiService.defaultObjectMapper()

    @Bean
    fun connectionPool(): ConnectionPool {
        return ConnectionPool(properties.maxIdleConnections, properties.keepAliveInSec.toLong(), properties.timeUnit)
    }

    @Bean
    fun gptAuthenticationInterceptor() = GptAuthenticationInterceptor(properties.token)

    @Bean
    fun openAiOkHttpClient(
        connectionPool: ConnectionPool,
        gptAuthenticationInterceptor: GptAuthenticationInterceptor,
        gptLogbookInterceptor: LogbookInterceptor,
        gptGzipInterceptor: GzipInterceptor
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .addNetworkInterceptor(gptLogbookInterceptor)
            .addNetworkInterceptor(gptGzipInterceptor)
            .addInterceptor(gptAuthenticationInterceptor)
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

    class GptAuthenticationInterceptor internal constructor(private val token: String) : Interceptor {

        @Throws(IOException::class)
        override fun intercept(chain: Interceptor.Chain): Response {
            val request: Request = chain.request().newBuilder().header("Authorization", "Bearer $token").build()
            return chain.proceed(request)
        }
    }
}
