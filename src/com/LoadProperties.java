package com;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

public class LoadProperties {

    String fileName = "";

    public Properties load(String fileName) throws IOException {
        this.fileName = fileName;
        Properties properties = new Properties();
        try (FileInputStream input = new FileInputStream(fileName)) {
            properties.load(input);
            return properties;
        }
    }

    public void save(Properties properties){
        try {
            FileOutputStream output = new FileOutputStream(fileName);
            properties.store(output, "Dernière mise à jour");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
