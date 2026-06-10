package pl.kmalski.verification.application.usecase.getverification;

import pl.kmalski.verification.domain.model.*;

import java.util.List;

public record GetVerificationResult(VerificationId verificationId,
                                    VerificationStatus status,
                                    VerificationDecision decision,
                                    List<VerificationCheckResult> checkResults) {

    public record VerificationCheckResult(VerificationCheckType type,
                                          VerificationCheckStatus status,
                                          String reason) {}

}
