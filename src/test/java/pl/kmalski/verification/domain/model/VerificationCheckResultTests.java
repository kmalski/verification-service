package pl.kmalski.verification.domain.model;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class VerificationCheckResultTests {

    @Test
    void shouldCreatePassedResult() {
        var result = VerificationCheckResult.passed(VerificationCheckType.FRAUD);

        assertThat(result.type()).isEqualTo(VerificationCheckType.FRAUD);
        assertThat(result.status()).isEqualTo(VerificationCheckStatus.PASSED);
        assertThat(result.reason()).isEqualTo("Passed");
    }

    @Test
    void shouldCreateFailedResult() {
        var result = VerificationCheckResult.failed(VerificationCheckType.SANCTIONS, "Match found");

        assertThat(result.type()).isEqualTo(VerificationCheckType.SANCTIONS);
        assertThat(result.status()).isEqualTo(VerificationCheckStatus.FAILED);
        assertThat(result.reason()).isEqualTo("Match found");
    }

    @Test
    void shouldCreateReviewResult() {
        var result = VerificationCheckResult.requireReview(VerificationCheckType.VELOCITY, "Manual review required");

        assertThat(result.type()).isEqualTo(VerificationCheckType.VELOCITY);
        assertThat(result.status()).isEqualTo(VerificationCheckStatus.REQUIRES_REVIEW);
        assertThat(result.reason()).isEqualTo("Manual review required");
    }

    @Test
    void shouldRejectBlankReason() {
        assertThatThrownBy(() -> new VerificationCheckResult(
                VerificationCheckType.FRAUD,
                VerificationCheckStatus.PASSED,
                " "
        ))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Reason cannot be empty");
    }
}
