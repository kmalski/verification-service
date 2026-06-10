package pl.kmalski.verification.domain.model;

import static pl.kmalski.verification.domain.validation.VerificationValidator.requireNonNull;

public record PaymentData(PaymentId paymentId,
                          CustomerId customerId,
                          Amount amount,
                          Currency currency,
                          Country country) {

    public PaymentData {
        requireNonNull(paymentId, "Payment id");
        requireNonNull(customerId, "Customer id");
        requireNonNull(amount, "Amount");
        requireNonNull(currency, "Currency");
        requireNonNull(country, "Country");
    }

}
