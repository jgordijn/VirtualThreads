package playground.examples;

import reactor.core.publisher.Flux;

import java.time.Duration;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.IntStream;

public class Example1Done {
    public static void main(String[] args) throws InterruptedException {
        var start = System.nanoTime();
// 1.1
        var t = Thread.ofPlatform().start(() -> {
            try {
                Thread.sleep(Duration.ofSeconds(2));
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            System.out.println(STR. "Hello from thread \{ Thread.currentThread() }" );
        });
// 1.2
        // make virtual
// 1.3
        // add .join()


// 1.4 Thread per task 5000 -> 10000

        try (var esc = Executors.newThreadPerTaskExecutor(Executors.defaultThreadFactory())) {
            IntStream.range(0, 10_000).forEach(i ->
                    esc.submit(() -> {
                        try {
                            Thread.sleep(Duration.ofSeconds(1));
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                        System.out.println(STR. "[\{ i }] Hello from thread \{ Thread.currentThread() }" );
                    })
            );

        }


// Async

        Flux.range(0, 10_000)
                .flatMap(i -> Flux.just(i)
                        .delayElements(Duration.ofSeconds(1))
                        .doOnNext(j -> System.out.println(STR. "[\{ j }] Hello from thread \{ Thread.currentThread() }" ))
                )
//                        , 10000) /// ADD THIS TO HAVE 1 sec done
                .last()
                .block();


//        try (ExecutorService es = Executors.newThreadPerTaskExecutor(Executors.defaultThreadFactory())) {
//        try (ExecutorService es = Executors.newFixedThreadPool(5_000)) {
        try (ExecutorService es = Executors.newVirtualThreadPerTaskExecutor()) {
            IntStream.range(0, 10_000).forEach(i ->
                    es.submit(() -> {
                        try {
                            Thread.sleep(Duration.ofSeconds(1));
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                        System.out.println(STR. "Hello from thread \{ i } \{ Thread.currentThread() }" );
                    })
            );
        }
        System.out.println("Duration: " + Duration.ofNanos(System.nanoTime() - start));
    }
}
