package ru.ruscalworld.fabricexporter.config;

import net.fabricmc.loader.api.FabricLoader;
import org.apache.logging.log4j.LogManager;
import ru.ruscalworld.fabricexporter.FabricExporter;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;

public abstract class Config {
    private final String fileName;

    public Config(String fileName) {
        this.fileName = fileName;
    }

    public void load() throws IOException {
        Path path = FabricLoader.getInstance().getConfigDir().resolve(this.getFileName());
        if (!path.toFile().exists()) try {
            URL url = this.getClass().getClassLoader().getResource("config/" + this.getFileName());
            assert url != null;
            File file = new File(url.toURI());
            byte[] bytes = Files.readAllBytes(file.toPath());

            Files.createFile(path);
            Files.write(path, bytes);
        } catch (Exception exception) {
            exception.printStackTrace();
            FabricExporter.getLogger().fatal("Unable to save default config");
        }

        Properties properties = new Properties();
        properties.load(Files.newInputStream(path));
        this.parse(properties);
    }

    public abstract void parse(Properties properties);

    public String getFileName() {
        return fileName;
    }
}
