package playground.structured;

import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.StructuredTaskScope;

public class StructuredConcurrencyThread {

    public static void main(String[] args) throws Exception {
        System.out.println("First complete");
        firstComplete();
        System.out.println("---");
        System.out.println("Both complete");
        bothComplete();
        System.out.println("---");
        System.out.println("Exception complete");
        failure();
    }

    private static void firstComplete() throws Exception {
        var start = System.currentTimeMillis();
        String result = raceFirst();
        var end = System.currentTimeMillis();
        System.out.println("result = " + result);
        System.out.println("Duration: " + Duration.ofMillis(end - start));
    }

    private static String raceFirst() throws InterruptedException, ExecutionException {
        try (var scope = new StructuredTaskScope.ShutdownOnSuccess<String>()) {
            scope.fork(() -> run(2));
            scope.fork(() -> run(1));
            scope.join();
            return scope.result();
        }
    }

    private static void bothComplete() throws Exception {
        var start = System.currentTimeMillis();
        try (var scope = new StructuredTaskScope.ShutdownOnFailure()) {
            var a = scope.fork(() -> run(2));
            var b = scope.fork(() -> run(1));
            scope.join().throwIfFailed();
            System.out.println(String.join("\n", a.get(), b.get()));
        }
        var end = System.currentTimeMillis();
        System.out.println("Duration: " + Duration.ofMillis(end - start));
    }

    private static void failure() throws Exception {
        var start = System.currentTimeMillis();
        shutdownOnFailure();
        var end = System.currentTimeMillis();
        System.out.println("Duration: " + Duration.ofMillis(end - start));
    }

    private static void shutdownOnFailure() throws ExecutionException, InterruptedException {
        try (var scope = new StructuredTaskScope.ShutdownOnFailure()) {
            var a = scope.fork(() -> run(2));
            var b = scope.fork(() -> run(1));
            var c = scope.fork(() -> exception(1));
            scope.join().throwIfFailed();
            System.out.println(String.join("\n", a.get(), b.get()));
        }
    }

    private static String run(int i) throws Exception {
        System.out.println("Start " + i);
        Thread.sleep(Duration.ofSeconds(i));
        System.out.println("Done " + i);
        return STR. "\{ Instant.now() } - \{ Thread.currentThread() } Hello from run(\{ i })" ;
    }

    private static String exception(int i) throws Exception {
        Thread.sleep(Duration.ofSeconds(i));
        throw new RuntimeException("BOOM");
    }
}
