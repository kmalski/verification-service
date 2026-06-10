package pl.kmalski.verification.application.usecase.startverification;

import org.junit.jupiter.api.Test;
import pl.kmalski.verification.domain.model.VerificationId;
import pl.kmalski.verification.domain.model.VerificationStatus;

import static org.assertj.core.api.Assertions.assertThat;

class StartVerificationResultTests {

    @Test
    void shouldExposeVerificationIdAndStatus() {
        var verificationId = VerificationId.random();

        var result = new StartVerificationResult(verificationId, VerificationStatus.QUEUED);

        assertThat(result.verificationId()).isEqualTo(verificationId);
        assertThat(result.status()).isEqualTo(VerificationStatus.QUEUED);
    }
}
