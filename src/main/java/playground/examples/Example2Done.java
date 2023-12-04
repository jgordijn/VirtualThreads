package playground.examples;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;

public class Example2Done {
    public static void main(String[] args) throws Exception {
        try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
            var time = executor.submit(Example2::getTime);
            var date = executor.submit(Example2::getDate);
            System.out.println(STR. "It is \{ date.get() } at time \{ time.get() }" );
        }
    }
}
