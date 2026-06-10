package pl.kmalski.verification.domain.model;

import org.jspecify.annotations.NonNull;

import java.util.random.RandomGenerator;

import static java.util.Objects.requireNonNull;

public record PaymentId(String value) {

    public PaymentId {
        requireNonNull(value);
    }

    @NonNull
    @Override
    public String toString() {
        return value;
    }

}
