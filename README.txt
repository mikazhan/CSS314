# Concurrency Benchmark: Monte Carlo Pi Estimation

This repository contains a Java-based concurrency benchmark exploring data races, synchronization overhead, and state reduction techniques using Monte Carlo Pi estimation.

## Benchmark Results

### Part 1: The Phantom Bug (Data Race)
* **Execution:** 5 runs using 4 native threads updating a shared `static long totalHits` without synchronization.
* **Calculated Pi values:** ≈ 0.92 - 0.97
* **Lesson:** Unprotected shared memory writes lead to a severe data race, causing threads to overwrite each other's memory updates and yielding completely incorrect results.

### Part 2: The Synchronization Trap
* **Single Thread:** 166 ms | $\pi \approx 3.14156$
* **AtomicLong (4 Threads):** 453 ms | $\pi \approx 3.14145$
* **Lesson:** Using thread-safe atomic operations restores mathematical accuracy, but drastically slows down execution (~3x slower than a single thread) due to heavy cache contention and memory bus locking.

### Part 3: OpenMP-Style Reduction
Simulation performed with $100,000,000$ points across different thread counts using thread-local counters:

| Threads (T) | Runtime (ms) | Speedup vs 1 Thread ($T_1 / T_N$) | Efficiency ($Speedup / T$) |
| :--- | :--- | :--- | :--- |
| **1** | 725 | 1.00x | 100.0% |
| **2** | 500 | 1.45x | 72.5% |
| **4** | 240 | 3.02x | 75.5% |
| **8** | 173 | 4.19x | 52.4% |
| **16** | 178 | 4.07x | 25.5% |
| **32** | 167 | 4.34x | 13.6% |

---

## Questions & Answers

1. **Look at your row for 16 threads. Why didn't your 8-core CPU run twice as fast as 8 threads?**
   * **Answer:** The CPU has a fixed limit of 8 physical cores[cite: 1]. Running 16 threads forces the operating system to time-slice and context-switch threads on the available cores[cite: 1]. Since no additional physical hardware cores exist to process instructions in parallel, performance plateaus and efficiency drops significantly[cite: 1].

2. **Why was the synchronized version in Part 2 slower than running on one single core?**
   * **Answer:** Calling `incrementAndGet()` forces CPU cores to repeatedly lock the memory bus and synchronize cache lines (cache coherency traffic)[cite: 1]. Threads spend up to 95% of their cycles stalling and waiting for access to the shared memory location rather than performing actual computations[cite: 1]

