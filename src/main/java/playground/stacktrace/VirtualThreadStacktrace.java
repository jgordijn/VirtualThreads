package playground.stacktrace;

import java.util.concurrent.ThreadLocalRandom;

public class VirtualThreadStacktrace {
    public static void main(String[] args) throws Exception {
        var reactor = new VirtualThreadStacktrace();
        Thread.startVirtualThread(reactor::foo);
        Thread.startVirtualThread(reactor::bar);
        Thread.sleep(10000);
    }

    public void foo() {
        for (int i = 100; i < 200; i++) {
            var result = calculate(i);
            System.out.println(result);
        }
    }

    public void bar() {
        for (int i = 2000; i < 2500; i++) {
            var result = calculate(i);
            System.out.println(">> " + result);
        }
    }

    private String calculate(long nr) {
        double r = ThreadLocalRandom.current().nextDouble();
        if (nr * r < 10) {
            throw new RuntimeException("BOOM");
        } else {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            return STR. "[\{ nr }] Hello from thread \{ Thread.currentThread() }" ;
        }
    }

}
