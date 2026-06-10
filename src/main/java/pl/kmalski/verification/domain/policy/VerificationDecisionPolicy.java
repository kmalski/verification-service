package pl.kmalski.verification.domain.policy;

import pl.kmalski.verification.domain.model.VerificationCheckResult;
import pl.kmalski.verification.domain.model.VerificationCheckStatus;
import pl.kmalski.verification.domain.model.VerificationDecision;

import java.util.List;
import java.util.stream.Collectors;

import static java.util.Objects.requireNonNull;

public class VerificationDecisionPolicy {

    public VerificationDecision decide(List<VerificationCheckResult> checkResults) {
        requireNonNull(checkResults);

        var uniqueStatues = checkResults.stream()
                .map(VerificationCheckResult::status)
                .collect(Collectors.toSet());

        if (uniqueStatues.contains(VerificationCheckStatus.FAILED)) {
            return VerificationDecision.REJECTED;
        }

        if (uniqueStatues.contains(VerificationCheckStatus.REQUIRES_REVIEW)) {
            return VerificationDecision.REQUIRES_REVIEW;
        }

        return VerificationDecision.APPROVED;
    }

}
