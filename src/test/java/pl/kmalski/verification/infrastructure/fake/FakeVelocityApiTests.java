package pl.kmalski.verification.infrastructure.fake;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.kmalski.verification.application.port.VerificationConfiguration;

import java.time.Duration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@ExtendWith(MockitoExtension.class)
class FakeVelocityApiTests {

    @Mock
    private VerificationConfiguration configuration;

    @Mock
    private LatencySimulator latencySimulator;

    @InjectMocks
    private FakeVelocityApi velocityApi;

    @Test
    void shouldReturnValueWithinExpectedRange() {
        given(configuration.getVelocityLimit()).willReturn(10);

        var result = velocityApi.count("customer-1", Duration.ofMinutes(5));

        assertThat(result).isBetween(0, 14);
        then(latencySimulator).should().simulateLatency();
    }

    @Test
    void shouldNotDependOnCustomerIdOrWindowForTheReturnedRange() {
        given(configuration.getVelocityLimit()).willReturn(4);

        var result = velocityApi.count("customer-abc", Duration.ofDays(1));

        assertThat(result).isBetween(0, 5);
        then(latencySimulator).should().simulateLatency();
    }

}
