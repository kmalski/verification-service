package pl.kmalski.verification.domain.model;

import org.jspecify.annotations.NonNull;

import static java.util.Objects.requireNonNull;

public record Country(String value) {

    public Country {
        requireNonNull(value);

        if (value.isBlank()) {
            throw new IllegalArgumentException("Country cannot be blank");
        }
    }

    @NonNull
    @Override
    public String toString() {
        return value;
    }

}
