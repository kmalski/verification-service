package pl.kmalski.verification.domain.model;

import org.jspecify.annotations.NonNull;

import static pl.kmalski.verification.domain.validation.VerificationValidator.requireNonNull;

public record CustomerId(String value) {

    public CustomerId {
        requireNonNull(value, "Customer id");
    }

    @NonNull
    @Override
    public String toString() {
        return value;
    }

}
