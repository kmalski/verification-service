package pl.kmalski.verification.application.orchestration;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pl.kmalski.verification.application.port.VerificationConfiguration;
import pl.kmalski.verification.application.strategy.check.VerificationCheck;
import pl.kmalski.verification.domain.model.PaymentData;
import pl.kmalski.verification.domain.model.VerificationCheckResult;

import java.util.List;
import java.util.concurrent.StructuredTaskScope;
import java.util.concurrent.StructuredTaskScope.Subtask;

import static java.util.concurrent.StructuredTaskScope.Joiner.allSuccessfulOrThrow;

@Slf4j
@Service
@RequiredArgsConstructor
public class VerificationOrchestrator {

    private final List<VerificationCheck> checks;
    private final VerificationConfiguration configuration;

    public List<VerificationCheckResult> run(PaymentData payment) {
        var timeout = configuration.getVerificationCheckTimeout();

        try (var scope = StructuredTaskScope.open(allSuccessfulOrThrow(), config -> config.withTimeout(timeout))) {
            var tasks = checks.stream()
                    .map(check -> scope.fork(() -> check.execute(payment)))
                    .toList();

            scope.join();

            return tasks.stream()
                    .map(Subtask::get)
                    .toList();
        } catch (InterruptedException exc) {
            log.warn("Running verification checks interrupted", exc);
            Thread.currentThread().interrupt();
            return List.of();
        }
    }

}
