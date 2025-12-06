package org.example.ktigerstudybe;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class KTigerStudyBEApplication {
    public static void main(String[] args) {
        SpringApplication.run(KTigerStudyBEApplication.class, args);
    }
}
