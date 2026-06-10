package pl.kmalski.verification.infrastructure.web.dto;

import pl.kmalski.verification.domain.model.VerificationStatus;

import java.util.UUID;

public record StartVerificationResponse(UUID verificationId,
                                        VerificationStatus status) {}
