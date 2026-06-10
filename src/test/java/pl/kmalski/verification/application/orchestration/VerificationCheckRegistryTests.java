package pl.kmalski.verification.application.orchestration;

import org.junit.jupiter.api.Test;
import pl.kmalski.verification.application.port.VerificationConfiguration;
import pl.kmalski.verification.application.strategy.check.VerificationCheck;
import pl.kmalski.verification.domain.model.VerificationCheckType;

import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

class VerificationCheckRegistryTests {

    @Test
    void shouldReturnOnlyEnabledChecks() {
        var fraudCheck = mock(VerificationCheck.class);
        var velocityCheck = mock(VerificationCheck.class);
        var configuration = mock(VerificationConfiguration.class);

        given(fraudCheck.type()).willReturn(VerificationCheckType.FRAUD);
        given(velocityCheck.type()).willReturn(VerificationCheckType.VELOCITY);
        given(configuration.getEnabledVerificationCheckTypes()).willReturn(Set.of(VerificationCheckType.VELOCITY));

        var registry = new VerificationCheckRegistry(List.of(fraudCheck, velocityCheck), configuration);

        assertThat(registry.getEnabledVerificationChecks()).containsExactly(velocityCheck);
    }
}
