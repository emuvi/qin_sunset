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

    public Setup() {
    }

    public String getServerName() {
        return this.serverName;
    }

    public void setServerName(String serverName) {
        this.serverName = serverName;
    }

    public String getServerLang() {
        return this.serverLang;
    }

    public void setServerLang(String serverLang) {
        this.serverLang = serverLang;
    }

    public String getServerHost() {
        return this.serverHost;
    }

    public void setServerHost(String serverHost) {
        this.serverHost = serverHost;
    }

    public Integer getServerPort() {
        return this.serverPort;
    }

    public void setServerPort(Integer serverPort) {
        this.serverPort = serverPort;
    }

    public String getServerFolder() {
        return this.serverFolder;
    }

    public void setServerFolder(String serverFolder) {
        this.serverFolder = serverFolder;
    }

    public Boolean isServesPub() {
        return this.servesPub;
    }

    public Boolean getServesPub() {
        return this.servesPub;
    }

    public void setServesPub(Boolean servesPub) {
        this.servesPub = servesPub;
    }

    public Boolean isServesApp() {
        return this.servesApp;
    }

    public Boolean getServesApp() {
        return this.servesApp;
    }

    public void setServesApp(Boolean servesApp) {
        this.servesApp = servesApp;
    }

    public Boolean isServesDir() {
        return this.servesDir;
    }

    public Boolean getServesDir() {
        return this.servesDir;
    }

    public void setServesDir(Boolean servesDir) {
        this.servesDir = servesDir;
    }

    public Boolean isServesCmd() {
        return this.servesCmd;
    }

    public Boolean getServesCmd() {
        return this.servesCmd;
    }

    public void setServesCmd(Boolean servesCmd) {
        this.servesCmd = servesCmd;
    }

    public Boolean isServesBas() {
        return this.servesBas;
    }

    public Boolean getServesBas() {
        return this.servesBas;
    }

    public void setServesBas(Boolean servesBas) {
        this.servesBas = servesBas;
    }

    public Boolean isServesReg() {
        return this.servesReg;
    }

    public Boolean getServesReg() {
        return this.servesReg;
    }

    public void setServesReg(Boolean servesReg) {
        this.servesReg = servesReg;
    }

    public Boolean isServesGiz() {
        return this.servesGiz;
    }

    public Boolean getServesGiz() {
        return this.servesGiz;
    }

    public void setServesGiz(Boolean servesGiz) {
        this.servesGiz = servesGiz;
    }

    public Map<String,String> getConfigMap() {
        return this.configMap;
    }

    public void setConfigMap(Map<String,String> configMap) {
        this.configMap = configMap;
    }

    public Map<String,String> getRedirectMap() {
        return this.redirectMap;
    }

    public void setRedirectMap(Map<String,String> redirectMap) {
        this.redirectMap = redirectMap;
    }

    public Integer getThreadsMin() {
        return this.threadsMin;
    }

    public void setThreadsMin(Integer threadsMin) {
        this.threadsMin = threadsMin;
    }

    public Integer getThreadsMax() {
        return this.threadsMax;
    }

    public void setThreadsMax(Integer threadsMax) {
        this.threadsMax = threadsMax;
    }

    public Integer getThreadsIdleTimeout() {
        return this.threadsIdleTimeout;
    }

    public void setThreadsIdleTimeout(Integer threadsIdleTimeout) {
        this.threadsIdleTimeout = threadsIdleTimeout;
    }

    public Long getCleanInterval() {
        return this.cleanInterval;
    }

    public void setCleanInterval(Long cleanInterval) {
        this.cleanInterval = cleanInterval;
    }

    public Long getTokenValidity() {
        return this.tokenValidity;
    }

    public void setTokenValidity(Long tokenValidity) {
        this.tokenValidity = tokenValidity;
    }

    public Setup serverName(String serverName) {
        setServerName(serverName);
        return this;
    }

    public Setup serverLang(String serverLang) {
        setServerLang(serverLang);
        return this;
    }

    public Setup serverHost(String serverHost) {
        setServerHost(serverHost);
        return this;
    }

    public Setup serverPort(Integer serverPort) {
        setServerPort(serverPort);
        return this;
    }

    public Setup serverFolder(String serverFolder) {
        setServerFolder(serverFolder);
        return this;
    }

    public Setup servesPub(Boolean servesPub) {
        setServesPub(servesPub);
        return this;
    }

    public Setup servesApp(Boolean servesApp) {
        setServesApp(servesApp);
        return this;
    }

    public Setup servesDir(Boolean servesDir) {
        setServesDir(servesDir);
        return this;
    }

    public Setup servesCmd(Boolean servesCmd) {
        setServesCmd(servesCmd);
        return this;
    }

    public Setup servesBas(Boolean servesBas) {
        setServesBas(servesBas);
        return this;
    }

    public Setup servesReg(Boolean servesReg) {
        setServesReg(servesReg);
        return this;
    }

    public Setup servesGiz(Boolean servesGiz) {
        setServesGiz(servesGiz);
        return this;
    }

    public Setup configMap(Map<String,String> configMap) {
        setConfigMap(configMap);
        return this;
    }

    public Setup redirectMap(Map<String,String> redirectMap) {
        setRedirectMap(redirectMap);
        return this;
    }

    public Setup threadsMin(Integer threadsMin) {
        setThreadsMin(threadsMin);
        return this;
    }

    public Setup threadsMax(Integer threadsMax) {
        setThreadsMax(threadsMax);
        return this;
    }

    public Setup threadsIdleTimeout(Integer threadsIdleTimeout) {
        setThreadsIdleTimeout(threadsIdleTimeout);
        return this;
    }

    public Setup cleanInterval(Long cleanInterval) {
        setCleanInterval(cleanInterval);
        return this;
    }

    public Setup tokenValidity(Long tokenValidity) {
        setTokenValidity(tokenValidity);
        return this;
    }

    public Setup fixDefaults() {
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
        return this;
    }

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }

    public static Setup fromString(String source) {
        return new Gson().fromJson(source, Setup.class);
    }
}
