package pl.kmalski.verification.infrastructure.fake;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.kmalski.verification.domain.model.Country;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.then;

@ExtendWith(MockitoExtension.class)
class FakeSanctionsApiTests {

    @Mock
    private LatencySimulator latencySimulator;

    @InjectMocks
    private FakeSanctionsApi sanctionsApi;

    @Test
    void shouldReturnTrueForSanctionedCountries() {
        assertThat(sanctionsApi.isCountrySanctioned(new Country("ru"))).isTrue();
        then(latencySimulator).should().simulateLatency();
    }

    @Test
    void shouldReturnFalseForNonSanctionedCountries() {
        assertThat(sanctionsApi.isCountrySanctioned(new Country("PL"))).isFalse();
        then(latencySimulator).should().simulateLatency();
    }

}
