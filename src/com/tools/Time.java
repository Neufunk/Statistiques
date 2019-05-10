package tools;

public class Time {

    private static double startTime;

    public static void setStartTime() {
        startTime = System.currentTimeMillis();
    }

    public static int computeElapsed() {
        return (int) (System.currentTimeMillis() - startTime) / 1000;
    }
}
