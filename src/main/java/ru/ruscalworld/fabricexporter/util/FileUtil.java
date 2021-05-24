package ru.ruscalworld.fabricexporter.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class FileUtil {
    /**
     * Reads stream into a list of lines
     * @param stream Input stream
     * @return List of lines
     */
    public static List<String> getLinesFromStream(InputStream stream) throws IOException {
        List<String> result = new ArrayList<>();

        InputStreamReader streamReader = new InputStreamReader(stream, StandardCharsets.UTF_8);
        BufferedReader reader = new BufferedReader(streamReader);

        String line;
        while ((line = reader.readLine()) != null) result.add(line);

        return result;
    }
}
