package pl.kmalski.verification.infrastructure.fake;

import org.springframework.stereotype.Component;
import pl.kmalski.verification.application.port.VelocityApi;

import java.time.Duration;

@Component
public class FakeVelocityApi implements VelocityApi {

    @Override
    public int count(String customerId, Duration window) {
        try {
            Thread.sleep(Duration.ofSeconds(3));
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return 0;
    }

}
