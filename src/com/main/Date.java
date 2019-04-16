package main;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Date {

    static String getCurrentDate() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDate localDate = LocalDate.now();
        return dtf.format(localDate);
    }

    public static String getCurrentYear() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy");
        LocalDate localDate = LocalDate.now();
        return dtf.format(localDate);
    }
}
