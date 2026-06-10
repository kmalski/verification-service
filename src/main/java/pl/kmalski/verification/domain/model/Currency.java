package pl.kmalski.verification.domain.model;

import org.jspecify.annotations.NonNull;
import pl.kmalski.verification.domain.exception.InvalidVerificationException;

import java.util.Locale;

import static pl.kmalski.verification.domain.validation.VerificationValidator.requireNonNull;

public record Currency(String value) {

    public Currency {
        requireNonNull(value, "Currency");

        value = value.trim().toUpperCase(Locale.ROOT);

        if (!value.matches("[A-Z]{3}")) {
            throw new InvalidVerificationException("Currency must be a valid ISO 4217 code");
        }
    }

    @NonNull
    @Override
    public String toString() {
        return value;
    }

}
