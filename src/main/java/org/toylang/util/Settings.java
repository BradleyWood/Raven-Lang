package org.toylang.util;

import java.util.HashMap;

public class Settings {

    private static final HashMap<String, Object> SETTINGS = new HashMap<>();

    public static Object get(final String key) {
        return SETTINGS.get(key);
    }

    public static int getInt(final String key) {
        return (int) SETTINGS.get(key);
    }

    /**
     * Get a boolean setting, returns false if the setting does not exist
     * @param key The key
     * @return The setting or false if the setting does not exist
     */
    public static boolean getBoolean(final String key) {
        return (boolean) SETTINGS.getOrDefault(key, false);
    }

    public static String getString(final String key) {
        return (String) SETTINGS.get(key);
    }

    public static void set(String key, Object value) {
        SETTINGS.put(key, value);
    }

}
