package pl.kmalski.verification.application.orchestration;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.kmalski.verification.application.port.VerificationConfiguration;
import pl.kmalski.verification.application.strategy.check.VerificationCheck;
import pl.kmalski.verification.domain.model.PaymentData;
import pl.kmalski.verification.domain.model.VerificationCheckResult;
import pl.kmalski.verification.domain.model.VerificationCheckType;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
class VerificationOrchestratorTests {

    @Mock
    private VerificationCheckRegistry checkRegistry;

    @Mock
    private VerificationConfiguration configuration;

    @InjectMocks
    private VerificationOrchestrator orchestrator;

    @Test
    void shouldExecuteEnabledChecksAndCollectResults() {
        var payment = validPaymentData();
        var fraudCheck = mock(VerificationCheck.class);
        var velocityCheck = mock(VerificationCheck.class);
        var fraudResult = VerificationCheckResult.passed(VerificationCheckType.FRAUD);
        var velocityResult = VerificationCheckResult.failed(VerificationCheckType.VELOCITY, "High velocity");

        given(configuration.getVerificationCheckTimeout()).willReturn(Duration.ofSeconds(1));
        given(checkRegistry.getEnabledVerificationChecks()).willReturn(List.of(fraudCheck, velocityCheck));
        given(fraudCheck.execute(payment)).willReturn(fraudResult);
        given(velocityCheck.execute(payment)).willReturn(velocityResult);

        var results = orchestrator.execute(payment);

        assertThat(results).containsExactlyInAnyOrder(fraudResult, velocityResult);
        then(fraudCheck).should().execute(payment);
        then(velocityCheck).should().execute(payment);
    }

    private static PaymentData validPaymentData() {
        return new PaymentData(
                "payment-1",
                "customer-1",
                new BigDecimal("10.00"),
                "PLN",
                "PL"
        );
    }
}
