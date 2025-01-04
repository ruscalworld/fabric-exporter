package ru.ruscalworld.fabricexporter.config;

import ru.ruscalworld.fabricexporter.util.ConvertUtil;

import java.util.Properties;

public class MainConfig extends Config {
    private int port;
    private int updateInterval;
    private boolean useSpark;
    private boolean exportJvmDefaults;
    private boolean stripIdentifierNamespaces;

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

        this.setShouldExportJvmDefaults(properties.getProperty("export-default-jvm-metrics", "true").equalsIgnoreCase("true"));
        this.setShouldStripIdentifierNamespaces(properties.getProperty("strip-identifier-namespaces", "true").equalsIgnoreCase("true"));
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

    public boolean shouldExportJvmDefaults() {
        return exportJvmDefaults;
    }

    public void setShouldExportJvmDefaults(boolean exportJvmDefaults) {
        this.exportJvmDefaults = exportJvmDefaults;
    }

    public boolean shouldStripIdentifierNamespaces() {
        return stripIdentifierNamespaces;
    }

    public void setShouldStripIdentifierNamespaces(boolean stripIdentifierNamespaces) {
        this.stripIdentifierNamespaces = stripIdentifierNamespaces;
    }
}
