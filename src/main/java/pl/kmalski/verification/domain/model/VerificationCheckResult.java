package pl.kmalski.verification.domain.model;

import pl.kmalski.verification.domain.exception.InvalidVerificationException;

import static pl.kmalski.verification.domain.validation.VerificationValidator.requireNonNull;

public record VerificationCheckResult(VerificationCheckType type,
                                      VerificationCheckStatus status,
                                      String reason) {

    public VerificationCheckResult {
        requireNonNull(type, "Type");
        requireNonNull(status, "Status");
        requireNonNull(reason, "Reason");

        if (reason.isBlank()) {
            throw new InvalidVerificationException("Reason cannot be empty");
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
