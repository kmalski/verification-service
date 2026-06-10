package pl.kmalski.verification.domain.model;

import static java.util.Objects.requireNonNull;

public record VerificationCheckResult(VerificationCheckType type,
                                      VerificationCheckStatus status,
                                      String reason) {

    public VerificationCheckResult {
        requireNonNull(type);
        requireNonNull(status);
        requireNonNull(reason);

        if (reason.isBlank()) {
            throw new IllegalArgumentException("Reason cannot be empty");
        }
    }

    public static VerificationCheckResult passed(VerificationCheckType type) {
        return new VerificationCheckResult(type, VerificationCheckStatus.PASSED, "Passed");
    }

    public static VerificationCheckResult failed(VerificationCheckType type, String reason) {
        return new VerificationCheckResult(type, VerificationCheckStatus.FAILED, reason);
    }

    public static VerificationCheckResult requireReview(VerificationCheckType type, String reason) {
        return new VerificationCheckResult(type, VerificationCheckStatus.REQUIRES_REVIEW, reason);
    }

}
