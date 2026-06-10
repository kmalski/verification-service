package pl.kmalski.verification.domain.model;

import org.jspecify.annotations.NonNull;

import java.util.Locale;

import static java.util.Objects.requireNonNull;

public record Currency(String value) {

    public Currency {
        requireNonNull(value);

        value = value.trim().toUpperCase(Locale.ROOT);

        if (!value.matches("[A-Z]{3}")) {
            throw new IllegalArgumentException("Currency must be a valid ISO 4217 code");
        }
    }

    @NonNull
    @Override
    public String toString() {
        return value;
    }

}
