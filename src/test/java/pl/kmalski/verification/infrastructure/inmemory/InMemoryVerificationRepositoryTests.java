package pl.kmalski.verification.infrastructure.inmemory;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.kmalski.verification.domain.model.*;

import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@ExtendWith(MockitoExtension.class)
class InMemoryVerificationRepositoryTests {

    @Mock
    private InMemoryVerificationStore store;

    @InjectMocks
    private InMemoryVerificationRepository repository;

    @Test
    void shouldReturnEmptyWhenVerificationDoesNotExist() {
        var verificationId = VerificationId.random();

        given(store.findById(verificationId)).willReturn(Optional.empty());

        assertThat(repository.findById(verificationId)).isEmpty();
    }

    @Test
    void shouldSaveAndFindVerificationById() {
        var verification = Verification.start(payment("payment-1"));

        given(store.findById(verification.getId())).willReturn(Optional.of(verification));

        repository.save(verification);

        assertThat(repository.findById(verification.getId()))
                .contains(verification);
        then(store).should().save(verification);
    }

    @Test
    void shouldSaveWorkflowStatusUpdate() {
        var verification = Verification.start(payment("payment-2"));

        verification.markInProgress();

        repository.save(verification);

        then(store).should().save(verification);
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

}
