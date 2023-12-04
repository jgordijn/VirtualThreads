package playground.examples;

import java.time.Duration;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.StructuredTaskScope;

public class Example3 {
    public static void main(String[] args) {
        try {
            var response = handle();
            System.out.println(STR. "\{ Thread.currentThread() } Response: " + response);
        } catch (Exception e) {
            System.err.println(STR. "\{ Thread.currentThread() } Exception: " + e);
        }
    }

    static String handle() throws Exception {
        try (var esvc = Executors.newVirtualThreadPerTaskExecutor()) {
            Future<String> user = esvc.submit((Example3::findUser));
            Future<Integer> order = esvc.submit(Example3::fetchOrder);
            String theUser = user.get();
            int theOrder = order.get();
            return STR. "\{ theUser } has order: \{ theOrder }" ;
        }
    }

    public static String findUser() throws InterruptedException {
        Thread.sleep(Duration.ofSeconds(5));
        System.out.println(STR. "\{ Thread.currentThread() } Found user" );
        return "John Doe";
    }

    public static Integer fetchOrder() throws InterruptedException {
        insertBugHere();
        Thread.sleep(Duration.ofSeconds(1));
        System.out.println(STR. "\{ Thread.currentThread() } Found order" );
        return 213;
    }

    static void insertBugHere() {
        System.err.println(STR. "\{ Thread.currentThread() } BUG! Exception is thrown!!!" );
        throw new RuntimeException("Something went wrong");
    }
}
