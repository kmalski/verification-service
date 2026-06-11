package pl.kmalski.verification;

import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import pl.kmalski.verification.infrastructure.fake.NoOpLatencyConfiguration;

import java.time.Duration;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.oneOf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(properties = {
        "verification.check.enabled=FRAUD",
        "verification.check.timeout=3s",
        "verification.check.velocity.window=1m",
        "verification.check.velocity.limit=10"
})
@Import(NoOpLatencyConfiguration.class)
class VerificationIntegrationTests {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void shouldStartVerificationIdempotentlyAndCompleteWorkflow() throws Exception {
        var request = request(UUID.randomUUID().toString());

        var firstResponse = mockMvc.perform(post("/verifications")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.verificationId").exists())
                .andExpect(jsonPath("$.status", oneOf("QUEUED", "IN_PROGRESS", "COMPLETED")))
                .andReturn();

        var verificationId = verificationId(firstResponse.getResponse().getContentAsString());

        var secondResponse = mockMvc.perform(post("/verifications")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.verificationId", is(verificationId)))
                .andExpect(jsonPath("$.status", oneOf("QUEUED", "IN_PROGRESS", "COMPLETED")))
                .andReturn();

        assertThat(verificationId(secondResponse.getResponse().getContentAsString()))
                .isEqualTo(verificationId);

        await().atMost(Duration.ofSeconds(3))
                .pollInterval(Duration.ofMillis(50))
                .untilAsserted(() -> mockMvc.perform(get("/verifications/{id}", verificationId))
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$.verificationId", is(verificationId)))
                        .andExpect(jsonPath("$.status", is("COMPLETED")))
                        .andExpect(jsonPath("$.decision", is("APPROVED")))
                        .andExpect(jsonPath("$.checks[0].type", is("FRAUD")))
                        .andExpect(jsonPath("$.checks[0].status", is("PASSED"))));
    }

    private String verificationId(String json) {
        return JsonPath.read(json, "$.verificationId");
    }

    private String request(String paymentId) {
        return """
                {
                  "payment": {
                    "paymentId": "%s",
                    "customerId": "customer-1",
                    "amount": 10.00,
                    "currency": "PLN",
                    "country": "PL"
                  }
                }
                """.formatted(paymentId);
    }

}
