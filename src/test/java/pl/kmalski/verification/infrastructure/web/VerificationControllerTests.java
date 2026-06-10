package pl.kmalski.verification.infrastructure.web;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import pl.kmalski.verification.application.usecase.getverification.GetVerificationQuery;
import pl.kmalski.verification.application.usecase.getverification.GetVerificationResult;
import pl.kmalski.verification.application.usecase.getverification.GetVerificationUseCase;
import pl.kmalski.verification.application.usecase.startverification.StartVerificationCommand;
import pl.kmalski.verification.application.usecase.startverification.StartVerificationResult;
import pl.kmalski.verification.application.usecase.startverification.StartVerificationUseCase;
import pl.kmalski.verification.domain.exception.VerificationNotFoundException;
import pl.kmalski.verification.domain.model.*;
import pl.kmalski.verification.infrastructure.web.mapper.VerificationDtoMapper;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = VerificationController.class)
@Import({VerificationDtoMapper.class, VerificationExceptionHandler.class})
class VerificationControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private StartVerificationUseCase startVerificationUseCase;

    @MockitoBean
    private GetVerificationUseCase getVerificationUseCase;

    @Test
    void shouldStartVerification() throws Exception {
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

        mockMvc.perform(post("/verifications")
                        .contentType("application/json")
                        .content("""
                                {
                                  "payment": {
                                    "paymentId": "payment-1",
                                    "customerId": "customer-1",
                                    "amount": 10.00,
                                    "currency": "PLN",
                                    "country": "PL"
                                  }
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.verificationId").value(verificationId.value().toString()))
                .andExpect(jsonPath("$.status").value("QUEUED"));
    }

    @Test
    void shouldGetVerification() throws Exception {
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

        mockMvc.perform(get("/verifications/{id}", verificationId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.verificationId").value(verificationId.toString()))
                .andExpect(jsonPath("$.status").value("COMPLETED"))
                .andExpect(jsonPath("$.decision").value("APPROVED"))
                .andExpect(jsonPath("$.checks[0].type").value("FRAUD"))
                .andExpect(jsonPath("$.checks[0].status").value("PASSED"))
                .andExpect(jsonPath("$.checks[0].reason").value("Passed"));
    }

    @Test
    void shouldReturnBadRequestWhenRequestBodyContainsInvalidCountry() throws Exception {
        mockMvc.perform(post("/verifications")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "payment": {
                                    "paymentId": "payment-1",
                                    "customerId": "customer-1",
                                    "amount": 10.00,
                                    "currency": "PLN",
                                    "country": "Poland"
                                  }
                                }
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.title").value("Bad request"))
                .andExpect(jsonPath("$.detail").value("Country must be a valid ISO 3166-1 alpha-2 code"))
                .andExpect(jsonPath("$.instance").value("/verifications"));
    }

    @Test
    void shouldReturnNotFoundWhenVerificationIsMissing() throws Exception {
        var verificationId = VerificationId.random();
        when(getVerificationUseCase.get(any()))
                .thenThrow(new VerificationNotFoundException(verificationId));

        mockMvc.perform(get("/verifications/{id}", verificationId))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.title").value("Not found"))
                .andExpect(jsonPath("$.detail").value("Verification with id " + verificationId + " not found"))
                .andExpect(jsonPath("$.instance").value("/verifications/" + verificationId));
    }


    @Test
    void shouldReturnInternalServerErrorWhenUseCaseThrowsUnexpectedException() throws Exception {
        when(startVerificationUseCase.start(any()))
                .thenThrow(new IllegalStateException("boom"));

        mockMvc.perform(post("/verifications")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "payment": {
                                    "paymentId": "payment-1",
                                    "customerId": "customer-1",
                                    "amount": 10.00,
                                    "currency": "PLN",
                                    "country": "PL"
                                  }
                                }
                                """))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.title").value("Internal Server Error"))
                .andExpect(jsonPath("$.instance").value("/verifications"));
    }

}
