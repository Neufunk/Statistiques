package tools;

public class Formatter {

    static public String formatString(String string) {
        return string.replace("_", " ");
    }

    static public String formatDouble(double value) {
        if (value - Math.floor(value) == 0) {
            return String.format("%,.0f", value);
        }
        return String.format("%,." + 2 + "f", value);
    }
}
