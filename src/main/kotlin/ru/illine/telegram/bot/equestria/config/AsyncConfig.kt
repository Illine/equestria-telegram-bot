package ru.illine.telegram.bot.equestria.config

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExecutorCoroutineDispatcher
import kotlinx.coroutines.asCoroutineDispatcher
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import ru.illine.telegram.bot.equestria.config.property.AsyncProperties
import java.util.concurrent.Executors

@Configuration
class AsyncConfig {

    @Bean
    fun openAIContext(properties: AsyncProperties) = Executors.newFixedThreadPool(properties.defaultPoolSize).asCoroutineDispatcher()

    @Bean
    fun defaultCoroutineScope(openAIContext: ExecutorCoroutineDispatcher) = CoroutineScope(openAIContext)
}
