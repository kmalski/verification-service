package pl.kmalski.verification.domain.model;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class CountryTests {

    @Test
    void shouldExposeValue() {
        var country = new Country("PL");

        assertThat(country.value()).isEqualTo("PL");
        assertThat(country.toString()).isEqualTo("PL");
    }

    @Test
    void shouldRejectBlankValue() {
        assertThatThrownBy(() -> new Country(" "))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Country cannot be blank");
    }
}
