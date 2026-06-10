package pl.kmalski.verification.application.orchestration;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.kmalski.verification.application.port.VerificationRepository;
import pl.kmalski.verification.domain.model.*;
import pl.kmalski.verification.domain.policy.VerificationDecisionPolicy;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verifyNoMoreInteractions;

@ExtendWith(MockitoExtension.class)
class VerificationWorkflowTests {

    @Mock
    private VerificationRepository repository;

    @Mock
    private VerificationDecisionPolicy decisionPolicy;

    @Mock
    private VerificationOrchestrator orchestrator;

    @InjectMocks
    private VerificationWorkflow workflow;

    @Test
    void shouldCompleteVerificationWhenAllChecksPass() {
        var verificationId = VerificationId.random();
        var verification = Verification.start(validPaymentData());
        var results = List.of(VerificationCheckResult.passed(VerificationCheckType.FRAUD));

        given(repository.findById(verificationId)).willReturn(Optional.of(verification));
        given(orchestrator.execute(verification.getPayment())).willReturn(results);
        given(decisionPolicy.decide(results)).willReturn(VerificationDecision.APPROVED);

        workflow.start(verificationId);

        then(repository).should(times(2)).save(verification);
        then(orchestrator).should().execute(verification.getPayment());
        then(decisionPolicy).should().decide(results);
        assertThat(verification.getStatus()).isEqualTo(VerificationStatus.COMPLETED);
        assertThat(verification.getDecision()).isEqualTo(VerificationDecision.APPROVED);
        assertThat(verification.getCheckResults()).containsExactlyElementsOf(results);
    }

    @Test
    void shouldMarkVerificationFailedWhenExecutionThrows() {
        var verificationId = VerificationId.random();
        var verification = Verification.start(validPaymentData());
        given(repository.findById(verificationId)).willReturn(Optional.of(verification));
        given(orchestrator.execute(verification.getPayment())).willThrow(new IllegalStateException("Boom"));

        workflow.start(verificationId);

        then(repository).should(times(2)).save(verification);
        assertThat(verification.getStatus()).isEqualTo(VerificationStatus.FAILED);
        assertThat(verification.getFailureReason()).isEqualTo("Boom");
    }

    @Test
    void shouldIgnoreMissingVerificationWhenFailureOccursBeforeLoad() {
        var verificationId = VerificationId.random();
        given(repository.findById(verificationId)).willReturn(Optional.empty());

        assertThatThrownBy(() -> workflow.start(verificationId))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageMatching("Could not find verification with id [a-f0-9\\-]+");

        then(repository).should().findById(verificationId);
        verifyNoMoreInteractions(repository);
    }

    private static PaymentData validPaymentData() {
        return new PaymentData(
                new PaymentId("payment-1"),
                new CustomerId("customer-1"),
                new Amount(new BigDecimal("10.00")),
                new Currency("PLN"),
                new Country("PL")
        );
    }
}
