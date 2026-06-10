package pl.kmalski.verification.application.port;

import pl.kmalski.verification.domain.model.Country;

public interface SanctionsApi {

    boolean isCountrySanctioned(Country country);

}
