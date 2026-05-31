package com.vidlus.qin_sunset.core;

import java.io.File;
import java.nio.file.Files;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vidlus.jarch.data.Data;
import com.vidlus.jarch.data.DataListArray;
import com.vidlus.jarch.flow.FixBool;
import com.vidlus.jarch.flow.FixObject;

public class Group implements Data {

    private static Logger log = LoggerFactory.getLogger(Group.class);

    public String name;
    public String home;
    public String lang;
    @FixBool(false)
    public Boolean master;

    @FixObject(value = "[]", type = DataListArray.class)
    public DataListArray<Allow> allowList;
    public Map<String, String> configMap;

    public Group() {}

    public Group(String name, String home, String lang, Boolean master, DataListArray<Allow> allowList, Map<String, String> configMap) {
        this.name = name;
        this.home = home;
        this.lang = lang;
        this.master = master;
        this.allowList = allowList;
        this.configMap = configMap;
    }

    public void fixDefaults() {
        if (this.home.isEmpty()) {
            this.home = "dir/" + this.name;
        }
        var homeDir = new File(this.home);
        this.home = homeDir.getAbsolutePath();
        try {
            Files.createDirectories(homeDir.toPath());
        } catch (Exception e) {
            log.error("Error creating group home directory on: " + this.home, e);
        }
    }

    @Override
    public Group clone() {
        return (Group) this.deepClone();
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

    public static User fromChars(String chars) {
        return Data.fromChars(chars, User.class);
    }
    
}
