package pl.kmalski.verification.infrastructure.web;

import org.junit.jupiter.api.Test;
import pl.kmalski.verification.application.usecase.getverification.GetVerificationResult;
import pl.kmalski.verification.application.usecase.getverification.GetVerificationResult.VerificationCheckResult;
import pl.kmalski.verification.application.usecase.startverification.StartVerificationResult;
import pl.kmalski.verification.domain.model.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static pl.kmalski.verification.infrastructure.web.GetVerificationResponse.VerificationCheckResponse;

class VerificationDtoMapperTests {

    private final VerificationDtoMapper mapper = new VerificationDtoMapper();

    @Test
    void shouldMapStartRequestToCommand() {
        var request = new StartVerificationRequest(
                new StartVerificationRequest.Payment(
                        "payment-1",
                        "customer-1",
                        new BigDecimal("10.00"),
                        "PLN",
                        "PL"
                )
        );

        var command = mapper.toStartVerificationCommand(request);

        assertThat(command.payment()).isEqualTo(new PaymentData(
                new PaymentId("payment-1"),
                new CustomerId("customer-1"),
                new Amount(new BigDecimal("10.00")),
                new Currency("PLN"),
                new Country("PL")
        ));
    }

    @Test
    void shouldMapStartResultToResponse() {
        var verificationId = VerificationId.random();
        var result = new StartVerificationResult(verificationId, VerificationStatus.QUEUED);

        var response = mapper.toStartVerificationResponse(result);

        assertThat(response.verificationId()).isEqualTo(verificationId.value());
        assertThat(response.status()).isEqualTo(VerificationStatus.QUEUED);
    }

    @Test
    void shouldMapGetResultToResponse() {
        var verificationId = VerificationId.random();
        var result = new GetVerificationResult(
                verificationId,
                VerificationStatus.COMPLETED,
                VerificationDecision.APPROVED,
                List.of(new VerificationCheckResult(
                        VerificationCheckType.FRAUD,
                        VerificationCheckStatus.PASSED,
                        "Passed"
                ))
        );

        var response = mapper.toGetVerificationResponse(result);

        assertThat(response.verificationId()).isEqualTo(verificationId.value());
        assertThat(response.status()).isEqualTo(VerificationStatus.COMPLETED);
        assertThat(response.decision()).isEqualTo(VerificationDecision.APPROVED);
        assertThat(response.checks()).containsExactly(
                new VerificationCheckResponse(
                        VerificationCheckType.FRAUD,
                        VerificationCheckStatus.PASSED,
                        "Passed"
                )
        );
    }

    @Test
    void shouldMapVerificationIdToQuery() {
        var verificationId = UUID.fromString("123e4567-e89b-12d3-a456-426614174000");

        var query = mapper.toGetVerificationQuery(verificationId);

        assertThat(query.verificationId()).isEqualTo(new VerificationId(verificationId));
    }
}
