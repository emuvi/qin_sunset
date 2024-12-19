package br.com.pointel.qin_sunset.core;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import br.com.pointel.jarch.data.CrudDeeds;
import br.com.pointel.jarch.data.Registry;

public class Authed {
    private final User user;
    private final Group group;
    private final GizMap gizMap;
    private final IssuedMap issuedMap;
    private final List<Allow> access;

    public Authed(User user, Group group, WayToRun way) {
        this.user = user;
        this.group = group;
        this.gizMap = new GizMap(way);
        this.issuedMap = new IssuedMap();
        this.access = new ArrayList<>();
        this.initAccess();
    }

    private void initAccess() {
        if (this.group != null) {
            for (var allow : this.group.allowList) {
                this.access.add(allow);
            }
        }
        if (this.user.allowList != null) {
            for (var allow : this.user.allowList) {
                this.access.removeIf(onGroup -> onGroup.isOnSameResource(allow));
                this.access.add(allow);
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

    public Boolean isMaster() {
        if (this.user.master) {
            return true;
        } else if (this.group != null) {
            return this.group.master;
        } else {
            return false;
        }
    }

    public List<Allow> getAccess() {
        return this.access;
    }

    public boolean allowAPP(String name) {
        if (isMaster()) {
            return true;
        }
        for (var allow : this.user.allowList) {
            if (allow.allowApp != null && allow.allowApp.name.equals(name)) {
                return true;
            }
        }
        if (this.group != null) {
            for (var allow : this.group.allowList) {
                if (allow.allowApp != null && allow.allowApp.name.equals(name)) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean allowDIR(String fullPath, boolean toMutate) {
        if (this.isMaster()) {
            return true;
        }
        for (var allow : this.user.allowList) {
            if (allow.allowDir != null && fullPath.startsWith(allow.allowDir.path)) {
                if (toMutate) {
                    if (allow.allowDir.mutate) {
                        return true;
                    }
                } else {
                    return true;
                }
            }
        }
        if (this.group != null) {
            for (var allow : this.group.allowList) {
                if (allow.allowDir != null && fullPath.startsWith(allow.allowDir.path)) {
                    if (toMutate) {
                        if (allow.allowDir.mutate) {
                            return true;
                        }
                    } else {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public boolean allowCMD(String name) {
        if (isMaster()) {
            return true;
        }
        for (var allow : this.user.allowList) {
            if (allow.allowCmd != null && allow.allowCmd.name.equals(name)) {
                return true;
            }
        }
        if (this.group != null) {
            for (var allow : this.group.allowList) {
                if (allow.allowCmd != null && allow.allowCmd.name.equals(name)) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean allowBAS(String name, boolean toMutate) {
        if (this.isMaster()) {
            return true;
        }
        for (var allow : this.user.allowList) {
            if (allow.allowBas != null && allow.allowBas.name.equals(name)) {
                if (toMutate) {
                    if (allow.allowBas.mutate) {
                        return true;
                    }
                } else {
                    return true;
                }
            }
        }
        if (this.group != null) {
            for (var allow : this.group.allowList) {
                if (allow.allowBas != null && allow.allowBas.name.equals(name)) {
                    if (toMutate) {
                        if (allow.allowBas.mutate) {
                            return true;
                        }
                    } else {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public AllowedReg allowREG(Registry registry, CrudDeeds deed) {
        var result = new AllowedReg(false, null);
        if (!this.allowBAS(registry.base, deed.mutates)) {
            return result;
        }
        if (this.isMaster()) {
            result.allowed = true;
        }
        for (var allow : this.getAccess()) {
            if (allow.allowReg != null && allow.allowReg.registry != null) {
                if (canAllowResource(allow.allowReg.registry, registry)) {
                    if (allow.allowReg.all != null && allow.allowReg.all) {
                        result.allowed = true;
                    }
                    switch (deed) {
                        case INSERT:
                            if (allow.allowReg.insert != null && allow.allowReg.insert) {
                                result.allowed = true;
                            }
                            break;
                        case SELECT:
                            if (allow.allowReg.select != null && allow.allowReg.select) {
                                result.allowed = true;
                            }
                            break;
                        case UPDATE:
                            if (allow.allowReg.update != null && allow.allowReg.update) {
                                result.allowed = true;
                            }
                            break;
                        case DELETE:
                            if (allow.allowReg.delete != null && allow.allowReg.delete) {
                                result.allowed = true;
                            }
                            break;
                    }
                    result.strained = allow.allowReg.strain;
                }
            }
        }
        return result;
    }

    public boolean allowGIZ(String path) {
        if (isMaster()) {
            return true;
        }
        for (var allow : this.user.allowList) {
            if (allow.allowGiz != null && allow.allowGiz.path.equals(path)) {
                return true;
            }
        }
        if (this.group != null) {
            for (var allow : this.group.allowList) {
                if (allow.allowGiz != null && allow.allowGiz.path.equals(path)) {
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean canAllowResource(Registry guarantor, Registry requester) {
        if (guarantor.tableHead != null && requester.tableHead != null
                        && Objects.equals(guarantor.tableHead.name,
                                        requester.tableHead.name)) {
            if (checkWeighted(guarantor.base, requester.base) &&
                            checkWeighted(guarantor.tableHead.catalog,
                                            requester.tableHead.catalog) &&
                            checkWeighted(guarantor.tableHead.schema,
                                            requester.tableHead.schema)) {
                return true;
            }
        }
        return false;
    }

    public static boolean checkWeighted(String strong, String weak) {
        if (strong == null || strong.isEmpty()) {
            return true;
        }
        return strong.equals(weak);
    }

    public String getParam(String name) {
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
