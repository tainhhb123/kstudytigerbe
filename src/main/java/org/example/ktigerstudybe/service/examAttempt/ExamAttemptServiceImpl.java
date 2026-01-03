package org.example.ktigerstudybe.service.examAttempt;

import org.example.ktigerstudybe.dto.req.ExamAttemptRequest;
import org.example.ktigerstudybe.dto.req.WritingGradingRequest;
import org.example.ktigerstudybe.dto.resp.ExamAttemptResponse;
import org.example.ktigerstudybe.dto.resp.ExamResultResponse;
import org.example.ktigerstudybe.dto.resp.QuestionResultResponse;
import org.example.ktigerstudybe.dto.resp.SectionResultResponse;
import org.example.ktigerstudybe.dto.resp.WritingGradingResult;
import org.example.ktigerstudybe.enums.ExamAttemptStatus;
import org.example.ktigerstudybe.enums.QuestionType;
import org.example.ktigerstudybe.model.*;
import org.example.ktigerstudybe.repository.*;
import org.example.ktigerstudybe.service.aiGrading.AIGradingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * ExamAttemptServiceImpl - Đã tích hợp AI Grading cho WRITING
 *
 * LOGIC CHẤM ĐIỂM:
 * - LISTENING/READING (MCQ): Tự động so sánh với đáp án đúng
 * - WRITING:
 *   + SHORT (Q51, Q52): AI so sánh với đáp án mẫu → partial credit
 *   + ESSAY (Q53, Q54): AI đánh giá dựa trên đề bài
 */
@Service
public class ExamAttemptServiceImpl implements ExamAttemptService {

    @Autowired
    private ExamAttemptRepository examAttemptRepository;

    @Autowired
    private ExamRepository examRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserAnswerRepository userAnswerRepository;

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private AnswerChoiceRepository answerChoiceRepository;

    @Autowired
    private AIGradingService aiGradingService;  // ✨ Inject AI Grading Service

    // ===== Mapper =====
    private ExamAttemptResponse toResponse(ExamAttempt attempt) {
        ExamAttemptResponse resp = new ExamAttemptResponse();

        resp.setAttemptId(attempt.getAttemptId());

        resp.setExamId(attempt.getExam().getExamId());
        resp.setExamTitle(attempt.getExam().getTitle());

        resp.setUserId(attempt.getUser().getUserId());
        resp.setUserName(attempt.getUser().getUserName());

        resp.setStartTime(attempt.getStartTime());
        resp.setEndTime(attempt.getEndTime());

        resp.setStatus(attempt.getStatus().name());

        resp.setListeningScore(attempt.getListeningScore());
        resp.setReadingScore(attempt.getReadingScore());
        resp.setWritingScore(attempt.getWritingScore());
        resp.setTotalScore(attempt.getTotalScore());

        return resp;
    }

    // ===== Start exam =====
    @Override
    public ExamAttemptResponse startExam(ExamAttemptRequest request) {

        Exam exam = examRepository.findById(request.getExamId())
                .orElseThrow(() -> new IllegalArgumentException("Exam not found"));

        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        ExamAttempt attempt = new ExamAttempt();
        attempt.setExam(exam);
        attempt.setUser(user);
        attempt.setStartTime(LocalDateTime.now());
        attempt.setStatus(ExamAttemptStatus.IN_PROGRESS);

        attempt = examAttemptRepository.save(attempt);
        return toResponse(attempt);
    }

    @Override
    public ExamAttemptResponse getAttemptById(Long id) {
        ExamAttempt attempt = examAttemptRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Attempt not found"));
        return toResponse(attempt);
    }

    @Override
    public List<ExamAttemptResponse> getAttemptsByUser(Long userId) {
        return examAttemptRepository.findByUser_UserId(userId)
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    // ===== Submit exam =====
    @Override
    @Transactional
    public ExamAttemptResponse submitExam(Long attemptId) {

        ExamAttempt attempt = examAttemptRepository.findById(attemptId)
                .orElseThrow(() -> new IllegalArgumentException("Attempt not found"));

        if (attempt.getStatus() == ExamAttemptStatus.COMPLETED) {
            throw new IllegalStateException("Exam already submitted");
        }

        System.out.println("📝 Starting exam submission for attempt: " + attemptId);

        // Tính điểm (bao gồm AI grading cho WRITING)
        calculateAndSaveScores(attempt);

        attempt.setEndTime(LocalDateTime.now());
        attempt.setStatus(ExamAttemptStatus.COMPLETED);

        attempt = examAttemptRepository.save(attempt);

        System.out.println("✅ Exam submission completed! Total score: " + attempt.getTotalScore());
        return toResponse(attempt);
    }

    /**
     * Tính điểm và lưu vào database
     * ✨ Đã tích hợp AI Grading cho WRITING section
     */
    private void calculateAndSaveScores(ExamAttempt attempt) {
        List<UserAnswer> userAnswers = userAnswerRepository.findByAttempt_AttemptId(attempt.getAttemptId());

        BigDecimal listeningTotal = BigDecimal.ZERO;
        BigDecimal writingTotal = BigDecimal.ZERO;
        BigDecimal readingTotal = BigDecimal.ZERO;

        for (UserAnswer userAnswer : userAnswers) {
            Question question = userAnswer.getQuestion();
            String sectionType = question.getSection().getSectionType().name();

            BigDecimal score;

            // ✨ WRITING section: Dùng AI Grading
            if ("WRITING".equals(sectionType)) {
                score = calculateWritingScore(userAnswer, question);
            } else {
                // LISTENING, READING: Chấm tự động
                score = calculateQuestionScore(userAnswer, question);
            }

            // Lưu điểm vào user_answer
            userAnswer.setScore(score);
            userAnswerRepository.save(userAnswer);

            // Cộng điểm theo section
            if ("LISTENING".equals(sectionType)) {
                listeningTotal = listeningTotal.add(score);
            } else if ("WRITING".equals(sectionType)) {
                writingTotal = writingTotal.add(score);
            } else if ("READING".equals(sectionType)) {
                readingTotal = readingTotal.add(score);
            }
        }

        // Lưu điểm vào exam_attempt
        attempt.setListeningScore(listeningTotal);
        attempt.setWritingScore(writingTotal);
        attempt.setReadingScore(readingTotal);
        attempt.setTotalScore(listeningTotal.add(writingTotal).add(readingTotal));

        System.out.println("📊 Scores: Listening=" + listeningTotal +
                ", Writing=" + writingTotal +
                ", Reading=" + readingTotal);
    }

    // ==================== AI GRADING CHO WRITING ====================

    /**
     * ✨ Tính điểm WRITING bằng AI
     *
     * Logic:
     * - SHORT (Q51, Q52): Exact match trước → nếu không đúng → AI chấm partial credit
     * - ESSAY (Q53, Q54): AI đánh giá dựa trên đề bài
     */
    private BigDecimal calculateWritingScore(UserAnswer userAnswer, Question question) {
        QuestionType type = question.getQuestionType();

        if (type == QuestionType.SHORT) {
            return calculateShortAnswerWithAI(userAnswer, question);
        } else if (type == QuestionType.ESSAY) {
            return calculateEssayScoreWithAI(userAnswer, question);
        }

        return BigDecimal.ZERO;
    }

    /**
     * ✨ Chấm điểm SHORT (Q51, Q52) với AI
     *
     * Flow:
     * 1. Thử exact match trước (nhanh)
     * 2. Nếu không exact match → gọi AI chấm partial credit
     * 3. AI trả về 0-100 → quy đổi sang điểm thực (0-5)
     */
    private BigDecimal calculateShortAnswerWithAI(UserAnswer userAnswer, Question question) {
        String studentAnswer = userAnswer.getAnswerText();

        // Nếu không có câu trả lời
        if (studentAnswer == null || studentAnswer.trim().isEmpty()) {
            return BigDecimal.ZERO;
        }

        String correctAnswer = question.getCorrectAnswer();
        if (correctAnswer == null || correctAnswer.isEmpty()) {
            return BigDecimal.ZERO;
        }

        // 1. Thử exact match trước (giống logic cũ)
        String userText = studentAnswer.trim().toLowerCase();
        String[] possibleAnswers = correctAnswer.split("\\|");

        for (String possible : possibleAnswers) {
            String trimmed = possible.trim().toLowerCase();
            if (userText.equals(trimmed) ||
                    userText.replace(" ", "").equals(trimmed.replace(" ", ""))) {
                System.out.println("✅ Q" + question.getQuestionNumber() + " exact match → " + question.getPoints());
                return question.getPoints();
            }
        }

        // 2. Không exact match → gọi AI chấm partial credit
        System.out.println("🤖 Q" + question.getQuestionNumber() + " no exact match, calling AI...");

        try {
            WritingGradingRequest request = new WritingGradingRequest();
            request.setQuestionNumber(question.getQuestionNumber());
            request.setQuestionType("SHORT");
            request.setQuestionText(question.getPassageText());  // Đề bài có chỗ trống
            request.setReferenceAnswer(correctAnswer);           // Đáp án đúng
            request.setStudentAnswer(studentAnswer);
            request.setMinChars(1);
            request.setMaxChars(50);
            request.setMaxPoints(question.getPoints().intValue());

            WritingGradingResult result = aiGradingService.gradeWriting(request);

            // AI trả về 0-100 → quy đổi sang điểm thực
            int aiScore = result.getScore() != null ? result.getScore() : 0;
            BigDecimal finalScore = question.getPoints()
                    .multiply(BigDecimal.valueOf(aiScore))
                    .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);

            // ✨ LƯU KẾT QUẢ AI VÀO DATABASE
            userAnswer.setAiScore(aiScore);
            userAnswer.setAiFeedback(result.getFeedback());
            if (result.getBreakdown() != null) {
                userAnswer.setAiBreakdown(convertBreakdownToJson(result.getBreakdown()));
            }
            if (result.getSuggestions() != null && !result.getSuggestions().isEmpty()) {
                userAnswer.setAiSuggestions(convertSuggestionsToJson(result.getSuggestions()));
            }

            System.out.println("✅ Q" + question.getQuestionNumber() + " AI score: " + aiScore + "/100 → " + finalScore + "/" + question.getPoints());
            System.out.println("   💾 AI data saved to user_answer");
            return finalScore;

        } catch (Exception e) {
            System.err.println("❌ AI grading failed for Q" + question.getQuestionNumber() + ": " + e.getMessage());
            return BigDecimal.ZERO;
        }
    }

    /**
     * ✨ Chấm điểm ESSAY (Q53, Q54) với AI
     *
     * Flow:
     * 1. Gọi AI với đề bài + bài mẫu + bài viết học sinh
     * 2. AI đánh giá theo 4 tiêu chí (Content/Grammar/Vocabulary/Organization)
     * 3. AI trả về 0-100 → quy đổi sang điểm thực (0-30 hoặc 0-50)
     */
    private BigDecimal calculateEssayScoreWithAI(UserAnswer userAnswer, Question question) {
        String studentAnswer = userAnswer.getAnswerText();

        // Nếu không có câu trả lời
        if (studentAnswer == null || studentAnswer.trim().isEmpty()) {
            return BigDecimal.ZERO;
        }

        System.out.println("🤖 Grading ESSAY Q" + question.getQuestionNumber() + "...");

        try {
            // Xác định min/max chars dựa trên question_number
            int minChars = 200;
            int maxChars = 300;
            if (question.getQuestionNumber() == 54) {
                minChars = 600;
                maxChars = 700;
            }

            WritingGradingRequest request = new WritingGradingRequest();
            request.setQuestionNumber(question.getQuestionNumber());
            request.setQuestionType("ESSAY");
            request.setQuestionText(question.getPassageText());  // ĐỀ BÀI (quan trọng!)
            request.setReferenceAnswer(question.getCorrectAnswer());  // Bài mẫu (tham khảo)
            request.setStudentAnswer(studentAnswer);
            request.setMinChars(minChars);
            request.setMaxChars(maxChars);
            request.setMaxPoints(question.getPoints().intValue());

            WritingGradingResult result = aiGradingService.gradeWriting(request);

            // AI trả về 0-100 → quy đổi sang điểm thực
            int aiScore = result.getScore() != null ? result.getScore() : 0;
            BigDecimal finalScore = question.getPoints()
                    .multiply(BigDecimal.valueOf(aiScore))
                    .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);

            // ✨ LƯU KẾT QUẢ AI VÀO DATABASE
            userAnswer.setAiScore(aiScore);
            userAnswer.setAiFeedback(result.getFeedback());
            if (result.getBreakdown() != null) {
                userAnswer.setAiBreakdown(convertBreakdownToJson(result.getBreakdown()));
            }
            if (result.getSuggestions() != null && !result.getSuggestions().isEmpty()) {
                userAnswer.setAiSuggestions(convertSuggestionsToJson(result.getSuggestions()));
            }

            System.out.println("✅ Q" + question.getQuestionNumber() + " ESSAY AI score: " + aiScore + "/100 → " + finalScore + "/" + question.getPoints());
            System.out.println("   💾 AI data saved to user_answer");

            // Log chi tiết (nếu có)
            if (result.getFeedback() != null) {
                System.out.println("   Feedback: " + result.getFeedback().substring(0, Math.min(100, result.getFeedback().length())) + "...");
            }

            return finalScore;

        } catch (Exception e) {
            System.err.println("❌ AI grading failed for ESSAY Q" + question.getQuestionNumber() + ": " + e.getMessage());
            e.printStackTrace();
            return BigDecimal.ZERO;
        }
    }

    // ==================== LOGIC CŨ CHO MCQ ====================

    /**
     * Tính điểm cho 1 câu hỏi (MCQ only - dùng cho LISTENING/READING)
     */
    private BigDecimal calculateQuestionScore(UserAnswer userAnswer, Question question) {
        QuestionType type = question.getQuestionType();

        if (type == QuestionType.MCQ) {
            return calculateMCQScore(userAnswer, question);
        }

        return BigDecimal.ZERO;
    }

    /**
     * Tính điểm MCQ (Trắc nghiệm)
     */
    private BigDecimal calculateMCQScore(UserAnswer userAnswer, Question question) {
        if (userAnswer.getChoice() == null) {
            return BigDecimal.ZERO;
        }

        AnswerChoice selectedChoice = userAnswer.getChoice();

        if (selectedChoice.getIsCorrect() != null && selectedChoice.getIsCorrect()) {
            return question.getPoints();
        }

        return BigDecimal.ZERO;
    }

    // ==================== GET EXAM RESULT ====================

    /**
     * Lấy kết quả chi tiết (cho trang Result)
     */
    @Override
    @Transactional(readOnly = true)
    public ExamResultResponse getExamResult(Long attemptId) {
        ExamAttempt attempt = examAttemptRepository.findById(attemptId)
                .orElseThrow(() -> new IllegalArgumentException("Exam attempt not found"));

        if (attempt.getStatus() != ExamAttemptStatus.COMPLETED) {
            throw new IllegalStateException("Exam not yet completed");
        }

        List<UserAnswer> userAnswers = userAnswerRepository.findByAttempt_AttemptId(attemptId);

        Map<String, SectionResultResponse> sectionResults = calculateSectionResults(userAnswers);

        List<QuestionResultResponse> questionResults = prepareQuestionResults(userAnswers);

        int totalQuestions = questionResults.size();
        int correctAnswers = (int) questionResults.stream()
                .filter(QuestionResultResponse::isCorrect)
                .count();

        return ExamResultResponse.builder()
                .attemptId(attemptId)
                .totalScore(attempt.getTotalScore())
                .totalQuestions(totalQuestions)
                .correctAnswers(correctAnswers)
                .sectionResults(sectionResults)
                .questions(questionResults)
                .build();
    }

    /**
     * Tính kết quả từng section
     */
    private Map<String, SectionResultResponse> calculateSectionResults(List<UserAnswer> userAnswers) {
        Map<String, SectionResultResponse> sectionMap = new HashMap<>();

        for (UserAnswer answer : userAnswers) {
            Question question = answer.getQuestion();
            String sectionType = question.getSection().getSectionType().name();

            SectionResultResponse sectionResult = sectionMap.getOrDefault(
                    sectionType,
                    new SectionResultResponse(sectionType)
            );

            sectionResult.addQuestion(answer.getScore(), question.getPoints());
            sectionMap.put(sectionType, sectionResult);
        }

        return sectionMap;
    }

    /**
     * Tạo danh sách chi tiết câu hỏi
     * ✨ Đã thêm AI grading data cho WRITING
     */
    private List<QuestionResultResponse> prepareQuestionResults(List<UserAnswer> userAnswers) {
        return userAnswers.stream()
                .map(answer -> {
                    Question question = answer.getQuestion();

                    String userAnswerText = getUserAnswerText(answer);
                    String correctAnswerText = getCorrectAnswerText(question);

                    // ✨ Với WRITING: isCorrect = score > 0
                    boolean isCorrect = answer.getScore() != null && answer.getScore().compareTo(BigDecimal.ZERO) > 0;

                    QuestionResultResponse.QuestionResultResponseBuilder builder = QuestionResultResponse.builder()
                            .questionId(question.getQuestionId())
                            .questionNumber(question.getQuestionNumber())
                            .questionText(question.getQuestionText() != null ? question.getQuestionText() : question.getPassageText())
                            .questionType(question.getQuestionType().name())
                            .sectionType(question.getSection().getSectionType().name())
                            .userAnswer(userAnswerText)
                            .correctAnswer(correctAnswerText)
                            .isCorrect(isCorrect)
                            .score(answer.getScore() != null ? answer.getScore() : BigDecimal.ZERO)
                            .maxScore(question.getPoints());

                    // ✨ MAP AI GRADING DATA cho WRITING section
                    if ("WRITING".equals(question.getSection().getSectionType().name())) {
                        builder.aiScore(answer.getAiScore());
                        builder.aiFeedback(answer.getAiFeedback());

                        // Parse aiBreakdown từ JSON string
                        if (answer.getAiBreakdown() != null) {
                            builder.aiBreakdown(parseAiBreakdown(answer.getAiBreakdown()));
                        }

                        // Parse aiSuggestions từ JSON array string
                        if (answer.getAiSuggestions() != null && !answer.getAiSuggestions().isEmpty()) {
                            builder.aiSuggestions(parseAiSuggestions(answer.getAiSuggestions()));
                        }
                    }

                    return builder.build();
                })
                .sorted(Comparator.comparing(QuestionResultResponse::getQuestionNumber))
                .collect(Collectors.toList());
    }

    /**
     * ✨ Helper: Parse JSON string to AIScoreBreakdown
     */
    private QuestionResultResponse.AIScoreBreakdown parseAiBreakdown(String jsonString) {
        try {
            // Simple JSON parsing: {"content":25,"grammar":20,"vocabulary":15,"organization":20}
            QuestionResultResponse.AIScoreBreakdown breakdown = new QuestionResultResponse.AIScoreBreakdown();

            String clean = jsonString.replaceAll("[{}\"\\s]", "");
            String[] pairs = clean.split(",");

            for (String pair : pairs) {
                String[] kv = pair.split(":");
                if (kv.length == 2) {
                    String key = kv[0].toLowerCase();
                    Integer value = Integer.parseInt(kv[1]);

                    switch (key) {
                        case "content": breakdown.setContent(value); break;
                        case "grammar": breakdown.setGrammar(value); break;
                        case "vocabulary": breakdown.setVocabulary(value); break;
                        case "organization": breakdown.setOrganization(value); break;
                    }
                }
            }
            return breakdown;
        } catch (Exception e) {
            System.err.println("Failed to parse aiBreakdown: " + e.getMessage());
            return null;
        }
    }

    /**
     * ✨ Helper: Parse JSON array string to List<String>
     */
    private List<String> parseAiSuggestions(String jsonArrayString) {
        try {
            // Simple JSON array parsing: ["suggestion1", "suggestion2"]
            List<String> suggestions = new ArrayList<>();

            // Remove brackets and split by ","
            String clean = jsonArrayString.trim();
            if (clean.startsWith("[")) clean = clean.substring(1);
            if (clean.endsWith("]")) clean = clean.substring(0, clean.length() - 1);

            if (clean.isEmpty()) return suggestions;

            // Split by "," but handle escaped quotes
            StringBuilder current = new StringBuilder();
            boolean inQuote = false;
            boolean escaped = false;

            for (char c : clean.toCharArray()) {
                if (escaped) {
                    current.append(c);
                    escaped = false;
                } else if (c == '\\') {
                    escaped = true;
                } else if (c == '"') {
                    inQuote = !inQuote;
                } else if (c == ',' && !inQuote) {
                    String item = current.toString().trim();
                    if (!item.isEmpty()) suggestions.add(item);
                    current = new StringBuilder();
                } else {
                    current.append(c);
                }
            }

            // Add last item
            String lastItem = current.toString().trim();
            if (!lastItem.isEmpty()) suggestions.add(lastItem);

            return suggestions;
        } catch (Exception e) {
            System.err.println("Failed to parse aiSuggestions: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    /**
     * Lấy text câu trả lời của user
     */
    private String getUserAnswerText(UserAnswer answer) {
        // SHORT hoặc ESSAY
        if (answer.getAnswerText() != null && !answer.getAnswerText().isEmpty()) {
            return answer.getAnswerText();
        }

        // MCQ
        if (answer.getChoice() != null) {
            AnswerChoice choice = answer.getChoice();
            String choiceText = choice.getChoiceText();

            if (choiceText != null && choiceText.trim().startsWith("http")) {
                return choiceText.trim();
            }

            return choice.getChoiceLabel() + ". " + choiceText;
        }

        return "(Không trả lời)";
    }

    /**
     * ✨ Helper: Convert Breakdown object to JSON string
     */
    private String convertBreakdownToJson(WritingGradingResult.Breakdown breakdown) {
        try {
            StringBuilder sb = new StringBuilder("{");
            sb.append("\"content\":").append(breakdown.getContent() != null ? breakdown.getContent() : 0);
            sb.append(",\"grammar\":").append(breakdown.getGrammar() != null ? breakdown.getGrammar() : 0);
            sb.append(",\"vocabulary\":").append(breakdown.getVocabulary() != null ? breakdown.getVocabulary() : 0);
            sb.append(",\"organization\":").append(breakdown.getOrganization() != null ? breakdown.getOrganization() : 0);
            sb.append("}");
            return sb.toString();
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * ✨ Helper: Convert suggestions List to JSON array string
     */
    private String convertSuggestionsToJson(List<String> suggestions) {
        try {
            StringBuilder sb = new StringBuilder("[");
            boolean first = true;
            for (String suggestion : suggestions) {
                if (!first) sb.append(",");
                // Escape quotes and special characters in suggestion text
                String escaped = suggestion.replace("\\", "\\\\").replace("\"", "\\\"");
                sb.append("\"").append(escaped).append("\"");
                first = false;
            }
            sb.append("]");
            return sb.toString();
        } catch (Exception e) {
            return "[]";
        }
    }

    /**
     * Lấy đáp án đúng
     */
    private String getCorrectAnswerText(Question question) {
        QuestionType type = question.getQuestionType();

        if (type == QuestionType.SHORT || type == QuestionType.ESSAY) {
            return question.getCorrectAnswer() != null ? question.getCorrectAnswer() : "(Chưa có đáp án)";
        }

        if (type == QuestionType.MCQ) {
            List<AnswerChoice> choices = answerChoiceRepository.findByQuestionId(question.getQuestionId());
            return choices.stream()
                    .filter(c -> c.getIsCorrect() != null && c.getIsCorrect())
                    .findFirst()
                    .map(c -> {
                        String choiceText = c.getChoiceText();
                        if (choiceText != null && choiceText.trim().startsWith("http")) {
                            return choiceText.trim();
                        }
                        return c.getChoiceLabel() + ". " + choiceText;
                    })
                    .orElse("(Không có đáp án đúng)");
        }

        return "";
    }
}
