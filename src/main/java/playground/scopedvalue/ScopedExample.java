package playground.scopedvalue;

import java.util.NoSuchElementException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.StructuredTaskScope;

import static java.util.FormatProcessor.FMT;

public class ScopedExample {
    private final static ScopedValue<String> CONTEXT = ScopedValue.newInstance();

    public static void main(String[] args) throws Exception {
        ScopedValue.runWhere(CONTEXT, "TestValue", () -> {
            showContext("Parent");
            Thread.startVirtualThread(() -> showContext("Unbound VT"));
            try (var scope = new StructuredTaskScope.ShutdownOnFailure()) {
                scope.fork(() -> {
                    showContext("  |-> Scoped VT");
                    return null;
                });
                scope.join().throwIfFailed();
            } catch (ExecutionException | InterruptedException e) {
                throw new RuntimeException(e);
            }

            ScopedValue.runWhere(CONTEXT, "Another Value", ScopedExample::nesting);
        });
        try {
            showContext("Outside scope");
        } catch (NoSuchElementException e) {
            System.out.println("CONTEXT not set outside scope");
        }
    }

    private static void nesting() {
        showContext("  \\-> Nesting");
        ScopedValue.runWhere(CONTEXT, "Yet another value", () -> ScopedExample.showContext("     \\-> SubNesting"));
        showContext(" \\-> Nesting after subNesting");
    }

    private static void showContext(String source) {
        try {
            System.out.println(FMT. "%-30s\{ source }: \{ CONTEXT.get() }" );
        } catch (NoSuchElementException e) {
            System.err.println(FMT. "%-15s\{ source }: NOT SET." );
        }
    }
}
