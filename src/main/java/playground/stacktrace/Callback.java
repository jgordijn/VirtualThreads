package playground.stacktrace;

import java.util.concurrent.CompletableFuture;

public class Callback {

    private static CompletableFuture<String> saveMyName(String name) {
        System.out.println(STR. "Start \{ name }" );
        return CompletableFuture.supplyAsync(() -> {
            try {
                Thread.sleep(1000);
                return STR. "Hello from thread \{ Thread.currentThread() }" ;
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public static void main(String[] args) {
        saveMyName("John Doe").whenComplete((result, throwable) -> {
            if (throwable != null) {
                System.out.println(STR. "Exception: \{ throwable }" );
            } else {
                System.out.println(STR. "Result: \{ result }" );
            }
        });
    }
}
