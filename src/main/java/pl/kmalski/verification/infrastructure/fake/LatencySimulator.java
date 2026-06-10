package pl.kmalski.verification.infrastructure.fake;

import org.springframework.stereotype.Component;

import java.util.random.RandomGenerator;

@Component
class LatencySimulator {

    public void simulateLatency() {
        try {
            var millis = RandomGenerator.getDefault().nextLong(500, 1500);
            Thread.sleep(millis);
        } catch (InterruptedException exc) {
            Thread.currentThread().interrupt();
        }
    }

}
