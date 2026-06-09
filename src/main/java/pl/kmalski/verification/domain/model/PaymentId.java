package pl.kmalski.verification.domain.model;

import org.jspecify.annotations.NonNull;

import java.util.UUID;

import static java.util.Objects.requireNonNull;

public record PaymentId(UUID id) {

    public PaymentId {
        requireNonNull(id, "Payment id must not be null");
    }

    @NonNull
    @Override
    public String toString() {
        return id.toString();
    }

}
