package objects;

import java.util.HashMap;
import java.util.Map;

public class Properties {
    private Map<String, String> props = new HashMap<>();

    public void setProperty(String key, String value) {
        props.put(key, value);
    }

    public String getProperty(String key) {
        return props.getOrDefault(key, null);
    }
}
