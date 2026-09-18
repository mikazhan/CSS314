import java.util.concurrent.ThreadLocalRandom;

public class Part1 {
    static long totalHits = 0;
    static final long TOTAL_POINTS = 50_000_000L;
    static final int THREADS = 4;

    public static void main(String[] args) throws InterruptedException {
        for (int run = 1; run <= 5; run++) {
            totalHits = 0;
            Thread[] threads = new Thread[THREADS];
            long pointsPerThread = TOTAL_POINTS / THREADS;

            for (int i = 0; i < THREADS; i++) {
                threads[i] = new Thread(() -> {
                    for (long j = 0; j < pointsPerThread; j++) {
                        double x = ThreadLocalRandom.current().nextDouble();
                        double y = ThreadLocalRandom.current().nextDouble();
                        if (x * x + y * y <= 1.0) {
                            totalHits++;
                        }
                    }
                });
                threads[i].start();
            }

            for (Thread t : threads) {
                t.join();
            }

            double pi = 4.0 * totalHits / TOTAL_POINTS;
            System.out.printf("Прогон %d: totalHits = %d, pi = %.4f%n", run, totalHits, pi);
        }
    }
}


