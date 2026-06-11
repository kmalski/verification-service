package pl.kmalski.verification.infrastructure.inmemory;

import org.junit.jupiter.api.Test;
import pl.kmalski.verification.application.port.VerificationRegistrationService.VerificationRegistration;
import pl.kmalski.verification.domain.model.*;

import java.math.BigDecimal;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;

class InMemoryVerificationStoreTests {

    @Test
    void shouldReturnEmptyWhenVerificationDoesNotExist() {
        var store = new InMemoryVerificationStore();

        assertThat(store.findById(VerificationId.random())).isEmpty();
    }

    @Test
    void shouldReturnExistingVerificationWhenPaymentIsRegisteredAgain() {
        var store = new InMemoryVerificationStore();
        var first = Verification.start(payment("payment-1"));
        var duplicate = Verification.start(payment("payment-1"));

        var firstRegistration = store.register(first);
        var duplicateRegistration = store.register(duplicate);

        assertThat(firstRegistration.created()).isTrue();
        assertThat(duplicateRegistration.created()).isFalse();
        assertThat(duplicateRegistration.verification().getId()).isEqualTo(first.getId());
        assertThat(store.findById(duplicate.getId())).isEmpty();
    }

    @Test
    void shouldCreateOnlyOneVerificationWhenSamePaymentIsRegisteredConcurrently() {
        var store = new InMemoryVerificationStore();
        var threadCount = 8;
        var barrier = new CyclicBarrier(threadCount);

        try (var executor = Executors.newFixedThreadPool(threadCount)) {
            var futures = IntStream.range(0, threadCount)
                    .mapToObj(_ -> executor.submit(() -> {
                        barrier.await();
                        return store.register(Verification.start(payment("payment-999")));
                    }))
                    .toList();

            var registrations = futures.stream()
                    .map(InMemoryVerificationStoreTests::await)
                    .toList();

            assertThat(registrations)
                    .filteredOn(VerificationRegistration::created)
                    .hasSize(1);
            assertThat(registrations.stream()
                    .map(registration -> registration.verification().getId())
                    .collect(Collectors.toSet()))
                    .hasSize(1);
        }
    }

    @Test
    void shouldReturnDetachedVerificationSnapshot() {
        var store = new InMemoryVerificationStore();
        var verification = Verification.start(payment("payment-2"));

        store.save(verification);
        var found = store.findById(verification.getId()).orElseThrow();

        found.markInProgress();

        var foundLater = store.findById(verification.getId()).orElseThrow();
        assertThat(foundLater)
                .isNotSameAs(found)
                .isEqualTo(found)
                .hasSameHashCodeAs(found);
        assertThat(foundLater.getStatus())
                .isEqualTo(VerificationStatus.QUEUED);
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

    private static VerificationRegistration await(Future<VerificationRegistration> future) {
        try {
            return future.get();
        } catch (Exception exc) {
            throw new IllegalStateException(exc);
        }
    }

}
