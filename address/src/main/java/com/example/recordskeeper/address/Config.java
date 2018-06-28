package com.example.recordskeeper.address;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * <h1>Config class usage</h1>
 * Config file is being loaded and then this class is called by other classes to access config file.
 * <h2>Libraries</h2>
 * Import these java libraries first to get started with the functionality.
 * <p><code>import java.io.FileInputStream;<br>
 * import java.io.IOException;<br>
 * import java.io.InputStream;<br>
 * import java.util.Properties;</code></p>
 * <h2>Create Connection</h2>
 * <p><code> Properties prop = new Properties(); <br>
 * InputStream inputStream = new FileInputStream("address/src/main/res/config.properties"); <br>
 * prop.load(inputStream); <br>
 * inputStream.close();</code></p>
 */

public class Config {

    /**
     * @param key It carries the actual keys and information
     * @return It will return the string value.
     * @throws IOException
     */

        public static String getProperty(String key) throws IOException {
            Properties prop = new Properties();
            InputStream inputStream = new FileInputStream("address/src/main/res/config.properties");
            prop.load(inputStream);
            inputStream.close();

            String value = prop.getProperty(key);
            return value;
        }

}