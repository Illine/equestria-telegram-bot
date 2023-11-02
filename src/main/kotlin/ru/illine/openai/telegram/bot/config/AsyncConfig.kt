package ru.illine.openai.telegram.bot.config

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExecutorCoroutineDispatcher
import kotlinx.coroutines.asCoroutineDispatcher
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import ru.illine.openai.telegram.bot.config.property.AsyncProperties
import java.util.concurrent.Executors

@Configuration
class AsyncConfig {

    @Bean
    fun openAIContext(properties: AsyncProperties) = Executors.newFixedThreadPool(properties.openAIPoolSize).asCoroutineDispatcher()

    @Bean
    fun defaultCCoroutineScope(openAIContext: ExecutorCoroutineDispatcher) = CoroutineScope(openAIContext)
}
