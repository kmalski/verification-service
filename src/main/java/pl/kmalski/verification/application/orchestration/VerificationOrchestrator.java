package pl.kmalski.verification.application.orchestration;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pl.kmalski.verification.application.port.VerificationConfiguration;
import pl.kmalski.verification.domain.model.PaymentData;
import pl.kmalski.verification.domain.model.VerificationCheckResult;

import java.util.List;
import java.util.concurrent.StructuredTaskScope;
import java.util.concurrent.StructuredTaskScope.Configuration;
import java.util.concurrent.StructuredTaskScope.Subtask;
import java.util.function.Function;

import static java.util.concurrent.StructuredTaskScope.Joiner.allSuccessfulOrThrow;

@Slf4j
@Service
@RequiredArgsConstructor
public class VerificationOrchestrator {

    private final VerificationCheckRegistry checkRegistry;
    private final VerificationConfiguration configuration;

    public List<VerificationCheckResult> execute(PaymentData payment) {
        try (var scope = StructuredTaskScope.open(allSuccessfulOrThrow(), config())) {
            var checks = checkRegistry.getEnabledVerificationChecks();
            log.debug("Running {} verification checks for payment {}", checks.size(), payment.paymentId());

            var tasks = checks.stream()
                    .map(check -> scope.fork(() -> check.execute(payment)))
                    .toList();

            scope.join();

            var results = tasks.stream()
                    .map(Subtask::get)
                    .toList();

            log.debug("Completed verification checks for payment {} with {} results", payment.paymentId(), results.size());
            return results;
        } catch (InterruptedException exc) {
            Thread.currentThread().interrupt();
            log.warn("Verification checks interrupted for payment {}", payment.paymentId(), exc);
            throw new IllegalStateException("Running verification checks interrupted", exc);
        }
    }

    private Function<Configuration, Configuration> config() {
        var timeout = configuration.getVerificationCheckTimeout();
        return config -> config.withTimeout(timeout);
    }

}
