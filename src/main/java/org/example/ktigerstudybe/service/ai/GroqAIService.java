package org.example.ktigerstudybe.service.ai;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

@Service
public class GroqAIService {

    private static final Logger logger = LoggerFactory.getLogger(GroqAIService.class);

    @Value("${groq.api.key}")
    private String groqApiKey;

    @Value("${groq.api.url:https://api.groq.com/openai/v1/chat/completions}")
    private String groqApiUrl;

    @Value("${groq.api.model:llama-3.1-8b-instant}")
    private String groqModel;

    @Value("${groq.api.mock:false}")
    private boolean useMockResponse;

    private final RestTemplate restTemplate;
    private final Random random = new Random();

    public GroqAIService() {
        this.restTemplate = new RestTemplate();
    }

    /**
     * Generate Korean response with conversation history for context
     * @param userMessage Current user message
     * @param scenario Conversation scenario
     * @param difficulty Difficulty level
     * @param conversationHistory Previous messages (optional)
     * @return AI response in Korean
     */
    public String generateKoreanResponse(String userMessage, String scenario, String difficulty, List<Map<String, String>> conversationHistory) {
        if (useMockResponse) {
            logger.info("Using mock response for scenario: {}, difficulty: {}, message: {}",
                    scenario, difficulty, userMessage);
            return getMockResponse(userMessage, scenario, difficulty);
        }

        try {
            String systemPrompt = buildKoreanPrompt(scenario, difficulty);
            logger.info("Calling Groq API with model: {} (history size: {})", groqModel, conversationHistory != null ? conversationHistory.size() : 0);

            // Build messages array for Groq API
            List<Map<String, String>> messages = new ArrayList<>();
            messages.add(Map.of("role", "system", "content", systemPrompt));

            // Add conversation history (last 10 messages to stay within token limit)
            if (conversationHistory != null && !conversationHistory.isEmpty()) {
                int startIndex = Math.max(0, conversationHistory.size() - 10);
                messages.addAll(conversationHistory.subList(startIndex, conversationHistory.size()));
                logger.info("Added {} messages from history", conversationHistory.size() - startIndex);
            }

            // Add current user message
            messages.add(Map.of("role", "user", "content", userMessage));

            Map<String, Object> requestBody = Map.of(
                    "model", groqModel,
                    "messages", messages,
                    "temperature", 0.7,
                    "max_tokens", 120,
                    "top_p", 0.85
            );

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setBearerAuth(groqApiKey);

            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);

            ResponseEntity<Map> response = restTemplate.postForEntity(groqApiUrl, entity, Map.class);

            String result = extractResponseText(response.getBody());
            logger.info("Groq API response: {}", result);
            return result;

        } catch (Exception e) {
            logger.error("Groq API Error: {}", e.getMessage(), e);
            logger.info("Falling back to mock response");
            return getMockResponse(userMessage, scenario, difficulty);
        }
    }

    // Backward compatibility method
    public String generateKoreanResponse(String userMessage, String scenario, String difficulty) {
        return generateKoreanResponse(userMessage, scenario, difficulty, null);
    }

    private String buildKoreanPrompt(String scenario, String difficulty) {
        // Base prompt - Tối ưu cho conversation context
        String basePrompt = """
            당신은 한국어 회화 연습을 도와주는 친절한 한국인입니다.
            
            ⚠️ 핵심 규칙 (절대 지켜야 함):
            1. 대화 내용을 기억하고 이전 대화를 참고하세요
            2. 항상 존댓말(-요/-습니다)을 사용하세요
            3. 상대방의 말을 주의 깊게 듣고 그 내용에 맞게 대답하세요
            4. 상대방이 말한 내용과 모순되는 정보를 절대 말하지 마세요
            5. 1-2문장으로 짧고 명확하게 답하세요
            6. 이모지는 한 번만 사용하세요
            7. 상대방이 이미 말한 내용을 다시 묻지 마세요
            8. 대화의 맥락과 흐름을 유지하세요
            
            올바른 대화 예시:
            상대방: "김치찌개 얼마예요?"
            당신: "김치찌개는 8,000원이에요! 매운 거 괜찮으세요? 😊"
            
            상대방: "네 괜찮아요"
            당신: "좋아요! 그럼 김치찌개 하나 주문해 드릴게요. 음료는 뭐로 하시겠어요?"
            (✅ 이전 대화 기억, 자연스러운 흐름)
            
            상대방: "저는 띤이예요"
            당신: "띤 씨, 반갑습니다! 베트남 분이시죠? 😊"
            (✅ 이름 기억, 맥락 유지)
            
            틀린 예시 (절대 하지 마세요):
            ❌ 상대방이 김치찌개 물어봤는데 갑자기 날씨 얘기하기
            ❌ 상대방이 이미 이름 말했는데 다시 "이름이 뭐예요?" 묻기
            ❌ 상대방이 "네 괜찮아요"라고 했는데 "매운 거 괜찮으세요?" 다시 묻기
            ❌ 대화 흐름 무시하고 새로운 주제로 갑자기 전환
            """;

        // Scenario specific context
        String scenarioContext = getScenarioContext(scenario);

        // Difficulty level
        String difficultyLevel = getDifficultyLevel(difficulty);

        return basePrompt + "\n\n" + scenarioContext + "\n\n" + difficultyLevel;
    }

    private String getScenarioContext(String scenario) {
        return switch (scenario) {
            case "restaurant" -> """
                상황: 한국 레스토랑 직원
                당신의 이름: 민서 (직원)
                메뉴: 김치찌개(8,000원), 불고기(15,000원), 비빔밥(9,000원), 제육볶음(10,000원)
                역할: 메뉴를 추천하고 주문을 받으세요
                중요: 손님이 주문한 메뉴를 기억하고, 추가 주문이나 음료를 자연스럽게 제안하세요
                """;

            case "shopping" -> """
                상황: 옷가게 직원
                당신의 이름: 수진 (직원)
                상품: 의류, 액세서리 (30% 할인 중)
                역할: 상품을 소개하고 사이즈/색상을 안내하세요
                중요: 손님이 관심있는 상품을 기억하고, 관련 상품을 자연스럽게 제안하세요
                """;

            case "direction" -> """
                상황: 서울 시민
                당신의 이름: 준호 (서울 토박이)
                장소: 강남역 근처
                역할: 길을 안내하고 교통편을 추천하세요
                중요: 상대방이 어디 가려고 했는지 기억하고, 추가 정보를 제공하세요
                """;

            case "introduction" -> """
                상황: 처음 만난 친구
                당신의 이름: 지혜 (대학생)
                장소: 홍대 카페
                역할: 자기소개하고 상대방에 대해 물어보세요
                중요: 상대방이 말한 정보(이름, 국적, 직업 등)를 기억하고 대화를 이어가세요
                """;

            case "daily" -> """
                상황: 친한 친구와 일상 대화
                당신의 이름: 태민 (친구)
                장소: 서울
                역할: 일상적인 주제로 편하게 대화하세요 (하지만 존댓말 유지)
                중요: 친구가 말한 계획이나 상황을 기억하고, 자연스럽게 대화를 이어가세요
                """;

            default -> "상황: 한국인과 일반 대화\n중요: 대화 내용을 기억하고 맥락을 유지하세요";
        };
    }

    private String getDifficultyLevel(String difficulty) {
        return switch (difficulty) {
            case "beginner" -> """
                난이도: 초급
                - 매우 간단한 단어와 문장 사용
                - 한 번에 1-2문장만
                - 천천히, 명확하게
                """;

            case "intermediate" -> """
                난이도: 중급
                - 일상적인 어휘 사용
                - 2-3문장
                - 자연스럽게
                """;

            case "advanced" -> """
                난이도: 고급
                - 자연스러운 한국어
                - 관용구 사용 가능
                - 빠르고 자연스럽게
                """;

            default -> "난이도: 중급";
        };
    }

    private String extractResponseText(Map<String, Object> responseBody) {
        try {
            @SuppressWarnings("unchecked")
            List<Map<String, Object>> choices = (List<Map<String, Object>>) responseBody.get("choices");
            if (choices != null && !choices.isEmpty()) {
                @SuppressWarnings("unchecked")
                Map<String, Object> message = (Map<String, Object>) choices.get(0).get("message");
                if (message != null) {
                    String text = (String) message.get("content");
                    return cleanResponse(text);
                }
            }
            return "죄송해요, 다시 말해 주세요.";
        } catch (Exception e) {
            logger.error("Response parsing error: {}", e.getMessage(), e);
            return "미안해요, 잘 못 들었어요.";
        }
    }

    // Dịch sang tiếng Việt
    public String translateToVietnamese(String koreanText) {
        try {
            String prompt = "Dịch câu tiếng Hàn sau sang tiếng Việt tự nhiên, chỉ trả về bản dịch, không giải thích:\n\n" + koreanText;

            List<Map<String, String>> messages = new ArrayList<>();
            messages.add(Map.of("role", "user", "content", prompt));

            Map<String, Object> requestBody = Map.of(
                    "model", groqModel,
                    "messages", messages,
                    "temperature", 0.2,
                    "max_tokens", 150
            );

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setBearerAuth(groqApiKey);

            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);

            ResponseEntity<Map> response = restTemplate.postForEntity(groqApiUrl, entity, Map.class);
            String result = extractResponseText(response.getBody());
            logger.info("Groq Translate result: {}", result);
            return result;
        } catch (Exception e) {
            logger.error("Groq Translate Error: {}", e.getMessage(), e);
            return "(Không dịch được)";
        }
    }

    private String cleanResponse(String response) {
        if (response == null) return "네, 알겠어요!";
        String cleaned = response
                .replaceAll("\\([^)]*\\)", "")
                .replaceAll("\\[[^]]*\\]", "")
                .replaceAll("^AI:", "")
                .replaceAll("^User:", "")
                .replaceAll("^당신:", "")
                .replaceAll("^상대방:", "")
                .trim();
        return cleaned.isEmpty() ? "네!" : cleaned.trim();
    }

    private String getMockResponse(String userMessage, String scenario, String difficulty) {
        try {
            Thread.sleep(800 + random.nextInt(1200));
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        String message = userMessage.toLowerCase();

        return switch (scenario) {
            case "restaurant" -> {
                if (message.contains("메뉴") || message.contains("추천")) {
                    yield "오늘 김치찌개가 정말 맛있어요! 매운 거 괜찮으세요? 😊";
                } else if (message.contains("안녕")) {
                    yield "어서오세요! 몇 분이세요? 😊";
                } else if (message.contains("주문")) {
                    yield "네, 뭘 드시고 싶으세요?";
                } else {
                    yield "네, 말씀하세요! 😊";
                }
            }
            case "shopping" -> {
                if (message.contains("얼마")) {
                    yield "3만 5천원이에요. 지금 30% 할인 중이에요! 👕";
                } else if (message.contains("안녕")) {
                    yield "어서오세요! 구경하세요. 😊";
                } else {
                    yield "네, 도와드릴게요! 😊";
                }
            }
            case "direction" -> {
                if (message.contains("어디") || message.contains("가")) {
                    yield "지하철로 20분 정도 걸려요! 🚇";
                } else if (message.contains("안녕")) {
                    yield "네, 어디 가시려고요? 😊";
                } else {
                    yield "어디로 가시려고 하세요? 😊";
                }
            }
            case "introduction" -> {
                if (message.contains("안녕")) {
                    yield "안녕하세요! 만나서 반가워요. 이름이 어떻게 되세요? 😊";
                } else if (message.contains("이름")) {
                    yield "저는 지혜예요. 반가워요! 😊";
                } else {
                    yield "그렇군요! 한국은 어때요? 😊";
                }
            }
            case "daily" -> {
                if (message.contains("안녕")) {
                    yield "안녕하세요! 오늘 어때요? 😊";
                } else if (message.contains("날씨")) {
                    yield "네, 오늘 날씨 정말 좋아요! 🌤️";
                } else {
                    yield "그렇군요! 재미있어요. 😊";
                }
            }
            default -> "네, 그렇군요! 😊";
        };
    }
}
