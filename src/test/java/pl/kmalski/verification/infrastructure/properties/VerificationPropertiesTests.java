package pl.kmalski.verification.infrastructure.properties;

import org.junit.jupiter.api.Test;
import pl.kmalski.verification.domain.model.VerificationCheckType;

import java.time.Duration;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class VerificationPropertiesTests {

    @Test
    void shouldExposeConfiguredVerificationValues() {
        var properties = new VerificationProperties(new VerificationProperties.CheckProperties(
                Duration.ofSeconds(12),
                new VerificationProperties.VelocityCheckProperties(Duration.ofMinutes(5), 7),
                Set.of(VerificationCheckType.FRAUD, VerificationCheckType.VELOCITY)
        ));

        assertThat(properties.getVerificationCheckTimeout()).isEqualTo(Duration.ofSeconds(12));
        assertThat(properties.getVelocityWindow()).isEqualTo(Duration.ofMinutes(5));
        assertThat(properties.getVelocityLimit()).isEqualTo(7);
        assertThat(properties.getEnabledVerificationCheckTypes())
                .containsExactlyInAnyOrder(VerificationCheckType.FRAUD, VerificationCheckType.VELOCITY);
    }

    @Test
    void shouldReturnUnmodifiableEnabledCheckTypes() {
        var properties = new VerificationProperties(new VerificationProperties.CheckProperties(
                Duration.ofSeconds(12),
                new VerificationProperties.VelocityCheckProperties(Duration.ofMinutes(5), 7),
                Set.of(VerificationCheckType.FRAUD)
        ));

        var enabled = properties.getEnabledVerificationCheckTypes();

        assertThatThrownBy(() -> enabled.add(VerificationCheckType.VELOCITY))
                .isInstanceOf(UnsupportedOperationException.class);
    }

}
