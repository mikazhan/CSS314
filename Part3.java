import java.util.concurrent.ThreadLocalRandom;

public class Part3 {
    static final long TOTAL_POINTS = 100_000_000L;

    public static void main(String[] args) throws InterruptedException {
        int[] threadCounts = {1, 2, 4, 8, 16, 32};
        long baselineTime = 0;

        System.out.println("Threads (T) | Runtime (ms) | Speedup  | Efficiency");
        System.out.println("-------------------------------------------------");

        for (int T : threadCounts) {
            long totalHits = 0;
            long pointsPerThread = TOTAL_POINTS / T;
            WorkerThread[] threads = new WorkerThread[T];

            long start = System.currentTimeMillis();

            for (int i = 0; i < T; i++) {
                threads[i] = new WorkerThread(pointsPerThread);
                threads[i].start();
            }

            for (int i = 0; i < T; i++) {
                threads[i].join();
                totalHits += threads[i].localHits; // Редукция (сложение)
            }

            long runtime = System.currentTimeMillis() - start;

            if (T == 1) {
                baselineTime = runtime;
            }

            double speedup = (double) baselineTime / runtime;
            double efficiency = (speedup / T) * 100.0;

            System.out.printf("%-11d | %-12d | %-8.2fx | %-6.1f%%%n", 
                    T, runtime, speedup, efficiency);
        }
    }

    static class WorkerThread extends Thread {
        long points;
        long localHits = 0;

        WorkerThread(long points) {
            this.points = points;
        }

        @Override
        public void run() {
            for (long i = 0; i < points; i++) {
                double x = ThreadLocalRandom.current().nextDouble();
                double y = ThreadLocalRandom.current().nextDouble();
                if (x * x + y * y <= 1.0) {
                    localHits++;
                }
            }
        }
    }
}

