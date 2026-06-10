package pl.kmalski.verification.infrastructure.web;

import java.math.BigDecimal;

record StartVerificationRequest(Payment payment) {

    record Payment(String paymentId,
                   String customerId,
                   BigDecimal amount,
                   String currency,
                   String country) {}

}
