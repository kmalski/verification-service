package pl.kmalski.verification.infrastructure.properties;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;
import pl.kmalski.verification.application.port.VerificationConfiguration;
import pl.kmalski.verification.domain.model.VerificationCheckType;

import java.time.Duration;
import java.util.Set;

@Validated
@ConfigurationProperties(prefix = "verification")
record VerificationProperties(@Valid @NotNull CheckProperties check) implements VerificationConfiguration {

    @Override
    public Duration getVerificationCheckTimeout() {
        return check.timeout();
    }

    @Override
    public Duration getVelocityWindow() {
        return check.velocity().window();
    }

    @Override
    public int getVelocityLimit() {
        return check.velocity().limit();
    }

    @Override
    public Set<VerificationCheckType> getEnabledVerificationCheckTypes() {
        return Set.copyOf(check.enabled());
    }

    record CheckProperties(@NotNull Duration timeout,
                           @NotNull VelocityCheckProperties velocity,
                           @NotNull Set<VerificationCheckType> enabled) {}

    record VelocityCheckProperties(@NotNull Duration window,
                                   @Positive int limit) {}

}
