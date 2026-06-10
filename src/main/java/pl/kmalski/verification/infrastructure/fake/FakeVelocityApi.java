package pl.kmalski.verification.infrastructure.fake;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pl.kmalski.verification.application.port.VelocityApi;
import pl.kmalski.verification.application.port.VerificationConfiguration;

import java.time.Duration;
import java.util.random.RandomGenerator;

@Component
@RequiredArgsConstructor
class FakeVelocityApi implements VelocityApi {

    private final VerificationConfiguration configuration;
    private final LatencySimulator latencySimulator;

    @Override
    public int count(String customerId, Duration window) {
        latencySimulator.simulateLatency();

        var velocityLimit = configuration.getVelocityLimit();

        return RandomGenerator.getDefault().nextInt(velocityLimit + (int) Math.floor(0.5 * velocityLimit));
    }

}
