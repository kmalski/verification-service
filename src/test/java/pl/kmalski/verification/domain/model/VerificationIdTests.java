package pl.kmalski.verification.domain.model;

import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class VerificationIdTests {

    @Test
    void shouldRenderUuidAsString() {
        var uuid = UUID.fromString("123e4567-e89b-12d3-a456-426614174000");

        var verificationId = new VerificationId(uuid);

        assertThat(verificationId.toString()).isEqualTo(uuid.toString());
    }

    @Test
    void shouldRejectNullUuid() {
        assertThatThrownBy(() -> new VerificationId(null))
                .isInstanceOf(NullPointerException.class);
    }
}
