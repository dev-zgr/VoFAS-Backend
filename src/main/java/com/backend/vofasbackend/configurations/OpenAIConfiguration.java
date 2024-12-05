package com.backend.vofasbackend.configurations;

import org.springframework.ai.openai.OpenAiAudioTranscriptionModel;
import org.springframework.ai.openai.OpenAiAudioTranscriptionOptions;
import org.springframework.ai.openai.api.OpenAiAudioApi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
public class OpenAIConfiguration {
    @Value("$openai.apiKey")
    private String apiKey;

    @Bean
    public OpenAiAudioTranscriptionModel transcriptionModel() {
        OpenAiAudioTranscriptionOptions options = OpenAiAudioTranscriptionOptions.builder()
                .withLanguage("tr")
                .withTemperature(0f)
                .withResponseFormat(OpenAiAudioApi.TranscriptResponseFormat.TEXT)
                .build();
        OpenAiAudioApi openAiAudioApi = new OpenAiAudioApi(apiKey);
        return new OpenAiAudioTranscriptionModel(openAiAudioApi);
    }

}

