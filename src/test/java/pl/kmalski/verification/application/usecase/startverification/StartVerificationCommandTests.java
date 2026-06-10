package pl.kmalski.verification.application.usecase.startverification;

import org.junit.jupiter.api.Test;
import pl.kmalski.verification.domain.exception.InvalidVerificationException;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class StartVerificationCommandTests {

    @Test
    void shouldRejectNullPayment() {
        assertThatThrownBy(() -> new StartVerificationCommand(null))
                .isInstanceOf(InvalidVerificationException.class)
                .hasMessage("Payment cannot be null");
    }
}
