package com.example.recordskeeper.address;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Config {

        public static String getProperty(String key) throws IOException {
            Properties prop = new Properties();
            InputStream inputStream = new FileInputStream("address/src/main/res/config.properties");
            prop.load(inputStream);
            inputStream.close();

            String value = prop.getProperty(key);
            return value;
        }

}