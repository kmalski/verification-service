package pl.kmalski.verification.infrastructure.inmemory;

import org.junit.jupiter.api.Test;
import pl.kmalski.verification.domain.model.*;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

class InMemoryVerificationRepositoryTests {

    private final InMemoryVerificationRepository repository = new InMemoryVerificationRepository();

    @Test
    void shouldReturnEmptyWhenVerificationDoesNotExist() {
        assertThat(repository.findById(VerificationId.random())).isEmpty();
    }

    @Test
    void shouldSaveAndReturnVerificationById() {
        var verification = Verification.start(validPaymentData());

        repository.save(verification);

        assertThat(repository.findById(verification.getId()))
                .isNotEmpty()
                .contains(verification);
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
