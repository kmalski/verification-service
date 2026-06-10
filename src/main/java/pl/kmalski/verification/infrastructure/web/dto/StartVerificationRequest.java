package pl.kmalski.verification.infrastructure.web.dto;

import java.math.BigDecimal;

public record StartVerificationRequest(Payment payment) {

    public record Payment(String paymentId,
                          String customerId,
                          BigDecimal amount,
                          String currency,
                          String country) {}

}
