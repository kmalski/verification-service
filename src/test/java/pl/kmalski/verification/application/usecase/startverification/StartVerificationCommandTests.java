package pl.kmalski.verification.application.usecase.startverification;

import org.junit.jupiter.api.Test;
import pl.kmalski.verification.domain.model.PaymentData;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class StartVerificationCommandTests {

    @Test
    void shouldRejectNullPayment() {
        assertThatThrownBy(() -> new StartVerificationCommand(null))
                .isInstanceOf(NullPointerException.class);
    }
}
