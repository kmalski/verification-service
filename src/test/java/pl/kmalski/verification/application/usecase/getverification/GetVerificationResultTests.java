package pl.kmalski.verification.application.usecase.getverification;

import org.junit.jupiter.api.Test;
import pl.kmalski.verification.application.usecase.getverification.GetVerificationResult.VerificationCheckResult;
import pl.kmalski.verification.domain.model.*;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class GetVerificationResultTests {

    @Test
    void shouldExposeVerificationDetails() {
        var verificationId = VerificationId.random();
        var checkResult = new VerificationCheckResult(
                VerificationCheckType.FRAUD,
                VerificationCheckStatus.PASSED,
                "Passed"
        );

        var result = new GetVerificationResult(
                verificationId,
                VerificationStatus.COMPLETED,
                VerificationDecision.APPROVED,
                List.of(checkResult)
        );

        assertThat(result.verificationId()).isEqualTo(verificationId);
        assertThat(result.status()).isEqualTo(VerificationStatus.COMPLETED);
        assertThat(result.decision()).isEqualTo(VerificationDecision.APPROVED);
        assertThat(result.checkResults()).containsExactly(checkResult);
    }
}
