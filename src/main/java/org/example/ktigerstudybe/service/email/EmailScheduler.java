package org.example.ktigerstudybe.service.email;

import org.example.ktigerstudybe.model.User;
import org.example.ktigerstudybe.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class EmailScheduler {

    @Autowired
    private EmailService emailService;

    @Autowired
    private UserRepository userRepository;

    @Scheduled(cron = "0 45 9 * * *") // 8:00 AM mỗi ngày
    public void sendReminderEmail() {
        List<User> users = userRepository.findAllActiveUsers();

        for (User user : users) {
            try {
                emailService.sendSimpleEmail(
                        user.getEmail(),
                        "⏰ Nhắc học tiếng Hàn hôm nay",
                        "Chào " + user.getFullName() + ", đừng quên luyện tập tiếng Hàn hôm nay nhé!"
                );
                System.out.println("✅ Sent to: " + user.getEmail());
            } catch (Exception e) {
                System.out.println("❌ Failed: " + user.getEmail());
            }
        }
    }
}