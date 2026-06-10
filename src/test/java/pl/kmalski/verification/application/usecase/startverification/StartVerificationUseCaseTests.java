package pl.kmalski.verification.application.usecase.startverification;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.kmalski.verification.application.orchestration.VerificationWorkflow;
import pl.kmalski.verification.application.port.VerificationRepository;
import pl.kmalski.verification.domain.model.PaymentData;
import pl.kmalski.verification.domain.model.Verification;
import pl.kmalski.verification.domain.model.VerificationStatus;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.BDDMockito.then;

@ExtendWith(MockitoExtension.class)
class StartVerificationUseCaseTests {

    @Mock
    private VerificationRepository repository;

    @Mock
    private VerificationWorkflow workflow;

    @InjectMocks
    private StartVerificationUseCase useCase;

    @Test
    void shouldCreateStoreAndStartVerification() {
        var command = new StartVerificationCommand(validPaymentData());

        var result = useCase.start(command);

        then(repository).should().save(argThat(savedVerification ->
                savedVerification.getId().equals(result.verificationId())
                        && savedVerification.getPayment().equals(command.payment())
                        && savedVerification.getStatus() == VerificationStatus.QUEUED
        ));
        then(workflow).should().start(result.verificationId());

        assertThat(result.verificationId()).isNotNull();
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
