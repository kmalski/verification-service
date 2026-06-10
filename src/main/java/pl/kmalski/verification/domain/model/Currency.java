package pl.kmalski.verification.domain.model;

import org.jspecify.annotations.NonNull;

import static java.util.Objects.requireNonNull;

public record Currency(String value) {

    public Currency {
        requireNonNull(value);

        if (value.isBlank()) {
            throw new IllegalArgumentException("Currency cannot be blank");
        }
    }

    @NonNull
    @Override
    public String toString() {
        return value;
    }

}
