package pl.kmalski.verification.application.usecase.getverification;

import org.junit.jupiter.api.Test;
import pl.kmalski.verification.domain.exception.InvalidVerificationException;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class GetVerificationQueryTests {

    @Test
    void shouldRejectNullVerificationId() {
        assertThatThrownBy(() -> new GetVerificationQuery(null))
                .isInstanceOf(InvalidVerificationException.class)
                .hasMessage("Verification id cannot be null");
    }
}
