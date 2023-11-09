package ru.illine.telegram.bot.equestria.service.gpt.impl

import com.theokanning.openai.service.OpenAiService
import org.springframework.stereotype.Service
import ru.illine.telegram.bot.equestria.config.property.GptProperties
import ru.illine.telegram.bot.equestria.model.GptModelType
import ru.illine.telegram.bot.equestria.service.gpt.AbstractGptService

@Service("defaultGptService")
class Gpt3Turbo16kServiceImpl(
    gptProperties: GptProperties,
    openAi: OpenAiService
) : AbstractGptService(gptProperties, openAi) {

    override fun getModel() = GptModelType.GPT_3_5_TURBO_16_K

}
