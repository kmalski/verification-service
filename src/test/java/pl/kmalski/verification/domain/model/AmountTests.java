package pl.kmalski.verification.domain.model;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class AmountTests {

    @Test
    void shouldExposeAmountValue() {
        var amount = new Amount(new BigDecimal("12.50"));

        assertThat(amount.value()).isEqualTo("12.50");
    }

    @Test
    void shouldRejectNonPositiveAmount() {
        assertThatThrownBy(() -> new Amount(BigDecimal.ZERO))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Amount must be positive");
    }
}
