package playground.mutex;

import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;
import java.util.stream.IntStream;

public class LimitConcurrency {
    private static final Semaphore CONCURRENCY_SEMAPHORE = new Semaphore(4);


    private static void start(int nr)  {
        try {
            CONCURRENCY_SEMAPHORE.acquire();
            Thread.sleep(Duration.ofSeconds(1));
            System.out.println(STR. "\{ Instant.now() } [\{ nr }] - \{ Thread.currentThread() } Hello from thread" );

        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            CONCURRENCY_SEMAPHORE.release();
        }
    }

    public static void main(String[] args) {
        System.out.println(STR. "\{ Instant.now() } - \{ Thread.currentThread() } Starting" );
        try (ExecutorService es = Executors.newVirtualThreadPerTaskExecutor()) {
            IntStream.range(0, 20).forEach(i ->
                    es.submit(() -> {
                        start(i);
                    })
            );
        }
    }

}
