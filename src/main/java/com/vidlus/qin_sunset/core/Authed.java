package com.vidlus.qin_sunset.core;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import com.vidlus.jarch.data.Deeds;
import com.vidlus.jarch.data.Registry;

public class Authed {

    private final User user;
    private final Group group;
    private final GizMap gizMap;
    private final IssuedMap issuedMap;
    private final List<Allow> allowList;

    public Authed(User user, Group group, WayToRun wayToRun) {
        this.user = user;
        this.group = group;
        this.gizMap = new GizMap(wayToRun);
        this.issuedMap = new IssuedMap();
        this.allowList = new ArrayList<>();
        this.initAllowList();
    }

    private void initAllowList() {
        if (this.group != null && this.group.allowList != null) {
            for (var allowFromGroup : this.group.allowList) {
                this.allowList.add(allowFromGroup);
            }
        }
        if (this.user != null && this.user.allowList != null) {
            for (var allowFromUser : this.user.allowList) {
                this.allowList.removeIf(onGroup -> onGroup.isOnSameResource(allowFromUser));
                this.allowList.add(allowFromUser);
            }
        }
    }

    public String getUserName() {
        return user.name;
    }

    public String getHome() {
        if (!this.user.home.isEmpty()) {
            return this.user.home;
        } else if (this.group != null) {
            return this.group.home;
        } else {
            return "";
        }
    }

    public String getLang() {
        if (!this.user.lang.isEmpty()) {
            return this.user.lang;
        } else if (this.group != null) {
            return this.group.lang;
        } else {
            return "";
        }
    }

    public boolean isMaster() {
        return Boolean.TRUE.equals(this.user.master) || (this.group != null && Boolean.TRUE.equals(this.group.master));
    }

    public List<Allow> getAllowList() {
        return this.allowList;
    }

    public boolean isAllowedApp(String appName) {
        if (isMaster()) {
            return true;
        }
        for (var allow : this.allowList) {
            if (allow.allowApp != null && Objects.equals(allow.allowApp.name, appName)) {
                return true;
            }
        }
        return false;
    }

    public boolean isAllowedDir(String fullPath, boolean toMutate) {
        if (this.isMaster()) {
            return true;
        }
        for (var allow : this.allowList) {
            if (allow.allowDir != null && fullPath.startsWith(allow.allowDir.path)) {
                if (toMutate) {
                    if (Boolean.TRUE.equals(allow.allowDir.mutate)) {
                        return true;
                    }
                } else {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean isAllowedCmd(String cmdName) {
        if (isMaster()) {
            return true;
        }
        for (var allow : this.allowList) {
            if (allow.allowCmd != null && Objects.equals(allow.allowCmd.name, cmdName)) {
                return true;
            }
        }
        return false;
    }

    public boolean isAllowedBas(String baseName, boolean toMutate) {
        if (this.isMaster()) {
            return true;
        }
        for (var allow : this.allowList) {
            if (allow.allowBas != null && Objects.equals(allow.allowBas.name, baseName)) {
                if (toMutate) {
                    if (Boolean.TRUE.equals(allow.allowBas.mutate)) {
                        return true;
                    }
                } else {
                    return true;
                }
            }
        }
        return false;
    }

    public AllowedReg isAllowedReg(Registry registry, Deeds deeds) {
        var result = new AllowedReg(false, null);
        if (!this.isAllowedBas(registry.base, deeds.mutates)) {
            return result;
        }
        if (this.isMaster()) {
            result.allowed = true;
            return result;
        }
        for (var allow : this.allowList) {
            if (allow.allowReg != null && allow.allowReg.registry != null
                            && canAllowResource(allow.allowReg.registry, registry)) {
                if (allow.allowReg.all != null && allow.allowReg.all) {
                    result.allowed = true;
                }
                switch (deeds) {
                    case Insert:
                        if (allow.allowReg.insert != null && allow.allowReg.insert) {
                            result.allowed = true;
                        }
                        break;
                    case Select:
                        if (allow.allowReg.select != null && allow.allowReg.select) {
                            result.allowed = true;
                        }
                        break;
                    case Update:
                        if (allow.allowReg.update != null && allow.allowReg.update) {
                            result.allowed = true;
                        }
                        break;
                    case Delete:
                        if (allow.allowReg.delete != null && allow.allowReg.delete) {
                            result.allowed = true;
                        }
                        break;
                }
                result.strained = allow.allowReg.strain;
            }
        }
        return result;
    }

    public boolean isAllowedGiz(String path) {
        if (isMaster()) {
            return true;
        }
        for (var allow : this.allowList) {
            if (allow.allowGiz != null && Objects.equals(allow.allowGiz.path, path)) {
                return true;
            }
        }
        return false;
    }

    public static boolean canAllowResource(Registry guarantor, Registry requester) {
        return guarantor.tableHead != null && requester.tableHead != null
                        && Objects.equals(guarantor.tableHead.name, requester.tableHead.name)
                        && checkWeighted(guarantor.base, requester.base)
                        && checkWeighted(guarantor.tableHead.catalog, requester.tableHead.catalog)
                        && checkWeighted(guarantor.tableHead.schema, requester.tableHead.schema);
    }

    public static boolean checkWeighted(String strong, String weak) {
        if (strong == null || strong.isEmpty()) {
            return true;
        }
        return strong.equals(weak);
    }

    public String getConfig(String name) {
        if (this.user.configMap.containsKey(name)) {
            return this.user.configMap.get(name);
        }
        if (this.group != null && this.group.configMap.containsKey(name)) {
            return this.group.configMap.get(name);
        }
        return null;
    }

    public GizMap getGizMap() {
        return this.gizMap;
    }

    public String newIssued(Issued issued) {
        return this.issuedMap.newIssued(issued);
    }

    public Issued getIssued(String token) {
        return this.issuedMap.get(token);
    }

    public void addIssued(String token, Issued issued) {
        this.issuedMap.put(token, issued);
    }

    public void delIssued(String token) {
        this.issuedMap.remove(token);
    }

}
