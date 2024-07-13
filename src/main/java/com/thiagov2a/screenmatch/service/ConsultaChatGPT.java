package com.thiagov2a.screenmatch.service;

import com.theokanning.openai.completion.CompletionRequest;
import com.theokanning.openai.service.OpenAiService;

public class ConsultaChatGPT {

    private static final String API_KEY = "${OPENAI_APIKEY}";

    public static String obtenerTraduccion(String texto) {
        OpenAiService service = new OpenAiService(API_KEY);

        CompletionRequest request = CompletionRequest.builder()
                .model("gpt-3.5-turbo-instruct")
                .prompt("traduce a español el siguiente texto: " + texto)
                .maxTokens(1000)
                .temperature(0.7)
                .build();

        var result = service.createCompletion(request);
        return result.getChoices().get(0).getText();
    }
}
