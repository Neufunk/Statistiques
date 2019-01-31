package main;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Version {

    public static final String versionNumber = "1.4.1";

    static String getCurrentDate() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDate localDate = LocalDate.now();
        return dtf.format(localDate);
    }
}
