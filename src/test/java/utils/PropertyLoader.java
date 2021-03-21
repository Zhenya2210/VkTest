package utils;


import lombok.SneakyThrows;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.Properties;

public class PropertyLoader {

    private static final String PROPERTIES_FILE = "/data.properties";
    private static final Properties PROPERTIES = getPropertiesInstance();

    private PropertyLoader() {

    }

    public static String loadProperty(String propertyName) {
        String value = null;
        value = PROPERTIES.getProperty(propertyName);
        if (null == value) {
            throw new IllegalArgumentException("В файле data.properties не найдено значение по ключу: " + propertyName);
        }
        return value;
    }

    @SneakyThrows(IOException.class)
    private static Properties getPropertiesInstance() {
        Properties instance = new Properties();
        try (
            InputStream resourceStream = PropertyLoader.class.getResourceAsStream(PROPERTIES_FILE);
            InputStreamReader inputStream = new InputStreamReader(resourceStream, Charset.forName("UTF-8"))
        ) {
            instance.load(inputStream);
        }
        return instance;
    }

}
