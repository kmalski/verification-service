package pl.kmalski.verification.domain.model;

import org.jspecify.annotations.NonNull;

import java.util.Locale;

import static java.util.Objects.requireNonNull;

public record Country(String value) {

    public Country {
        requireNonNull(value);

        value = value.trim().toUpperCase(Locale.ROOT);

        if (!value.matches("[A-Z]{2}")) {
            throw new IllegalArgumentException("Country must be a valid ISO 3166-1 alpha-2 code");
        }
    }

    @NonNull
    @Override
    public String toString() {
        return value;
    }

}
