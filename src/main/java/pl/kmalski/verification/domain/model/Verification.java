package pl.kmalski.verification.domain.model;

import lombok.Builder;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

import static pl.kmalski.verification.domain.validation.VerificationValidator.requireNonNull;

@Getter
public class Verification {

    private final VerificationId id;
    private final PaymentData payment;

    private VerificationStatus status;
    private VerificationDecision decision;
    private String failureReason;

    private final List<VerificationCheckResult> checkResults;

    @Builder
    private Verification(VerificationId id,
                         PaymentData payment,
                         VerificationStatus status,
                         VerificationDecision decision,
                         List<VerificationCheckResult> checkResults) {
        this.id = requireNonNull(id, "Verification id");
        this.payment = requireNonNull(payment, "Payment");
        this.status = requireNonNull(status, "Status");
        this.decision = decision;
        this.checkResults = requireNonNull(checkResults, "Check results");
    }

    public static Verification start(PaymentData payment) {
        return Verification.builder()
                .id(VerificationId.random())
                .payment(payment)
                .status(VerificationStatus.QUEUED)
                .checkResults(new ArrayList<>())
                .build();
    }

    public void markInProgress() {
        if (status != VerificationStatus.QUEUED && status != VerificationStatus.FAILED) {
            throw cannotTransitionTo(VerificationStatus.IN_PROGRESS);
        }

        this.status = VerificationStatus.IN_PROGRESS;
    }

    public void complete(VerificationDecision decision, List<VerificationCheckResult> checkResults) {
        requireNonNull(decision, "Decision");
        requireNonNull(checkResults, "Check results");

        if (status != VerificationStatus.IN_PROGRESS) {
            throw cannotTransitionTo(VerificationStatus.COMPLETED);
        }

        this.decision = decision;
        this.status = VerificationStatus.COMPLETED;
        this.checkResults.addAll(checkResults);
    }

    public void markFailed(String reason) {
        requireNonNull(reason, "Reason");

        if (status != VerificationStatus.IN_PROGRESS) {
            throw cannotTransitionTo(VerificationStatus.FAILED);
        }

        this.status = VerificationStatus.FAILED;
        this.failureReason = reason;
    }

    public List<VerificationCheckResult> getCheckResults() {
        return List.copyOf(checkResults);
    }

    private IllegalStateException cannotTransitionTo(VerificationStatus to) {
        return new IllegalStateException("Verification cannot transition from %s to %s".formatted(status, to));
    }

}
