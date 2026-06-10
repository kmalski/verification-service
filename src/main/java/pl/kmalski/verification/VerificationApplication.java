package pl.kmalski.verification;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync
@SpringBootApplication
public class VerificationApplication {

    void main(String[] args) {
        SpringApplication.run(VerificationApplication.class, args);
    }

}
