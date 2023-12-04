package playground;

import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.Callable;
import java.util.concurrent.StructuredTaskScope;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;

public class Main {

    private static Callable<Void> create(int nr) {
        return () -> {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            System.out.println(STR. "\{ Instant.now() } [\{ nr }] - \{ Thread.currentThread() } Hello from thread" );
            return null;
        };
    }
    static AtomicInteger counter = new AtomicInteger(0);

    private static Thread start(int nr, Thread.Builder builder) {
        return builder.name("test", nr).start(() -> {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            counter.incrementAndGet();
            System.out.println(STR. "\{ Instant.now() } [\{ nr }] - \{ Thread.currentThread() } Hello from thread" );
        });
    }

    private static void startNThread(int nr, Thread.Builder builder) {
        var result = IntStream.range(0, nr).mapToObj(i -> start(i, builder)).toList();
        for (Thread thread : result) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }


    public static void main(String[] args) throws Exception {
        var start = System.nanoTime();

        startNThread(1_000_000, Thread.ofVirtual());

        try(var scope = new StructuredTaskScope.ShutdownOnSuccess<>()) {
            var result = scope.fork(() -> {
                Thread.sleep(1000);
                return "Test";
            });
            scope.join();
            scope.result();
        }

        var end = System.nanoTime();
        System.out.println(STR. "Duration: \{ Duration.ofNanos(end - start) } (\{counter.get()})" );
    }
}
