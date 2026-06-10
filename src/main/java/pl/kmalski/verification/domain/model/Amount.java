package pl.kmalski.verification.domain.model;

import org.jspecify.annotations.NonNull;

import java.math.BigDecimal;

import static java.util.Objects.requireNonNull;

public record Amount(BigDecimal value) {

    public Amount {
        requireNonNull(value);

        if (value.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Amount must be positive");
        }
    }

    @NonNull
    @Override
    public String toString() {
        return value.toString();
    }

}
