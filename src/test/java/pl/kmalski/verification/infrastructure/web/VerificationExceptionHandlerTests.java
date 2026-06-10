package pl.kmalski.verification.infrastructure.web;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import pl.kmalski.verification.application.usecase.getverification.GetVerificationUseCase;
import pl.kmalski.verification.application.usecase.startverification.StartVerificationUseCase;
import pl.kmalski.verification.domain.exception.VerificationNotFoundException;
import pl.kmalski.verification.domain.model.VerificationId;
import pl.kmalski.verification.infrastructure.web.mapper.VerificationDtoMapper;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = VerificationController.class)
@Import({VerificationDtoMapper.class, VerificationExceptionHandler.class})
class VerificationExceptionHandlerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private StartVerificationUseCase startVerificationUseCase;

    @MockitoBean
    private GetVerificationUseCase getVerificationUseCase;

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

}
