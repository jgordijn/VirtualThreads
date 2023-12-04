package playground.stacktrace;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.concurrent.ThreadLocalRandom;

public class Reactor {

    public static void main(String[] args) throws Exception {
        var reactor = new Reactor();
        reactor.foo();
        reactor.bar();
        Thread.sleep(10000);
    }

    public void foo() {
        Flux.range(100, 200)
                .concatMap(this::calculate)
                .doOnEach(System.out::println)
                .subscribe();
    }

    public void bar() {
        Flux.range(2000, 2500)
                .concatMap(this::calculate)
                .doOnEach(out -> System.out.println(">> " + out))
                .subscribe();
    }

    private Mono<String> calculate(long nr) {
        double r = ThreadLocalRandom.current().nextDouble();
        if (nr * r < 10) {
            return Mono.defer(() -> Mono.error(new RuntimeException("BOOM")));
        } else {
            return Mono
                    .fromCallable(() -> STR. "[\{ nr }] Hello from thread \{ Thread.currentThread() }" )
                    .delayElement(Duration.ofMillis(500));
        }
    }
}
