package pl.kmalski.verification.domain.model;

import org.jspecify.annotations.NonNull;

import java.util.UUID;

import static pl.kmalski.verification.domain.validation.VerificationValidator.requireNonNull;

public record VerificationId(UUID value) {

    public VerificationId {
        requireNonNull(value, "Verification id");
    }

    public static VerificationId random() {
        return new VerificationId(UUID.randomUUID());
    }

    @NonNull
    @Override
    public String toString() {
        return value.toString();
    }

}
