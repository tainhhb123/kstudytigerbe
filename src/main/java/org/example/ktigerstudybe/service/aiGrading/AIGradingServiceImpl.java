package org.example.ktigerstudybe.service.aiGrading;

import org.example.ktigerstudybe.dto.req.WritingGradingRequest;
import org.example.ktigerstudybe.dto.resp.WritingGradingResult;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * ✨ AI GRADING SERVICE - PHIÊN BẢN CẢI TIẾN + JSON CLEANING
 *
 * CẢI TIẾN CHÍNH:
 * ===============
 * 1. ✅ JSON CLEANING: Tự động sửa JSON malformed từ AI
 * 2. ✅ ERROR HANDLING: Fallback khi parse fail
 * 3. ✅ CROSS-PLATFORM: Chạy ổn định trên mobile + website
 */
@Service
@RequiredArgsConstructor
public class AIGradingServiceImpl implements AIGradingService {

    @Value("${openrouter.api.key}")
    private String openRouterApiKey;

    private static final String OPENROUTER_API_URL = "https://openrouter.ai/api/v1/chat/completions";

    private static final String[] FALLBACK_MODELS = {
            "meta-llama/llama-3.3-70b-instruct:free",
            "deepseek/deepseek-r1-0528:free",
            "mistralai/mistral-small-3.1-24b-instruct:free",
            "qwen/qwen3-4b:free",
            "google/gemma-3-27b-it:free"
    };

    @Value("${openrouter.model:meta-llama/llama-3.3-70b-instruct:free}")
    private String primaryModel;

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    @Override
    public WritingGradingResult gradeWriting(WritingGradingRequest request) {
        if (request.getStudentAnswer() == null || request.getStudentAnswer().trim().isEmpty()) {
            return createEmptyAnswerResult();
        }

        boolean isShort = "SHORT".equalsIgnoreCase(request.getQuestionType());

        // SHORT: Thử exact match
        if (isShort && request.getReferenceAnswer() != null) {
            WritingGradingResult exactMatch = tryExactMatch(request);
            if (exactMatch != null) return exactMatch;
        }

        // Gọi AI
        try {
            String prompt = isShort ? buildShortPrompt(request) : buildImprovedEssayPrompt(request);
            String aiResponse = callOpenRouterAPI(prompt);
            return parseImprovedAIResponse(aiResponse, isShort, request);
        } catch (Exception e) {
            System.err.println("❌ AI Grading Error: " + e.getMessage());
            e.printStackTrace();
            return createFallbackResult(request);
        }
    }

    private WritingGradingResult tryExactMatch(WritingGradingRequest request) {
        String studentAnswer = request.getStudentAnswer().trim();
        String[] possibleAnswers = request.getReferenceAnswer().split("\\|");

        for (String possible : possibleAnswers) {
            String trimmed = possible.trim();
            if (studentAnswer.equalsIgnoreCase(trimmed) ||
                    studentAnswer.replace(" ", "").equalsIgnoreCase(trimmed.replace(" ", ""))) {

                return WritingGradingResult.builder()
                        .score(100)
                        .feedback("✅ Đúng hoàn toàn!")
                        .breakdown(WritingGradingResult.Breakdown.builder()
                                .content(100).grammar(0).vocabulary(0).organization(0).build())
                        .suggestions(List.of("Câu trả lời chính xác, tốt lắm!"))
                        .build();
            }
        }
        return null;
    }

    // ==================== IMPROVED ESSAY PROMPT ====================

    private String buildImprovedEssayPrompt(WritingGradingRequest request) {
        int charCount = request.getStudentAnswer().length();
        int minChars = request.getMinChars() != null ? request.getMinChars() : 200;
        int maxChars = request.getMaxChars() != null ? request.getMaxChars() : 700;
        boolean isQ53 = request.getQuestionNumber() != null && request.getQuestionNumber() == 53;

        return String.format("""
                === CHẤM ĐIỂM BÀI VIẾT TOPIK II - CÂU %d ===
                
                **Loại:** %s
                **Yêu cầu:** %d-%d ký tự | **Thực tế:** %d ký tự (%.1f%%)
                
                📋 **ĐỀ BÀI:**
                %s
                
                %s
                
                ✏️ **BÀI VIẾT CỦA HỌC SINH:**
                %s
                
                === YÊU CẦU ĐÁNH GIÁ CHI TIẾT ===
                
                Bạn cần phân tích BÀI VIẾT CỦA HỌC SINH theo 4 tiêu chí và đưa ra:
                
                1️⃣ **PHÂN TÍCH ĐỘ DÀI:**
                   - Đánh giá độ dài có phù hợp không
                   - Nếu thiếu: thiếu bao nhiêu ký tự, ảnh hưởng như thế nào
                   - Nếu đủ: khen ngợi cụ thể
                
                2️⃣ **NỘI DUNG (40đ):**
                   - Điểm mạnh: ý nào hay, câu nào viết tốt
                   - Điểm yếu: ý nào thiếu, lập luận yếu
                
                3️⃣ **NGỮ PHÁP (30đ):**
                   - Số lỗi: X lỗi
                   - Liệt kê 2-3 lỗi chính: Sai → Đúng
                
                4️⃣ **TỪ VỰNG (20đ):**
                   - Từ cao cấp: liệt kê
                   - Từ sai: sửa lại
                   - Từ lặp: gợi ý thay thế
                
                5️⃣ **TỔ CHỨC (10đ):**
                   - Bố cục, mạch lạc, liên kết
                
                === CÁCH TÍNH ĐIỂM ===
                %s
                
                === JSON FORMAT ===
                {
                  "content": <0-40>, "grammar": <0-30>, "vocabulary": <0-20>, "organization": <0-10>,
                  "detailedFeedback": {
                    "lengthAnalysis": "...",
                    "contentAnalysis": {"score": X, "strengths": [...], "weaknesses": [...]},
                    "grammarAnalysis": {"score": X, "errorCount": Y, "errors": [{"original":"...", "corrected":"..."}]},
                    "vocabularyAnalysis": {"score": X, "advancedWords": [...], "incorrectWords": [...]},
                    "organizationAnalysis": {"score": X, "structure": "..."}
                  },
                  "improvements": [
                    {"type": "GRAMMAR|VOCABULARY|CONTENT", "original": "...", "improved": "...", "explanation": "..."}
                  ]
                }
                
                ⚠️ Trích dẫn NGUYÊN VĂN, đưa 3-5 improvements cụ thể. CHỈ TRẢ JSON.
                """,
                request.getQuestionNumber(),
                isQ53 ? "Mô tả biểu đồ" : "Bài luận nghị luận",
                minChars, maxChars, charCount, (charCount * 100.0) / maxChars,
                request.getQuestionText(),
                request.getReferenceAnswer() != null ?
                        "📖 **BÀI MẪU THAM KHẢO:**\n" + request.getReferenceAnswer() : "",
                request.getStudentAnswer(),
                isQ53 ? buildQ53ScoringRules() : buildQ54ScoringRules(charCount, minChars)
        );
    }

    private String buildQ53ScoringRules() {
        return """
                📊 CONTENT (40đ): Mô tả đúng số liệu + xu hướng + nguyên nhân
                📝 GRAMMAR (30đ): Cấu trúc so sánh, biến đổi, nguyên nhân
                📚 VOCABULARY (20đ): Từ số liệu (증가/감소), so sánh (높다/낮다)
                🏗️ ORGANIZATION (10đ): Mở bài → Mô tả → Kết luận
                
                ⚠️ TRỪ ĐIỂM: 150-199 ký tự (-10đ), 100-149 (-20đ), <100 (-30đ)
                """;
    }

    private String buildQ54ScoringRules(int charCount, int minChars) {
        double factor = charCount >= minChars ? 1.0 :
                charCount >= 500 ? 0.8 :
                        charCount >= 400 ? 0.6 :
                                charCount >= 300 ? 0.5 :
                                        charCount >= 200 ? 0.4 : 0.2;

        return String.format("""
                📊 CONTENT (40đ): Luận điểm + ví dụ + dẫn chứng + lập luận logic
                📝 GRAMMAR (30đ): Câu phức (-(으)ㄴ/는데, -기 때문에), liên từ
                📚 VOCABULARY (20đ): Từ học thuật, đa dạng, collocations
                🏗️ ORGANIZATION (10đ): Mở bài → 2-3 luận điểm → Kết bài
                
                ⚠️ HỆ SỐ TRỪ ĐIỂM: %.1f (áp dụng cho TẤT CẢ tiêu chí)
                - 600-700 ký tự: x1.0
                - 500-599: x0.8
                - 400-499: x0.6
                - 300-399: x0.5
                - 200-299: x0.4
                - <200: x0.2
                """, factor);
    }

    private String buildShortPrompt(WritingGradingRequest request) {
        return String.format("""
                === CHẤM CÂU SHORT - CÂU %d ===
                
                📝 ĐỀ BÀI: %s
                ✅ ĐÁP ÁN MẪU: %s
                📝 CÂU TRẢ LỜI HỌC SINH: %s
                
                === QUY TẮC CHẤM ĐIỂM CHẶT CHẼ ===
                ⚠️ CÂU SHORT phải HOÀN CHỈNH về ngữ pháp, không được thiếu ending (-습니다, -면, -한다, etc.)
                
                100đ: Trùng CHÍNH XÁC với 1 trong các đáp án mẫu (hoặc sai khác tối đa 1 ký tự space/dấu)
                80-90đ: Đúng ý nghĩa + đúng cấu trúc NHƯNG thiếu ending (습니다, 면, 한다, etc.)
                60-70đ: Đúng ý nghĩa nhưng dùng cấu trúc khác
                30-50đ: Đúng 1 phần ý nghĩa, sai ngữ pháp
                0đ: Sai hoàn toàn hoặc nghĩa ngược
                
                Trả về JSON:
                {
                  "content": <0-100>,
                  "grammar": 0,
                  "vocabulary": 0,
                  "organization": 0,
                  "feedback": "<nhận xét rõ ràng: đúng/sai ở đâu, thiếu gì>",
                  "suggestions": ["<đáp án đầy đủ>"]
                }
                """,
                request.getQuestionNumber(),
                request.getQuestionText(),
                request.getReferenceAnswer(),
                request.getStudentAnswer()
        );
    }

    // ==================== ✨ JSON CLEANING (NEW) ====================

    /**
     * 🧹 CLEAN JSON RESPONSE - Fix malformed JSON from AI
     *
     * Common issues fixed:
     * 1. Missing colon: "score"" → "score":
     * 2. Wrong hyphen: ["item", - "item2"] → ["item", "item2"]
     * 3. Arrow in arrays: "text1" -> "text2" → "text1 → text2"
     * 4. Control characters: \x00-\x1F
     * 5. Markdown blocks: ```json ... ```
     */
    private String cleanJsonResponse(String rawJson) {
        if (rawJson == null || rawJson.trim().isEmpty()) {
            return "{}";
        }

        String cleaned = rawJson.trim();

        // 1. Remove markdown code blocks
        cleaned = cleaned.replaceAll("^```json\\s*", "");
        cleaned = cleaned.replaceAll("```\\s*$", "");
        cleaned = cleaned.trim();

        // 2. Fix missing colon after key: "key"" → "key":
        // Pattern: "word"" followed by anything except :
        cleaned = cleaned.replaceAll("\"(\\w+)\"\"(?!:)", "\"$1\":");

        // 3. Fix arrow in arrays: "text1" -> "text2" → "text1 → text2"
        // This converts invalid JSON to valid string format
        cleaned = cleaned.replaceAll("\"([^\"]+)\"\\s*->\\s*\"([^\"]+)\"", "\"$1 → $2\"");

        // 4. Fix wrong hyphen in arrays: , - → ,
        cleaned = cleaned.replaceAll(",\\s*-\\s*(?=[\\[\\{\"\\w])", ", ");

        // 5. Remove control characters (except \r\n\t)
        cleaned = cleaned.replaceAll("[\\x00-\\x08\\x0B\\x0C\\x0E-\\x1F\\x7F]", "");

        // 6. Fix double commas: ,, → ,
        cleaned = cleaned.replaceAll(",\\s*,", ",");

        // 7. Fix trailing commas before closing brackets
        cleaned = cleaned.replaceAll(",\\s*([\\]\\}])", "$1");

        return cleaned;
    }

    // ==================== PARSE IMPROVED RESPONSE (UPDATED) ====================

    /**
     * ✨ UPDATED: Sử dụng cleanJsonResponse() trước khi parse
     */
    private WritingGradingResult parseImprovedAIResponse(String aiResponse, boolean isShort, WritingGradingRequest request) {
        try {
            // ✨ STEP 1: Clean JSON response before parsing
            String cleanedJson = cleanJsonResponse(aiResponse);

            // 📋 Log for debugging (chỉ log 200 chars đầu để tránh spam log)
            System.out.println("📥 Raw AI Response (first 200 chars): " +
                    (aiResponse.length() > 200 ? aiResponse.substring(0, 200) + "..." : aiResponse));
            System.out.println("✨ Cleaned JSON (first 200 chars): " +
                    (cleanedJson.length() > 200 ? cleanedJson.substring(0, 200) + "..." : cleanedJson));

            // ✨ STEP 2: Parse cleaned JSON
            JsonNode root = objectMapper.readTree(cleanedJson);

            WritingGradingResult.Breakdown breakdown = WritingGradingResult.Breakdown.builder()
                    .content(root.path("content").asInt(0))
                    .grammar(root.path("grammar").asInt(0))
                    .vocabulary(root.path("vocabulary").asInt(0))
                    .organization(root.path("organization").asInt(0))
                    .build();

            int totalScore = isShort ? breakdown.getContent() :
                    (breakdown.getContent() + breakdown.getGrammar() +
                            breakdown.getVocabulary() + breakdown.getOrganization());

            // Parse detailed feedback
            String detailedFeedback = buildDetailedFeedbackText(root);

            // Parse improvements
            List<String> suggestions = parseImprovements(root);

            return WritingGradingResult.builder()
                    .score(totalScore)
                    .feedback(detailedFeedback)
                    .breakdown(breakdown)
                    .suggestions(suggestions)
                    .build();

        } catch (com.fasterxml.jackson.core.JsonParseException e) {
            // ✨ JSON parsing error - log chi tiết và fallback
            System.err.println("❌ JSON Parse Error at line " + e.getLocation().getLineNr() +
                    ", column " + e.getLocation().getColumnNr());

            // Log snippet của JSON bị lỗi
            int errorPos = (int) e.getLocation().getCharOffset();
            if (errorPos >= 0 && errorPos < aiResponse.length()) {
                int start = Math.max(0, errorPos - 50);
                int end = Math.min(aiResponse.length(), errorPos + 50);
                System.err.println("📄 Failed JSON snippet: " + aiResponse.substring(start, end));
            }

            // Fallback to length-based scoring
            System.err.println("⚠️ Falling back to length-based scoring due to JSON parse error");
            return createFallbackResult(request);

        } catch (Exception e) {
            System.err.println("❌ Unexpected error in parseImprovedAIResponse: " + e.getMessage());
            e.printStackTrace();

            // Fallback to length-based scoring
            return createFallbackResult(request);
        }
    }

    /**
     * Build detailed feedback text từ JSON structure
     */
    private String buildDetailedFeedbackText(JsonNode root) {
        StringBuilder feedback = new StringBuilder();
        JsonNode detailed = root.path("detailedFeedback");

        if (detailed.isMissingNode()) {
            return root.path("feedback").asText("Đã chấm điểm xong.");
        }

        // Length Analysis
        feedback.append("📏 **Về độ dài:**\n");
        feedback.append(detailed.path("lengthAnalysis").asText()).append("\n\n");

        // Content Analysis
        JsonNode content = detailed.path("contentAnalysis");
        if (!content.isMissingNode()) {
            feedback.append("📊 **Về nội dung** (").append(content.path("score").asInt()).append("/40đ):\n");
            feedback.append("✅ Điểm mạnh:\n");
            content.path("strengths").forEach(s ->
                    feedback.append("  • ").append(s.asText()).append("\n"));
            feedback.append("❌ Điểm yếu:\n");
            content.path("weaknesses").forEach(w ->
                    feedback.append("  • ").append(w.asText()).append("\n"));
            feedback.append("\n");
        }

        // Grammar Analysis
        JsonNode grammar = detailed.path("grammarAnalysis");
        if (!grammar.isMissingNode()) {
            feedback.append("📝 **Về ngữ pháp** (").append(grammar.path("score").asInt()).append("/30đ):\n");
            feedback.append("Số lỗi: ").append(grammar.path("errorCount").asInt()).append(" lỗi\n");
            grammar.path("errors").forEach(err -> {
                feedback.append("  ❌ Sai: ").append(err.path("original").asText())
                        .append(" → ✅ Đúng: ").append(err.path("corrected").asText()).append("\n");
            });
            feedback.append("\n");
        }

        // Vocabulary Analysis
        JsonNode vocab = detailed.path("vocabularyAnalysis");
        if (!vocab.isMissingNode()) {
            feedback.append("📚 **Về từ vựng** (").append(vocab.path("score").asInt()).append("/20đ):\n");
            if (vocab.has("advancedWords")) {
                feedback.append("✨ Từ cao cấp: ");
                vocab.path("advancedWords").forEach(w ->
                        feedback.append(w.asText()).append(", "));
                feedback.append("\n");
            }
            feedback.append("\n");
        }

        // Organization Analysis
        JsonNode org = detailed.path("organizationAnalysis");
        if (!org.isMissingNode()) {
            feedback.append("🏗️ **Về tổ chức** (").append(org.path("score").asInt()).append("/10đ):\n");
            feedback.append(org.path("structure").asText()).append("\n");
        }

        return feedback.toString();
    }

    /**
     * Parse improvements thành list suggestions - ULTRA SAFE MODE
     */
    private List<String> parseImprovements(JsonNode root) {
        try {
            // Tạm thời return null để skip constraint - debug mode
            System.out.println("⚠️ Skipping suggestions due to constraint issues");
            return null;

        } catch (Exception e) {
            System.err.println("❌ Error parsing suggestions: " + e.getMessage());
            return null;
        }
    }

    // ==================== API CALLS ====================

    private String callOpenRouterAPI(String prompt) {
        List<String> modelsToTry = new ArrayList<>();
        modelsToTry.add(primaryModel);
        for (String fallback : FALLBACK_MODELS) {
            if (!fallback.equals(primaryModel)) {
                modelsToTry.add(fallback);
            }
        }

        Exception lastException = null;
        int retryCount = 0;
        final int MAX_RETRIES = 2;

        for (String currentModel : modelsToTry) {
            retryCount = 0;
            while (retryCount <= MAX_RETRIES) {
                try {
                    if (retryCount > 0) {
                        System.out.println("🔄 Retry #" + retryCount + " with: " + currentModel);
                    } else {
                        System.out.println("🤖 Trying model: " + currentModel);
                    }

                    String result = callWithModel(prompt, currentModel);
                    System.out.println("✅ Success with model: " + currentModel);
                    return result;

                } catch (org.springframework.web.client.ResourceAccessException e) {
                    if (e.getMessage() != null && e.getMessage().contains("timeout")) {
                        System.out.println("⏱️ Timeout on " + currentModel + " (attempt " + (retryCount + 1) + "/" + (MAX_RETRIES + 1) + ")");
                        lastException = e;
                        retryCount++;
                        if (retryCount <= MAX_RETRIES) {
                            int waitTime = 2000 * retryCount;
                            try { Thread.sleep(waitTime); } catch (InterruptedException ie) { Thread.currentThread().interrupt(); }
                        }
                        continue;
                    }
                    lastException = e;
                    break;

                } catch (org.springframework.web.client.HttpClientErrorException.TooManyRequests e) {
                    System.out.println("⚠️ Rate limited on " + currentModel);
                    lastException = e;
                    try { Thread.sleep(1000); } catch (InterruptedException ie) { Thread.currentThread().interrupt(); }
                    break;

                } catch (Exception e) {
                    System.out.println("❌ Error with " + currentModel + ": " + e.getMessage());
                    lastException = e;
                    break;
                }
            }
        }

        throw new RuntimeException("All models failed after retries", lastException);
    }

    private String callWithModel(String prompt, String modelName) {
        int maxTokens = 2500;
        if (prompt.contains("SHORT")) maxTokens = 1500;
        else if (prompt.contains("Mô tả biểu đồ")) maxTokens = 2000;

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("model", modelName);
        requestBody.put("temperature", 0.3);
        requestBody.put("max_tokens", maxTokens);

        List<Map<String, String>> messages = new ArrayList<>();

        Map<String, String> systemMsg = new HashMap<>();
        systemMsg.put("role", "system");
        systemMsg.put("content", buildImprovedSystemPrompt());
        messages.add(systemMsg);

        Map<String, String> userMsg = new HashMap<>();
        userMsg.put("role", "user");
        userMsg.put("content", prompt);
        messages.add(userMsg);

        requestBody.put("messages", messages);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(openRouterApiKey);
        headers.set("HTTP-Referer", "https://ktiger-study.com");
        headers.set("X-Title", "KTiger Study TOPIK Grading");

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);

        ResponseEntity<String> response = restTemplate.exchange(
                OPENROUTER_API_URL,
                HttpMethod.POST,
                entity,
                String.class
        );

        try {
            JsonNode root = objectMapper.readTree(response.getBody());
            return root.path("choices").get(0).path("message").path("content").asText();
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse response", e);
        }
    }

    private String buildImprovedSystemPrompt() {
        return """
                Bạn là giáo viên tiếng Hàn chuyên nghiệp, có 10+ năm kinh nghiệm chấm thi TOPIK II.
                
                NHIỆM VỤ: Chấm điểm bài viết TOPIK và đưa ra FEEDBACK CHI TIẾT, CỤ THỂ.
                
                YÊU CẦU QUAN TRỌNG:
                1. PHÂN TÍCH TỪNG TIÊU CHÍ chi tiết (content, grammar, vocab, organization)
                2. TRÍCH DẪN NGUYÊN VĂN câu của học sinh khi chỉ lỗi
                3. ĐƯA RA CÂU SỬA CỤ THỂ từ câu của học sinh
                4. GIẢI THÍCH rõ ràng tại sao sửa như vậy
                5. KHÁCH QUAN, dựa trên đề bài, không so sánh với bài mẫu
                
                LUÔN trả về JSON hợp lệ với cấu trúc đầy đủ.
                """;
    }

    // ==================== FALLBACK ====================

    private WritingGradingResult createEmptyAnswerResult() {
        return WritingGradingResult.builder()
                .score(0)
                .feedback("❌ Bạn chưa viết câu trả lời.")
                .breakdown(WritingGradingResult.Breakdown.builder()
                        .content(0).grammar(0).vocabulary(0).organization(0).build())
                .suggestions(List.of("Hãy đọc kỹ đề bài và viết câu trả lời."))
                .build();
    }

    private WritingGradingResult createFallbackResult(WritingGradingRequest request) {
        int charCount = request.getStudentAnswer() != null ? request.getStudentAnswer().length() : 0;
        int minChars = request.getMinChars() != null ? request.getMinChars() : 100;
        int lengthScore = Math.min(100, (charCount * 100) / Math.max(minChars, 1));
        int score = (int) (lengthScore * 0.5);

        return WritingGradingResult.builder()
                .score(score)
                .feedback("⚠️ Hệ thống AI tạm thời không khả dụng. Điểm tạm tính dựa trên độ dài bài viết.")
                .breakdown(WritingGradingResult.Breakdown.builder()
                        .content((int) (score * 0.4))
                        .grammar((int) (score * 0.3))
                        .vocabulary((int) (score * 0.2))
                        .organization((int) (score * 0.1))
                        .build())
                .suggestions(List.of(
                        "Vui lòng thử lại sau khi hệ thống AI phục hồi.",
                        "Liên hệ giáo viên để được chấm điểm thủ công."))
                .build();
    }
}
