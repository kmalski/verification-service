package pl.kmalski.verification;

import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.resttestclient.autoconfigure.AutoConfigureRestTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.client.RestTestClient;
import pl.kmalski.verification.infrastructure.fake.NoOpLatencyConfiguration;

import java.time.Duration;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest(webEnvironment = RANDOM_PORT)
@AutoConfigureRestTestClient
@TestPropertySource(properties = {
        "verification.check.enabled=FRAUD",
        "verification.check.timeout=3s",
        "verification.check.velocity.window=1m",
        "verification.check.velocity.limit=10"
})
@Import(NoOpLatencyConfiguration.class)
class VerificationIntegrationTests {

    @Autowired
    private RestTestClient restClient;

    @Test
    void shouldStartVerificationIdempotentlyAndCompleteWorkflow() {
        var request = request(UUID.randomUUID().toString());

        var firstResponse = restClient.post()
                .uri("/verifications")
                .contentType(MediaType.APPLICATION_JSON)
                .body(request)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.verificationId").exists()
                .jsonPath("$.status").value(status -> assertThat(status).isIn("QUEUED", "IN_PROGRESS", "COMPLETED"))
                .returnResult();

        var verificationId = verificationId(firstResponse.getResponseBody());

        var secondResponse = restClient.post()
                .uri("/verifications")
                .contentType(MediaType.APPLICATION_JSON)
                .body(request)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.verificationId").isEqualTo(verificationId)
                .jsonPath("$.status").value(status -> assertThat(status).isIn("QUEUED", "IN_PROGRESS", "COMPLETED"))
                .returnResult();

        assertThat(verificationId(secondResponse.getResponseBody()))
                .isEqualTo(verificationId);

        await().atMost(Duration.ofSeconds(3))
                .pollInterval(Duration.ofMillis(50))
                .untilAsserted(() -> restClient.get()
                        .uri("/verifications/{id}", verificationId)
                        .exchange()
                        .expectStatus().isOk()
                        .expectBody()
                        .jsonPath("$.verificationId").isEqualTo(verificationId)
                        .jsonPath("$.status").isEqualTo("COMPLETED")
                        .jsonPath("$.decision").isEqualTo("APPROVED")
                        .jsonPath("$.checks[0].type").isEqualTo("FRAUD")
                        .jsonPath("$.checks[0].status").isEqualTo("PASSED"));
    }

    private String verificationId(byte[] responseBody) {
        var json = new String(responseBody);
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
