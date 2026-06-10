package pl.kmalski.verification.domain.model;

import java.math.BigDecimal;

import static java.util.Objects.requireNonNull;

public record PaymentData(String paymentId,
                          String customerId,
                          BigDecimal amount,
                          String currency,
                          String country) {

    public PaymentData {
        requireNonNull(paymentId);
        requireNonNull(customerId);
        requireNonNull(amount);
        requireNonNull(currency);
        requireNonNull(country);

        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Payment amount must be positive");
        }
    }

}
