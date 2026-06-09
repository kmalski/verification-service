package pl.kmalski.verification.domain.model;

import org.jspecify.annotations.NonNull;

import java.util.UUID;

import static java.util.Objects.requireNonNull;

public record VerificationId(UUID id) {

    public VerificationId {
        requireNonNull(id, "Verification id must not be null");
    }

    public static VerificationId random() {
        return new VerificationId(UUID.randomUUID());
    }

    @NonNull
    @Override
    public String toString() {
        return id.toString();
    }

}
