package pl.kmalski.verification.infrastructure.inmemory;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatcher;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.kmalski.verification.application.port.VerificationRegistrationService.VerificationRegistration;
import pl.kmalski.verification.domain.model.*;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@ExtendWith(MockitoExtension.class)
class InMemoryVerificationRegistrationServiceTests {

    @Mock
    private InMemoryVerificationStore store;

    @InjectMocks
    private InMemoryVerificationRegistrationService service;

    @Test
    void shouldCreateVerificationForNewPayment() {
        var payment = payment("payment-1");

        given(store.register(argThat(has(payment))))
                .willAnswer(invocation -> new VerificationRegistration(invocation.getArgument(0), true));

        var registration = service.registerForPayment(payment);

        assertThat(registration.created()).isTrue();
        assertThat(registration.verification().getPayment()).isEqualTo(payment);
        assertThat(registration.verification().getStatus()).isEqualTo(VerificationStatus.QUEUED);
    }

    @Test
    void shouldReturnExistingVerificationForDuplicatePayment() {
        var payment = payment("payment-2");
        var existingVerification = Verification.start(payment);

        given(store.register(argThat(has(payment))))
                .willReturn(new VerificationRegistration(existingVerification, false));

        var registration = service.registerForPayment(payment);

        assertThat(registration.created()).isFalse();
        assertThat(registration.verification()).isEqualTo(existingVerification);
        then(store).should().register(argThat(candidate -> candidate.getPayment().equals(payment)));
    }

    private static PaymentData payment(String paymentId) {
        return new PaymentData(
                new PaymentId(paymentId),
                new CustomerId("customer-1"),
                new Amount(new BigDecimal("10.00")),
                new Currency("PLN"),
                new Country("PL")
        );
    }

    private static ArgumentMatcher<Verification> has(PaymentData payment) {
        return candidate -> candidate.getPayment().equals(payment);
    }

}
