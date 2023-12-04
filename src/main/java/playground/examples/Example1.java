package playground.examples;

import reactor.core.publisher.Flux;

import java.time.Duration;
import java.util.concurrent.Executors;
import java.util.stream.IntStream;

public class Example1 {
    public static void main(String[] args) throws InterruptedException {
        var start = System.nanoTime();

        // 2. Create a thread per task executor and submit tasks that will sleep for 1 second and then print a message


        // 3. Async with Flux.range

        System.out.println(STR. "Duration: \{ Duration.ofNanos(System.nanoTime() - start) }" );
    }

}
