package tools;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;

public class Date {

    public static int[] getYearList() {
        int[] yearList = new int[5];
        int yearCount = -3;
        for(int i = 0; i < yearList.length; i++){
            yearList[i] = getCurrentYearInt() + yearCount;
            yearCount++;
        }
        return yearList;
    }

    static String getCurrentDate() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDate localDate = LocalDate.now();
        return dtf.format(localDate);
    }

    public static String getCurrentYearStr() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy");
        LocalDate localDate = LocalDate.now();
        return dtf.format(localDate);
    }

    public static int getCurrentYearInt() {
        return Calendar.getInstance().get(Calendar.YEAR);
    }
}
