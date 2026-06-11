package pl.kmalski.verification.infrastructure.web;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.resttestclient.autoconfigure.AutoConfigureRestTestClient;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.client.RestTestClient;
import pl.kmalski.verification.application.usecase.getverification.GetVerificationQuery;
import pl.kmalski.verification.application.usecase.getverification.GetVerificationResult;
import pl.kmalski.verification.application.usecase.getverification.GetVerificationUseCase;
import pl.kmalski.verification.application.usecase.startverification.StartVerificationCommand;
import pl.kmalski.verification.application.usecase.startverification.StartVerificationResult;
import pl.kmalski.verification.application.usecase.startverification.StartVerificationUseCase;
import pl.kmalski.verification.domain.exception.VerificationNotFoundException;
import pl.kmalski.verification.domain.model.*;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@AutoConfigureRestTestClient
@WebMvcTest(controllers = VerificationController.class)
@Import({VerificationDtoMapper.class, VerificationExceptionHandler.class})
class VerificationControllerTests {

    @Autowired
    private RestTestClient restClient;

    @MockitoBean
    private StartVerificationUseCase startVerificationUseCase;

    @MockitoBean
    private GetVerificationUseCase getVerificationUseCase;

    @Test
    void shouldStartVerification() {
        var verificationId = VerificationId.random();
        var command = new StartVerificationCommand(new PaymentData(
                new PaymentId("payment-1"),
                new CustomerId("customer-1"),
                new Amount(new BigDecimal("10.00")),
                new Currency("PLN"),
                new Country("PL")
        ));
        var result = new StartVerificationResult(verificationId, VerificationStatus.QUEUED);

        given(startVerificationUseCase.start(command)).willReturn(result);

        restClient.post()
                .uri("/verifications")
                .contentType(MediaType.APPLICATION_JSON)
                .body(request())
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.verificationId").isEqualTo(verificationId.value().toString())
                .jsonPath("$.status").isEqualTo("QUEUED");
    }

    @Test
    void shouldGetVerification() {
        var verificationId = VerificationId.random();
        var query = new GetVerificationQuery(verificationId);
        var result = new GetVerificationResult(
                verificationId,
                VerificationStatus.COMPLETED,
                VerificationDecision.APPROVED,
                List.of(new GetVerificationResult.VerificationCheckResult(
                        VerificationCheckType.FRAUD,
                        VerificationCheckStatus.PASSED,
                        "Passed"
                ))
        );

        given(getVerificationUseCase.get(query)).willReturn(result);

        restClient.get()
                .uri("/verifications/{id}", verificationId)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.verificationId").isEqualTo(verificationId.toString())
                .jsonPath("$.status").isEqualTo("COMPLETED")
                .jsonPath("$.decision").isEqualTo("APPROVED")
                .jsonPath("$.checks[0].type").isEqualTo("FRAUD")
                .jsonPath("$.checks[0].status").isEqualTo("PASSED")
                .jsonPath("$.checks[0].reason").isEqualTo("Passed");
    }

    @Test
    void shouldReturnBadRequestWhenRequestBodyContainsInvalidCountry() {
        restClient.post()
                .uri("/verifications")
                .contentType(MediaType.APPLICATION_JSON)
                .body(request("payment-1", "customer-1", "10.00", "PLN", "Poland"))
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody()
                .jsonPath("$.title").isEqualTo("Bad request")
                .jsonPath("$.detail").isEqualTo("payment.country: Country must be a valid ISO 3166-1 alpha-2 code")
                .jsonPath("$.instance").isEqualTo("/verifications");
    }

    @Test
    void shouldReturnBadRequestWhenPaymentIsMissing() {
        restClient.post()
                .uri("/verifications")
                .contentType(MediaType.APPLICATION_JSON)
                .body("{}")
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody()
                .jsonPath("$.title").isEqualTo("Bad request")
                .jsonPath("$.detail").isEqualTo("payment: must not be null")
                .jsonPath("$.instance").isEqualTo("/verifications");
    }

    @Test
    void shouldReturnBadRequestWhenRequestBodyContainsBlankIdentifiers() {
        restClient.post()
                .uri("/verifications")
                .contentType(MediaType.APPLICATION_JSON)
                .body(request(" ", "", "10.00", "PLN", "PL"))
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody()
                .jsonPath("$.title").isEqualTo("Bad request")
                .jsonPath("$.detail").value((String detail) -> assertThat(detail)
                        .contains("payment.customerId: must not be blank")
                        .contains("payment.paymentId: must not be blank"))
                .jsonPath("$.instance").isEqualTo("/verifications");
    }

    @Test
    void shouldReturnBadRequestWhenRequestBodyContainsNonPositiveAmount() {
        restClient.post()
                .uri("/verifications")
                .contentType(MediaType.APPLICATION_JSON)
                .body(request("payment-1", "customer-1", "0", "PLN", "PL"))
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody()
                .jsonPath("$.title").isEqualTo("Bad request")
                .jsonPath("$.detail").isEqualTo("payment.amount: must be greater than 0")
                .jsonPath("$.instance").isEqualTo("/verifications");
    }

    @Test
    void shouldReturnBadRequestWhenRequestBodyContainsInvalidCurrency() {
        restClient.post()
                .uri("/verifications")
                .contentType(MediaType.APPLICATION_JSON)
                .body(request("payment-1", "customer-1", "10.00", "PLNN", "PL"))
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody()
                .jsonPath("$.title").isEqualTo("Bad request")
                .jsonPath("$.detail").isEqualTo("payment.currency: Currency must be a valid ISO 4217 code")
                .jsonPath("$.instance").isEqualTo("/verifications");
    }

    @Test
    void shouldReturnNotFoundWhenVerificationIsMissing() {
        var verificationId = VerificationId.random();
        given(getVerificationUseCase.get(any()))
                .willThrow(new VerificationNotFoundException(verificationId));

        restClient.get()
                .uri("/verifications/{id}", verificationId)
                .exchange()
                .expectStatus().isNotFound()
                .expectBody()
                .jsonPath("$.title").isEqualTo("Not found")
                .jsonPath("$.detail").isEqualTo("Verification with id " + verificationId + " not found")
                .jsonPath("$.instance").isEqualTo("/verifications/" + verificationId);
    }

    @Test
    void shouldReturnInternalServerErrorWhenUseCaseThrowsUnexpectedException() {
        given(startVerificationUseCase.start(any()))
                .willThrow(new IllegalStateException("boom"));

        restClient.post()
                .uri("/verifications")
                .contentType(MediaType.APPLICATION_JSON)
                .body(request())
                .exchange()
                .expectStatus().is5xxServerError()
                .expectBody()
                .jsonPath("$.title").isEqualTo("Internal Server Error")
                .jsonPath("$.instance").isEqualTo("/verifications");
    }

    private String request() {
        return request("payment-1", "customer-1", "10.00", "PLN", "PL");
    }

    private String request(String paymentId, String customerId, String amount, String currency, String country) {
        return """
                {
                  "payment": {
                    "paymentId": "%s",
                    "customerId": "%s",
                    "amount": %s,
                    "currency": "%s",
                    "country": "%s"
                  }
                }
                """.formatted(paymentId, customerId, amount, currency, country);
    }

}
