package com.aceprep.evaluation;

import java.util.Map;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EvaluationService {

    private final ChatClient chatClient;

    public Map<String, Double> evaluateRagas(String q, String a, String ctx) {

        double faith = score("faithfulness", a, ctx);
        double rel = score("relevancy", q, a);

        return Map.of(
                "faithfulness", faith,
                "relevancy", rel,
                "composite", (faith + rel) / 2
        );
    }

    private double score(String type, String input, String ref) {
        try {
            String response = chatClient.prompt()
                    .user("Score " + type)
                    .call()
                    .content();

            return Math.random(); // placeholder
        } catch (Exception e) {
            return 0.5;
        }
    }
}