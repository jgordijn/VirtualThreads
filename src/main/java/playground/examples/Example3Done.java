package playground.examples;

import java.time.Duration;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.StructuredTaskScope;

public class Example3Done {

    public static void main(String[] args) {
        try {
            var response = handle();
            System.out.println(STR. "\{ Thread.currentThread() } Response: " + response);
        } catch (Exception e) {
            System.err.println(STR. "\{ Thread.currentThread() } Exception: " + e);
        }
    }
    static String handle() throws Exception {
        try (var scope = new StructuredTaskScope.ShutdownOnFailure()) {
            StructuredTaskScope.Subtask<String> user = scope.fork((Example3::findUser));
            StructuredTaskScope.Subtask<Integer> order = scope.fork(Example3::fetchOrder);
            scope.join().throwIfFailed();
            String theUser = user.get();
            int theOrder = order.get();
            return STR. "\{ theUser } has order: \{ theOrder }" ;
        }
    }
}
