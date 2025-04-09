package br.com.pointel.qin_sunset.core;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import com.google.gson.Gson;

public class Setup {

    public String serverName;
    public String serverLang;
    public String serverHost;
    public Integer serverPort;
    public String serverFolder;
    public Boolean servesPub;
    public Boolean servesApp;
    public Boolean servesDir;
    public Boolean servesCmd;
    public Boolean servesBas;
    public Boolean servesReg;
    public Boolean servesGiz;

    public Map<String, String> configMap;
    public Map<String, String> redirectMap;

    public Integer threadsMin;
    public Integer threadsMax;
    public Integer threadsIdleTimeout;
    public Long cleanInterval;
    public Long tokenValidity;

    public void fixDefaults() {
        if (this.serverName == null || this.serverName.isEmpty()) {
            this.serverName = "QinSunset";
        }
        if (this.serverLang == null || this.serverLang.isEmpty()) {
            this.serverLang = "en";
        }
        if (this.serverHost == null || this.serverHost.isEmpty()) {
            this.serverHost = "localhost";
        }
        if (this.serverPort == null) {
            this.serverPort = 5490;
        }
        if (this.serverFolder == null) {
            this.serverFolder = "";
        }
        this.serverFolder = new File(this.serverFolder).getAbsolutePath();
        if (this.servesPub == null) {
            this.servesPub = false;
        }
        if (this.servesApp == null) {
            this.servesApp = false;
        }
        if (this.servesDir == null) {
            this.servesDir = false;
        }
        if (this.servesCmd == null) {
            this.servesCmd = false;
        }
        if (this.servesBas == null) {
            this.servesBas = false;
        }
        if (this.servesReg == null) {
            this.servesReg = false;
        }
        if (this.servesGiz == null) {
            this.servesGiz = false;
        }

        if (this.configMap == null) {
            this.configMap = new HashMap<>();
        }
        if (this.redirectMap == null) {
            this.redirectMap = new HashMap<>();
        }

        if (this.threadsMin == null) {
            this.threadsMin = 10;
        }
        if (this.threadsMax == null) {
            this.threadsMax = 100;
        }
        if (this.threadsIdleTimeout == null) {
            this.threadsIdleTimeout = 120;
        }
        if (this.cleanInterval == null) {
            this.cleanInterval = 12 * 60 * 60 * 1000L;
        }
        if (this.tokenValidity == null) {
            this.tokenValidity = 24 * 60 * 60 * 1000L;
        }
    }

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }

    public static Setup fromString(String source) {
        return new Gson().fromJson(source, Setup.class);
    }

}
