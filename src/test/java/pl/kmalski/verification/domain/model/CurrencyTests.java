package pl.kmalski.verification.domain.model;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class CurrencyTests {

    @Test
    void shouldExposeValue() {
        var currency = new Currency("pln");

        assertThat(currency.value()).isEqualTo("PLN");
        assertThat(currency.toString()).isEqualTo("PLN");
    }

    @Test
    void shouldRejectInvalidValue() {
        assertThatThrownBy(() -> new Currency("12"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Currency must be a valid ISO 4217 code");
    }

    @Test
    void shouldRejectUnknownFormat() {
        assertThatThrownBy(() -> new Currency("PL"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Currency must be a valid ISO 4217 code");
    }
}
