package pl.kmalski.verification.infrastructure.fake;

import org.springframework.stereotype.Component;
import pl.kmalski.verification.application.port.SanctionsApi;
import pl.kmalski.verification.domain.model.Country;

import java.util.Locale;
import java.util.Set;

import static pl.kmalski.verification.infrastructure.fake.LatencySimulator.simulateLatency;

@Component
public class FakeSanctionsApi implements SanctionsApi {

    private static final Set<String> SANCTIONED_COUNTRIES = Set.of("RU", "KP", "IR", "SY");

    @Override
    public boolean isCountrySanctioned(Country country) {
        simulateLatency();

        return SANCTIONED_COUNTRIES.contains(country.value().toUpperCase(Locale.ROOT));
    }

}
