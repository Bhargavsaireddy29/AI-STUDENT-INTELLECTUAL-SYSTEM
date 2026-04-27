package com.aceprep.rag;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.ai.vectorstore.filter.FilterExpressionBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class AdvancedRagService {

    private final VectorStore vectorStore;
    private final ChatClient chatClient;
    private final MeterRegistry meterRegistry;

    @Value("${app.rag.top-k:10}")
    private int topK;

    @Value("${app.rag.re-rank-top-k:5}")
    private int reRankTopK;

    @Value("${app.rag.multi-query-count:3}")
    private int multiQueryCount;

    private static final int RRF_K = 60;

    /**
     * MAIN ENTRY — FULL PIPELINE
     */
    @Cacheable(value = "ragResults", key = "#query + '_' + #subjectCode")
    public List<Document> retrieve(String query, String subjectCode, Long studentId) {

        Timer.Sample timer = Timer.start(meterRegistry);

        try {
            // 1️⃣ MULTI QUERY EXPANSION
            List<String> queries = expandQuery(query);

            // 2️⃣ HYBRID RETRIEVAL
            Map<String, List<Integer>> rankings = new HashMap<>();
            Map<String, Document> docMap = new HashMap<>();

            for (String q : queries) {
                List<Document> docs = retrieveHybrid(q, subjectCode);

                for (int i = 0; i < docs.size(); i++) {
                    Document d = docs.get(i);
                    String id = d.getId();

                    docMap.putIfAbsent(id, d);
                    rankings.computeIfAbsent(id, k -> new ArrayList<>()).add(i + 1);
                }
            }

            // 3️⃣ RRF MERGE
            List<Document> merged = applyRRF(rankings, docMap);

            // 4️⃣ RE-RANK
            List<Document> reRanked = reRank(query, merged);

            // 5️⃣ COMPRESS
            return compress(query, reRanked);

        } finally {
            timer.stop(
                Timer.builder("rag.pipeline.duration")
                        .register(meterRegistry)
            );
        }
    }

    /**
     * 1️⃣ MULTI QUERY EXPANSION
     */
    private List<String> expandQuery(String query) {
        try {
            String prompt = """
                Generate %d variations of this academic query.
                Return JSON array only.
                Query: "%s"
                """.formatted(multiQueryCount, query);

            String response = chatClient.prompt()
                    .user(prompt)
                    .call()
                    .content();

            List<String> queries = parseJsonArray(response);
            queries.add(0, query);

            return queries.stream().distinct().limit(multiQueryCount + 1).toList();

        } catch (Exception e) {
            log.warn("Query expansion failed: {}", e.getMessage());
            return List.of(query);
        }
    }

    /**
     * 2️⃣ HYBRID RETRIEVAL
     */
    private List<Document> retrieveHybrid(String query, String subjectCode) {

        SearchRequest.Builder builder = SearchRequest.builder()
                .query(query)
                .topK(topK)
                .similarityThreshold(0.6);

        if (subjectCode != null) {
            FilterExpressionBuilder filter = new FilterExpressionBuilder();
            builder.filterExpression(filter.eq("subject_code", subjectCode).build());
        }

        return vectorStore.similaritySearch(builder.build());
    }

    /**
     * 3️⃣ RRF (RECIPROCAL RANK FUSION)
     */
    private List<Document> applyRRF(Map<String, List<Integer>> rankings,
                                   Map<String, Document> docMap) {

        Map<String, Double> scores = new HashMap<>();

        for (var entry : rankings.entrySet()) {
            double score = entry.getValue().stream()
                    .mapToDouble(rank -> 1.0 / (RRF_K + rank))
                    .sum();

            scores.put(entry.getKey(), score);
        }

        return scores.entrySet().stream()
                .sorted(Map.Entry.<String, Double>comparingByValue().reversed())
                .limit(topK)
                .map(e -> docMap.get(e.getKey()))
                .toList();
    }

    /**
     * 4️⃣ RE-RANK USING LLM
     */
    private List<Document> reRank(String query, List<Document> docs) {

        if (docs.size() <= reRankTopK) return docs;

        try {
            String docsText = docs.stream()
                    .map(d -> d.getText())
                    .collect(Collectors.joining("\n---\n"));

            String prompt = """
                Rank the following documents based on relevance to query:
                Query: %s
                Documents:
                %s
                Return top %d indexes as JSON array.
                """.formatted(query, docsText, reRankTopK);

            String response = chatClient.prompt()
                    .user(prompt)
                    .call()
                    .content();

            List<Integer> indexes = parseIntegerArray(response);

            return indexes.stream()
                    .filter(i -> i < docs.size())
                    .map(docs::get)
                    .toList();

        } catch (Exception e) {
            log.warn("Re-ranking failed: {}", e.getMessage());
            return docs.subList(0, Math.min(reRankTopK, docs.size()));
        }
    }

    /**
     * 5️⃣ CONTEXT COMPRESSION
     */
    private List<Document> compress(String query, List<Document> docs) {

        return docs.stream()
                .map(doc -> {
                    String text = doc.getText();

                    // simple compression (you can upgrade later)
                    if (text.length() > 500) {
                        text = text.substring(0, 500);
                    }

                    return new Document(doc.getId(), text, doc.getMetadata());
                })
                .toList();
    }

    /**
     * JSON PARSER HELPERS
     */
    private List<String> parseJsonArray(String json) {
        try {
            json = json.replace("[", "").replace("]", "").replace("\"", "");
            return Arrays.stream(json.split(","))
                    .map(String::trim)
                    .toList();
        } catch (Exception e) {
            return List.of();
        }
    }

    private List<Integer> parseIntegerArray(String json) {
        try {
            json = json.replaceAll("[^0-9,]", "");
            return Arrays.stream(json.split(","))
                    .map(Integer::parseInt)
                    .toList();
        } catch (Exception e) {
            return List.of();
        }
    }
}