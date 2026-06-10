package pl.kmalski.verification.domain.model;

import org.jspecify.annotations.NonNull;
import pl.kmalski.verification.domain.exception.InvalidVerificationException;

import java.util.Locale;

import static pl.kmalski.verification.domain.validation.VerificationValidator.requireNonNull;

public record Country(String value) {

    public Country {
        requireNonNull(value, "Country");

        value = value.trim().toUpperCase(Locale.ROOT);

        if (!value.matches("[A-Z]{2}")) {
            throw new InvalidVerificationException("Country must be a valid ISO 3166-1 alpha-2 code");
        }
    }

    @NonNull
    @Override
    public String toString() {
        return value;
    }

}
