package ru.illine.telegram.bot.equestria.model

// https://platform.openai.com/docs/models/gpt-4-and-gpt-4-turbo
enum class GptModelType(
    val displayName: String,
    val model: String
) {

    GPT_4_TEXT_8_K("gpt-4 8k", "gpt-4-0613"),
//    GPT_4_TEXT_32_K("gpt-4 32k", "gpt-4-32k-0613"), // not access
    GPT_4_BETA_TEXT_128_K("gpt-4 turbo 128k", "gpt-4-1106-preview"), // gpt-4-1106-preview
    GPT_4_BETA_IMAGE_128_K("gpt-4 turbo 128k (with image)", "gpt-4-vision-preview"), // gpt-4-vision-preview
    GPT_3_5_TURBO_16_K("gpt-3.5 turbo", "gpt-3.5-turbo-1106") // gpt-3.5-turbo-1106


}