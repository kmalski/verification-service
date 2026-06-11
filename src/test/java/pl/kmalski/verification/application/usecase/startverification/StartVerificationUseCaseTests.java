package pl.kmalski.verification.application.usecase.startverification;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.kmalski.verification.application.orchestration.VerificationWorkflow;
import pl.kmalski.verification.application.port.VerificationRegistrationService;
import pl.kmalski.verification.domain.model.*;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.never;

@ExtendWith(MockitoExtension.class)
class StartVerificationUseCaseTests {

    @Mock
    private VerificationRegistrationService registrationService;

    @Mock
    private VerificationWorkflow workflow;

    @InjectMocks
    private StartVerificationUseCase useCase;

    @Test
    void shouldCreateStoreAndStartVerification() {
        var command = new StartVerificationCommand(validPaymentData());

        given(registrationService.registerForPayment(command.payment()))
                .willAnswer(invocation -> new VerificationRegistrationService.VerificationRegistration(
                        Verification.start(invocation.getArgument(0)),
                        true
                ));

        var result = useCase.start(command);

        then(registrationService).should().registerForPayment(command.payment());
        then(workflow).should().start(result.verificationId());

        assertThat(result.verificationId()).isNotNull();
        assertThat(result.status()).isEqualTo(VerificationStatus.QUEUED);
    }

    @Test
    void shouldReturnExistingVerificationWithoutStartingWorkflowForDuplicatePayment() {
        var command = new StartVerificationCommand(validPaymentData());
        var existingVerification = Verification.start(validPaymentData());

        given(registrationService.registerForPayment(command.payment()))
                .willReturn(new VerificationRegistrationService.VerificationRegistration(existingVerification, false));

        var result = useCase.start(command);

        then(workflow).should(never()).start(any());

        assertThat(result.verificationId()).isEqualTo(existingVerification.getId());
        assertThat(result.status()).isEqualTo(existingVerification.getStatus());
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
