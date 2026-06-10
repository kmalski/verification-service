package pl.kmalski.verification.infrastructure.fake;

import org.springframework.stereotype.Component;
import pl.kmalski.verification.application.port.VelocityApi;

import java.time.Duration;

@Component
public class FakeVelocityApi implements VelocityApi {

    @Override
    public int count(String customerId, Duration window) {
        return 0;
    }

}
