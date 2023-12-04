package playground.examples;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.Executors;

public class Example2 {
    public static void main(String[] args) throws Exception {

        // Create 2 threads that will perform the heavy tasks of getting the date and time and combine them

    }

    public static String getTime() throws Exception {
        if(true) throw new RuntimeException("Something went wrong");
        Thread.sleep(100);
        return LocalTime.now().toString();
    }

    public static String getDate() throws Exception {
        Thread.sleep(3000);
        return LocalDate.now().format(DateTimeFormatter.ofLocalizedDate(java.time.format.FormatStyle.FULL));
    }
}
