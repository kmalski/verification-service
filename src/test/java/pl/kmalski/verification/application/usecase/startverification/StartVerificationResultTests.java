package pl.kmalski.verification.application.usecase.startverification;

import org.junit.jupiter.api.Test;
import pl.kmalski.verification.domain.model.VerificationStatus;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class StartVerificationResultTests {

    @Test
    void shouldExposeVerificationIdAndStatus() {
        var verificationId = UUID.fromString("123e4567-e89b-12d3-a456-426614174000");

        var result = new StartVerificationResult(verificationId, VerificationStatus.QUEUED);

        assertThat(result.verificationId()).isEqualTo(verificationId);
        assertThat(result.status()).isEqualTo(VerificationStatus.QUEUED);
    }
}
