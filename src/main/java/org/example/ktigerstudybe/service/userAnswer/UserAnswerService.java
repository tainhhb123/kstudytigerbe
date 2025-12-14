package org.example.ktigerstudybe.service.userAnswer;

import org.example.ktigerstudybe.dto.req.UserAnswerRequest;
import org.example.ktigerstudybe.dto.resp.UserAnswerResponse;

import java.util.List;

public interface UserAnswerService {

    UserAnswerResponse saveUserAnswer(UserAnswerRequest request);

    List<UserAnswerResponse> getAnswersByAttempt(Long attemptId);
}
