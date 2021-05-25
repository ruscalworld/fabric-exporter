package ru.ruscalworld.fabricexporter.config;

import net.fabricmc.loader.api.FabricLoader;
import ru.ruscalworld.fabricexporter.FabricExporter;
import ru.ruscalworld.fabricexporter.util.FileUtil;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Properties;

public abstract class Config {
    private final String fileName;
    private Properties properties;

    public Config(String fileName) {
        this.fileName = fileName;
    }

    public void load() throws IOException {
        Path path = FabricLoader.getInstance().getConfigDir().resolve(this.getFileName());
        if (!path.toFile().exists()) try {
            InputStream stream = this.getClass().getClassLoader().getResourceAsStream("config/" + this.getFileName());
            List<String> lines = FileUtil.getLinesFromStream(stream);

            Files.createFile(path);
            Files.write(path, lines);
        } catch (Exception exception) {
            exception.printStackTrace();
            FabricExporter.getLogger().fatal("Unable to save default config");
        }

        Properties properties = new Properties();
        properties.load(Files.newInputStream(path));
        this.setProperties(properties);
        this.onLoad();
    }

    public abstract void onLoad();

    public String getFileName() {
        return fileName;
    }

    public Properties getProperties() {
        return properties;
    }

    public void setProperties(Properties properties) {
        this.properties = properties;
    }
}
