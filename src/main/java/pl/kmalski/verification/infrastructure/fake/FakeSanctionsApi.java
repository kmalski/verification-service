package pl.kmalski.verification.infrastructure.fake;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pl.kmalski.verification.application.port.SanctionsApi;
import pl.kmalski.verification.domain.model.Country;

import java.util.Locale;
import java.util.Set;

@Component
@RequiredArgsConstructor
class FakeSanctionsApi implements SanctionsApi {

    private static final Set<String> SANCTIONED_COUNTRIES = Set.of("RU", "KP", "IR", "SY");

    private final LatencySimulator latencySimulator;

    @Override
    public boolean isCountrySanctioned(Country country) {
        latencySimulator.simulateLatency();

        return SANCTIONED_COUNTRIES.contains(country.value().toUpperCase(Locale.ROOT));
    }

}
