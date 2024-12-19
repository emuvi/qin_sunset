package br.com.pointel.qin_sunset.core;

import java.io.File;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import com.google.gson.Gson;

public class User {

    public String name;
    public String pass;
    public String home;
    public String lang;
    public Boolean master;
    public List<Allow> allowList;
    public Map<String, String> configMap;
    public String group;

    public User() {}

    public User(String name, String pass, String home, String lang, Boolean master, List<Allow> allowList,
                    Map<String, String> configMap, String group) {
        this.name = name;
        this.pass = pass;
        this.home = home;
        this.lang = lang;
        this.master = master;
        this.allowList = allowList;
        this.configMap = configMap;
        this.group = group;
    }

    public void fixDefaults() {
        if (this.name == null) {
            this.name = "";
        }
        if (this.pass == null) {
            this.pass = "";
        }
        if (this.home == null) {
            this.home = "";
        }
        if (this.home.isEmpty()) {
            this.home = "dir/" + this.name;
        }
        var homeDir = new File(this.home);
        this.home = homeDir.getAbsolutePath();
        try {
            Files.createDirectories(homeDir.toPath());
        } catch (Exception e) {
            System.err.println("Error creating user home directory on: " + this.home
                            + " why: " + e.getMessage());
        }
        if (this.lang == null) {
            this.lang = "";
        }
        if (this.master == null) {
            this.master = false;
        }
        if (this.allowList == null) {
            this.allowList = new ArrayList<>();
        }
        for (var allow : this.allowList) {
            allow.fixDefaults();
        }
        if (this.configMap == null) {
            this.configMap = new HashMap<>();
        }
        if (this.group == null) {
            this.group = "";
        }
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof User)) {
            return false;
        }
        User user = (User) o;
        return Objects.equals(name, user.name)
                        && Objects.equals(pass, user.pass)
                        && Objects.equals(home, user.home)
                        && Objects.equals(lang, user.lang)
                        && Objects.equals(master, user.master)
                        && Objects.equals(allowList, user.allowList)
                        && Objects.equals(configMap, user.configMap)
                        && Objects.equals(group, user.group);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, pass, home, lang, master, allowList, configMap, group);
    }

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }

    public static User fromString(String json) {
        return new Gson().fromJson(json, User.class);
    }
}
