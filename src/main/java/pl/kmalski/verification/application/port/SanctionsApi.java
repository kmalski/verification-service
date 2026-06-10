package pl.kmalski.verification.application.port;

public interface SanctionsApi {

    boolean isCountrySanctioned(String countryCode);

}
