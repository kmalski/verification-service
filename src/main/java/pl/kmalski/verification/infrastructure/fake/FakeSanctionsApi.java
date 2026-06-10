package pl.kmalski.verification.infrastructure.fake;

import org.springframework.stereotype.Component;
import pl.kmalski.verification.application.port.SanctionsApi;

@Component
public class FakeSanctionsApi implements SanctionsApi {

    @Override
    public boolean isCountrySanctioned(String countryCode) {
        return false;
    }

}
