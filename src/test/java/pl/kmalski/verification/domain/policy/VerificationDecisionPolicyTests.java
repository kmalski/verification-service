package pl.kmalski.verification.domain.policy;

import org.junit.jupiter.api.Test;
import pl.kmalski.verification.domain.model.VerificationCheckResult;
import pl.kmalski.verification.domain.model.VerificationCheckType;
import pl.kmalski.verification.domain.model.VerificationDecision;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class VerificationDecisionPolicyTests {

    private final VerificationDecisionPolicy policy = new VerificationDecisionPolicy();

    @Test
    void shouldRejectWhenAnyCheckFailed() {
        var results = List.of(
                VerificationCheckResult.passed(VerificationCheckType.FRAUD),
                VerificationCheckResult.failed(VerificationCheckType.SANCTIONS, "Match found")
        );

        var decision = policy.decide(results);

        assertThat(decision).isEqualTo(VerificationDecision.REJECTED);
    }

    @Test
    void shouldRequireReviewWhenAnyCheckRequiresReviewAndNoneFailed() {
        var results = List.of(
                VerificationCheckResult.passed(VerificationCheckType.FRAUD),
                VerificationCheckResult.requireReview(VerificationCheckType.VELOCITY, "Manual review")
        );

        var decision = policy.decide(results);

        assertThat(decision).isEqualTo(VerificationDecision.REQUIRES_REVIEW);
    }

    @Test
    void shouldApproveWhenAllChecksPassed() {
        var results = List.of(
                VerificationCheckResult.passed(VerificationCheckType.FRAUD),
                VerificationCheckResult.passed(VerificationCheckType.SANCTIONS)
        );

        var decision = policy.decide(results);

        assertThat(decision).isEqualTo(VerificationDecision.APPROVED);
    }

    @Test
    void shouldRejectNullResults() {
        assertThatThrownBy(() -> policy.decide(null))
                .isInstanceOf(NullPointerException.class);
    }
}
