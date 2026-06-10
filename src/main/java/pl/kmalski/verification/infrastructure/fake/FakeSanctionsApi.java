package pl.kmalski.verification.infrastructure.fake;

import org.springframework.stereotype.Component;
import pl.kmalski.verification.application.port.SanctionsApi;

import java.time.Duration;

@Component
public class FakeSanctionsApi implements SanctionsApi {

    @Override
    public boolean isCountrySanctioned(String countryCode) {
        try {
            Thread.sleep(Duration.ofSeconds(1));
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return false;
    }

}
