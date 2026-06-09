package pl.kmalski.verification.infrastructure.properties;

import jakarta.validation.Valid;
import lombok.NonNull;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;
import pl.kmalski.verification.application.port.VerificationConfiguration;

import java.time.Duration;

@Validated
@ConfigurationProperties(prefix = "verification")
public record VerificationProperties(@Valid @NonNull CheckProperties check) implements VerificationConfiguration {

    @Override
    public Duration getVerificationCheckTimeout() {
        return check.timeout();
    }

    record CheckProperties(@NonNull Duration timeout) {}

}
