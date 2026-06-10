package pl.kmalski.verification.domain.model;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class CurrencyTests {

    @Test
    void shouldExposeValue() {
        var currency = new Currency("PLN");

        assertThat(currency.value()).isEqualTo("PLN");
        assertThat(currency.toString()).isEqualTo("PLN");
    }

    @Test
    void shouldRejectBlankValue() {
        assertThatThrownBy(() -> new Currency(" "))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Currency cannot be blank");
    }
}
