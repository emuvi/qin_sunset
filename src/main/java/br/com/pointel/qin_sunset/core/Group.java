package br.com.pointel.qin_sunset.core;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import com.google.gson.Gson;

public class Group {
    public String name;
    public String home;
    public String lang;
    public Boolean master;
    public List<Allow> allowList;
    public Map<String, String> configMap;

    public Group() {}

    public Group(String name, String home, String lang, Boolean master, List<Allow> allowList,
                    Map<String, String> configMap) {
        this.name = name;
        this.home = home;
        this.lang = lang;
        this.master = master;
        this.allowList = allowList;
        this.configMap = configMap;
    }

    public void fixDefaults() {
        if (this.name == null) {
            this.name = "";
        }
        if (this.home == null) {
            this.home = "";
        }
        if (this.home.isEmpty()) {
            this.home = "dir/" + this.name;
        }
        this.home = new File(this.home).getAbsolutePath();
        if (this.lang == null) {
            this.lang = "";
        }
        if (this.master == null) {
            this.master = false;
        }
        if (this.allowList == null) {
            this.allowList = new ArrayList<>();
        }
        for (var access : this.allowList) {
            access.fixDefaults();
        }
        if (this.configMap == null) {
            this.configMap = new HashMap<>();
        }
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof Group)) {
            return false;
        }
        Group group = (Group) o;
        return Objects.equals(name, group.name)
                        && Objects.equals(home, group.home)
                        && Objects.equals(lang, group.lang)
                        && Objects.equals(master, group.master)
                        && Objects.equals(allowList, group.allowList)
                        && Objects.equals(configMap, group.configMap);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, home, lang, master, allowList, configMap);
    }

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }

    public static User fromString(String json) {
        return new Gson().fromJson(json, User.class);
    }
}
