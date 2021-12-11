package ru.ruscalworld.fabricexporter.config;

import ru.ruscalworld.fabricexporter.util.ConvertUtil;

import java.util.Properties;

public class MainConfig extends Config {
    private int port;
    private int updateInterval;
    private boolean useSpark;
    private String instanceName;

    public MainConfig(String name) {
        super(name);
    }

    @Override
    public void onLoad() {
        Properties properties = this.getProperties();

        String portString = properties.getProperty("server-port", "25585");
        this.setPort(ConvertUtil.intToStringOrDefault(portString, 25585));

        String updateIntervalString = properties.getProperty("update-interval", "1000");
        this.setUpdateInterval(ConvertUtil.intToStringOrDefault(updateIntervalString, 1000));

        this.setShouldUseSpark(properties.getProperty("use-spark", "true").equalsIgnoreCase("true"));
        this.setInstanceName(properties.getProperty("instance-name", "default"));
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

    public boolean shouldUseSpark() {
        return useSpark;
    }

    public void setShouldUseSpark(boolean useSpark) {
        this.useSpark = useSpark;
    }

    public String getInstanceName() {
        return instanceName;
    }

    public void setInstanceName(String instanceName) {
        this.instanceName = instanceName;
    }
}
