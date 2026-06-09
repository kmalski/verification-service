package pl.kmalski.verification.domain.model;

import lombok.Builder;
import lombok.Getter;

import static java.util.Objects.requireNonNull;

@Getter
public class Verification {

    private final VerificationId id;

    @Builder
    private Verification(VerificationId id) {
        this.id = requireNonNull(id, "Verification id must not be null");
    }

    public static Verification create() {
        return Verification.builder()
                .id(VerificationId.random())
                .build();
    }

}
