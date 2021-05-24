package ru.ruscalworld.fabricexporter.config;

import ru.ruscalworld.fabricexporter.util.ConvertUtil;

import java.util.Properties;

public class MainConfig extends Config {
    private int port;
    private int updateInterval;

    public MainConfig(String name) {
        super(name);
    }

    @Override
    public void parse(Properties properties) {
        String portString = properties.getProperty("server-port", "25585");
        this.setPort(ConvertUtil.intToStringOrDefault(portString, 25585));

        String updateIntervalString = properties.getProperty("update-interval", "1000");
        this.setUpdateInterval(ConvertUtil.intToStringOrDefault(updateIntervalString, 1000));
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public int getUpdateInterval() {
        return updateInterval;
    }

    public void setUpdateInterval(int updateInterval) {
        this.updateInterval = updateInterval;
    }
}
