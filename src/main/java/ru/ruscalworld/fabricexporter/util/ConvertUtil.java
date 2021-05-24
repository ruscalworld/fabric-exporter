package ru.ruscalworld.fabricexporter.util;

public class ConvertUtil {
    public static int intToStringOrDefault(String input, int def) {
        int result = def;
        try {
            result = Integer.parseInt(input);
        } catch (Exception ignored) { }
        return result;
    }
}
