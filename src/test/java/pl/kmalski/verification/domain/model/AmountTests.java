package pl.kmalski.verification.domain.model;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class AmountTests {

    @Test
    void shouldRejectNullAmount() {
        assertThatThrownBy(() -> new Amount(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Amount cannot be null");
    }

    @Test
    void shouldRejectNonPositiveAmount() {
        assertThatThrownBy(() -> new Amount(BigDecimal.ZERO))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Amount must be positive");
    }
}
