package br.com.pointel.qin_sunset.core;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import br.com.pointel.jarch.data.DeedsCrud;
import br.com.pointel.jarch.data.Pair;
import br.com.pointel.jarch.data.Registry;
import br.com.pointel.jarch.data.Strain;

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
            for (var group_allow : this.group.access) {
                this.access.add(group_allow);
            }
        }
        if (this.user.access != null) {
            for (var user_allow : this.user.access) {
                this.access.removeIf(on_group -> on_group.isOnSameResource(user_allow));
                this.access.add(user_allow);
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
        for (var access : this.user.access) {
            if (access.allowApp != null && access.allowApp.name.equals(name)) {
                return true;
            }
        }
        if (this.group != null) {
            for (var access : this.group.access) {
                if (access.allowApp != null && access.allowApp.name.equals(name)) {
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
        for (var access : this.user.access) {
            if (access.allowDir != null && fullPath.startsWith(access.allowDir.path)) {
                if (toMutate) {
                    if (access.allowDir.mutate) {
                        return true;
                    }
                } else {
                    return true;
                }
            }
        }
        if (this.group != null) {
            for (var access : this.group.access) {
                if (access.allowDir != null && fullPath.startsWith(access.allowDir.path)) {
                    if (toMutate) {
                        if (access.allowDir.mutate) {
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
        for (var access : this.user.access) {
            if (access.allowCmd != null && access.allowCmd.name.equals(name)) {
                return true;
            }
        }
        if (this.group != null) {
            for (var access : this.group.access) {
                if (access.allowCmd != null && access.allowCmd.name.equals(name)) {
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
        for (var access : this.user.access) {
            if (access.allowBas != null && access.allowBas.name.equals(name)) {
                if (toMutate) {
                    if (access.allowBas.mutate) {
                        return true;
                    }
                } else {
                    return true;
                }
            }
        }
        if (this.group != null) {
            for (var access : this.group.access) {
                if (access.allowBas != null && access.allowBas.name.equals(name)) {
                    if (toMutate) {
                        if (access.allowBas.mutate) {
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

    public Pair<Boolean, Strain> allowREG(Registry registry, DeedsCrud deed) {
        Pair<Boolean, Strain> result = new Pair<>(false, null);
        if (!this.allowBAS(registry.base, deed.mutates)) {
            return result;
        }
        if (this.isMaster()) {
            result.head = true;
        }
        for (var allow : this.getAccess()) {
            if (allow.allowReg != null && allow.allowReg.registry != null) {
                if (canAllowResource(allow.allowReg.registry, registry)) {
                    if (allow.allowReg.all != null && allow.allowReg.all) {
                        result.head = true;
                    }
                    switch (deed) {
                        case INSERT:
                            if (allow.allowReg.insert != null && allow.allowReg.insert) {
                                result.head = true;
                            }
                            break;
                        case SELECT:
                            if (allow.allowReg.select != null && allow.allowReg.select) {
                                result.head = true;
                            }
                            break;
                        case UPDATE:
                            if (allow.allowReg.update != null && allow.allowReg.update) {
                                result.head = true;
                            }
                            break;
                        case DELETE:
                            if (allow.allowReg.delete != null && allow.allowReg.delete) {
                                result.head = true;
                            }
                            break;
                    }
                    result.tail = allow.allowReg.strain;
                }
            }
        }
        return result;
    }

    public boolean allowGIZ(String path) {
        if (isMaster()) {
            return true;
        }
        for (var access : this.user.access) {
            if (access.allowGiz != null && access.allowGiz.path.equals(path)) {
                return true;
            }
        }
        if (this.group != null) {
            for (var access : this.group.access) {
                if (access.allowGiz != null && access.allowGiz.path.equals(path)) {
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean canAllowResource(Registry guarantor, Registry requester) {
        if (guarantor.head != null && requester.head != null
                        && Objects.equals(guarantor.head.name,
                                        requester.head.name)) {
            if (checkWeighted(guarantor.base, requester.base) &&
                            checkWeighted(guarantor.head.catalog,
                                            requester.head.catalog) &&
                            checkWeighted(guarantor.head.schema,
                                            requester.head.schema)) {
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
        if (this.user.params.containsKey(name)) {
            return this.user.params.get(name);
        }
        if (this.group != null && this.group.params.containsKey(name)) {
            return this.group.params.get(name);
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
