package pl.kmalski.verification.infrastructure.fake;

import java.util.random.RandomGenerator;

class LatencySimulator {

    public static void simulateLatency() {
        try {
            var millis = RandomGenerator.getDefault().nextLong(500, 1500);
            Thread.sleep(millis);
        } catch (InterruptedException exc) {
            Thread.currentThread().interrupt();
        }
    }

}
