package com.vidlus.qin_sunset.core;

import java.io.File;
import java.util.Map;

import com.google.gson.Gson;

import com.vidlus.jarch.data.Data;
import com.vidlus.jarch.flow.FixBool;
import com.vidlus.jarch.flow.FixChars;
import com.vidlus.jarch.flow.FixInt;
import com.vidlus.jarch.flow.FixLong;
import com.vidlus.jarch.flow.FixObject;

public class Setup implements Data {
    
    @FixChars("QinSunset")
    public String serverName;
    @FixChars("en")
    public String serverLang;
    @FixChars("localhost")
    public String serverHost;
    @FixInt(5490)
    public Integer serverPort;
    @FixChars("")
    public String serverFolder;
    
    @FixBool(false)
    public Boolean servesPub;
    @FixBool(false)
    public Boolean servesApp;
    @FixBool(false)
    public Boolean servesDir;
    @FixBool(false)
    public Boolean servesCmd;
    @FixBool(false) 
    public Boolean servesBas;
    @FixBool(false)
    public Boolean servesReg;
    @FixBool(false)
    public Boolean servesGiz;

    @FixObject(value = "{}", type = Map.class)
    public Map<String, String> configMap;
    @FixObject(value = "{}", type = Map.class)
    public Map<String, String> redirectMap;

    @FixInt(10)
    public Integer threadsMin;
    @FixInt(100)
    public Integer threadsMax;
    @FixInt(120)
    public Integer threadsIdleTimeout;
    @FixLong(12 * 60 * 60 * 1000L)
    public Long cleanInterval;
    @FixLong(24 * 60 * 60 * 1000L)
    public Long tokenValidity;

    @Override
    public void fixDefaults() throws Exception{
        this.serverFolder = new File(this.serverFolder).getAbsolutePath();
    }

    @Override
    public Setup clone() {
        return (Setup) this.deepClone();
    }

    @Override
    public boolean equals(Object that) {
        return this.deepEquals(that);
    }

    @Override
    public int hashCode() {
        return this.deepHash();
    }

    @Override
    public String toString() {
        return this.toChars();
    }

    public static Setup fromChars(String chars) {
        return Data.fromChars(chars, Setup.class);
    }
    
}
