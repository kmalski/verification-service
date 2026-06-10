package pl.kmalski.verification.domain.model;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class VerificationTests {

    @Test
    void shouldStartVerificationInQueuedStateWithEmptyResults() {
        var verification = Verification.start(validPaymentData());

        assertThat(verification.getId()).isNotNull();
        assertThat(verification.getPayment()).isEqualTo(validPaymentData());
        assertThat(verification.getStatus()).isEqualTo(VerificationStatus.QUEUED);
        assertThat(verification.getDecision()).isNull();
        assertThat(verification.getFailureReason()).isNull();
        assertThat(verification.getCheckResults()).isEmpty();
    }

    @Test
    void shouldMarkQueuedVerificationAsInProgress() {
        var verification = Verification.start(validPaymentData());

        verification.markInProgress();

        assertThat(verification.getStatus()).isEqualTo(VerificationStatus.IN_PROGRESS);
    }

    @Test
    void shouldAllowTransitionFromFailedToInProgress() {
        var verification = Verification.start(validPaymentData());
        verification.markInProgress();
        verification.markFailed("Temporary issue");

        verification.markInProgress();

        assertThat(verification.getStatus()).isEqualTo(VerificationStatus.IN_PROGRESS);
        assertThat(verification.getFailureReason()).isEqualTo("Temporary issue");
    }

    @Test
    void shouldRejectInvalidTransitionToInProgress() {
        var verification = Verification.start(validPaymentData());
        verification.markInProgress();
        verification.complete(VerificationDecision.APPROVED, List.of());

        assertThatThrownBy(verification::markInProgress)
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("Verification cannot transition from COMPLETED to IN_PROGRESS");
    }

    @Test
    void shouldCompleteVerificationAndAppendCheckResults() {
        var verification = Verification.start(validPaymentData());
        var firstResult = VerificationCheckResult.passed(VerificationCheckType.FRAUD);
        var secondResult = VerificationCheckResult.requireReview(VerificationCheckType.VELOCITY, "Manual review");

        verification.markInProgress();
        verification.complete(VerificationDecision.REQUIRES_REVIEW, List.of(firstResult, secondResult));

        assertThat(verification.getStatus()).isEqualTo(VerificationStatus.COMPLETED);
        assertThat(verification.getDecision()).isEqualTo(VerificationDecision.REQUIRES_REVIEW);
        assertThat(verification.getCheckResults()).containsExactly(firstResult, secondResult);
    }

    @Test
    void shouldRejectCompletionWhenNotInProgress() {
        var verification = Verification.start(validPaymentData());

        assertThatThrownBy(() -> verification.complete(VerificationDecision.APPROVED, List.of()))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("Verification cannot transition from QUEUED to COMPLETED");
    }

    @Test
    void shouldMarkVerificationAsFailed() {
        var verification = Verification.start(validPaymentData());

        verification.markInProgress();
        verification.markFailed("Provider timeout");

        assertThat(verification.getStatus()).isEqualTo(VerificationStatus.FAILED);
        assertThat(verification.getFailureReason()).isEqualTo("Provider timeout");
    }

    @Test
    void shouldRejectFailureWhenNotInProgress() {
        var verification = Verification.start(validPaymentData());

        assertThatThrownBy(() -> verification.markFailed("Provider timeout"))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("Verification cannot transition from QUEUED to FAILED");
    }

    @Test
    void shouldReturnDefensiveCopyOfCheckResults() {
        var verification = Verification.start(validPaymentData());
        var result = VerificationCheckResult.passed(VerificationCheckType.FRAUD);

        verification.markInProgress();
        verification.complete(VerificationDecision.APPROVED, List.of(result));

        var checkResults = verification.getCheckResults();

        assertThatThrownBy(() -> checkResults.add(result))
                .isInstanceOf(UnsupportedOperationException.class);
        assertThat(verification.getCheckResults()).containsExactly(result);
    }

    private static PaymentData validPaymentData() {
        return new PaymentData(
                "payment-1",
                "customer-1",
                new BigDecimal("10.00"),
                "PLN",
                "PL"
        );
    }
}
