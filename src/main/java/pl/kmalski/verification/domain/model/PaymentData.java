package pl.kmalski.verification.domain.model;

import static java.util.Objects.requireNonNull;

public record PaymentData(PaymentId paymentId,
                          CustomerId customerId,
                          Amount amount,
                          Currency currency,
                          Country country) {

    public PaymentData {
        requireNonNull(paymentId);
        requireNonNull(customerId);
        requireNonNull(amount);
        requireNonNull(currency);
        requireNonNull(country);
    }

}
