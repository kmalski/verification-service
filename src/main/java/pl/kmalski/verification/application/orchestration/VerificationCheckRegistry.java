package pl.kmalski.verification.application.orchestration;

import org.springframework.stereotype.Component;
import pl.kmalski.verification.application.port.VerificationConfiguration;
import pl.kmalski.verification.application.strategy.check.VerificationCheck;
import pl.kmalski.verification.domain.model.VerificationCheckType;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class VerificationCheckRegistry {

    private final Map<VerificationCheckType, VerificationCheck> checks;
    private final VerificationConfiguration configuration;

    public VerificationCheckRegistry(List<VerificationCheck> checks,
                                     VerificationConfiguration configuration) {
        this.checks = checks.stream()
                .collect(Collectors.toUnmodifiableMap(
                        VerificationCheck::type,
                        Function.identity()
                ));
        this.configuration = configuration;
    }

    public Collection<VerificationCheck> getEnabledVerificationChecks() {
        var enabledTypes = configuration.getEnabledVerificationCheckTypes();
        return checks.entrySet().stream()
                .filter(kv -> enabledTypes.contains(kv.getKey()))
                .map(Entry::getValue)
                .toList();
    }

}
