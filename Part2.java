import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicLong;

public class Part2 {
    static AtomicLong totalHits = new AtomicLong(0);
    static final long TOTAL_POINTS = 50_000_000L;

    public static void main(String[] args) throws InterruptedException {
        
        long startSingle = System.currentTimeMillis();
        long singleHits = 0;
        for (long i = 0; i < TOTAL_POINTS; i++) {
            double x = ThreadLocalRandom.current().nextDouble();
            double y = ThreadLocalRandom.current().nextDouble();
            if (x * x + y * y <= 1.0) {
                singleHits++;
            }
        }
        long timeSingle = System.currentTimeMillis() - startSingle;
        System.out.printf(" Single Thread: time = %d ms, pi = %.5f%n", 
                timeSingle, 4.0 * singleHits / TOTAL_POINTS);

        
        totalHits.set(0);
        long startMulti = System.currentTimeMillis();
        Thread[] threads = new Thread[4];
        long pointsPerThread = TOTAL_POINTS / 4;

        for (int i = 0; i < 4; i++) {
            threads[i] = new Thread(() -> {
                for (long j = 0; j < pointsPerThread; j++) {
                    double x = ThreadLocalRandom.current().nextDouble();
                    double y = ThreadLocalRandom.current().nextDouble();
                    if (x * x + y * y <= 1.0) {
                        totalHits.incrementAndGet(); 
                    }
                }
            });
            threads[i].start();
        }

        for (Thread t : threads) {
            t.join();
        }
        long timeMulti = System.currentTimeMillis() - startMulti;

        System.out.printf("Atomic 4 Threads: time = %d ms, pi = %.5f%n", 
                timeMulti, 4.0 * totalHits.get() / TOTAL_POINTS);
    }
}

