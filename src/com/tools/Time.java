package tools;

import java.util.Calendar;

public class Time {

    private static double startTime;

    public static void setStartTime() {
        startTime = System.currentTimeMillis();
    }

    public static int computeElapsed() {
        return (int) (System.currentTimeMillis() - startTime) / 1000;
    }

    public static String getCurrentTime() {
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int min = calendar.get(Calendar.MINUTE);
        int seconds = calendar.get(Calendar.SECOND);
        return String.format("%02d:%02d:%02d", hour, min, seconds);
    }
}
