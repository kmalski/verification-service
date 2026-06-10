package pl.kmalski.verification.domain.model;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class CountryTests {

    @Test
    void shouldRejectNullCountry() {
        assertThatThrownBy(() -> new Country(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Country cannot be null");
    }

    @Test
    void shouldRejectInvalidValue() {
        assertThatThrownBy(() -> new Country("123"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Country must be a valid ISO 3166-1 alpha-2 code");
    }

    @Test
    void shouldRejectUnknownFormat() {
        assertThatThrownBy(() -> new Country("POL"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Country must be a valid ISO 3166-1 alpha-2 code");
    }
}
