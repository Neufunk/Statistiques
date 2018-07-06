package main;

import java.io.IOException;
import java.util.Properties;

public class Identification {

    public enum info {
        D03_URL, D03_USER, D03_PASSWD,
        D015_URL, D015_USER, D015_PASSWD,
        D615_URL, D615_USER, D615_PASSWD
    }

    public String set(info info) {
        Properties prop = getPropertiesFile();
        String answer;
        switch (info) {
            case D03_URL:
                answer = prop.getProperty("D03_URL", "vide");
                break;
            case D03_USER:
                answer = prop.getProperty("D03_USER", "vide");
                break;
            case D03_PASSWD:
                answer = prop.getProperty("D03_PASSWD", "vide");
                break;
            case D015_URL:
                answer = prop.getProperty("D015_URL", "vide");
                break;
            case D015_USER:
                answer = prop.getProperty("D015_USER", "vide");
                break;
            case D015_PASSWD:
                answer = prop.getProperty("D015_PASSWD", "vide");
                break;
            case D615_URL:
                answer = prop.getProperty("D615_URL", "vide");
                break;
            case D615_USER:
                answer = prop.getProperty("D615_USER", "vide");
                break;
            case D615_PASSWD:
                answer = prop.getProperty("D615_PASSWD", "vide");
                break;
            default:
                answer = null;
        }
        return answer;
    }

    private Properties getPropertiesFile() {
        Properties prop = new Properties();
        try {
            prop.load(getClass().getResourceAsStream("/properties/Identification.properties"));
        } catch (IOException e) {
            ExceptionHandler.switchException(e, this.getClass());
        }
        return prop;
    }

}
