package pl.kmalski.verification.domain.model;

import org.jspecify.annotations.NonNull;

import static pl.kmalski.verification.domain.validation.VerificationValidator.requireNonNull;

public record PaymentId(String value) {

    public PaymentId {
        requireNonNull(value, "Payment id");
    }

    @NonNull
    @Override
    public String toString() {
        return value;
    }

}
