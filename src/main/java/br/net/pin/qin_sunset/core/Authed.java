package br.net.pin.qin_sunset.core;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import br.com.pointel.jarch.data.Deed;
import br.com.pointel.jarch.data.Pair;
import br.com.pointel.jarch.data.Registier;
import br.com.pointel.jarch.data.Strain;

public class Authed {
    private final User user;
    private final Group group;
    private final GizMap gizMap;
    private final IssuedMap issuedMap;
    private final List<Allow> access;

    public Authed(User user, Group group, Way way) {
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
            if (access.app != null && access.app.name.equals(name)) {
                return true;
            }
        }
        if (this.group != null) {
            for (var access : this.group.access) {
                if (access.app != null && access.app.name.equals(name)) {
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
            if (access.dir != null && fullPath.startsWith(access.dir.path)) {
                if (toMutate) {
                    if (access.dir.mutate) {
                        return true;
                    }
                } else {
                    return true;
                }
            }
        }
        if (this.group != null) {
            for (var access : this.group.access) {
                if (access.dir != null && fullPath.startsWith(access.dir.path)) {
                    if (toMutate) {
                        if (access.dir.mutate) {
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
            if (access.cmd != null && access.cmd.name.equals(name)) {
                return true;
            }
        }
        if (this.group != null) {
            for (var access : this.group.access) {
                if (access.cmd != null && access.cmd.name.equals(name)) {
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
            if (access.bas != null && access.bas.name.equals(name)) {
                if (toMutate) {
                    if (access.bas.mutate) {
                        return true;
                    }
                } else {
                    return true;
                }
            }
        }
        if (this.group != null) {
            for (var access : this.group.access) {
                if (access.bas != null && access.bas.name.equals(name)) {
                    if (toMutate) {
                        if (access.bas.mutate) {
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

    public Pair<Boolean, Strain> allowREG(Registier registier, Deed deed) {
        Pair<Boolean, Strain> result = new Pair<>(false, null);
        if (!this.allowBAS(registier.base, deed.mutates)) {
            return result;
        }
        if (this.isMaster()) {
            result.head = true;
        }
        for (var allow : this.getAccess()) {
            if (allow.reg != null && allow.reg.registier != null) {
                if (canAllowResource(allow.reg.registier, registier)) {
                    if (allow.reg.all != null && allow.reg.all) {
                        result.head = true;
                    }
                    switch (deed) {
                        case INSERT:
                            if (allow.reg.insert != null && allow.reg.insert) {
                                result.head = true;
                            }
                            break;
                        case SELECT:
                            if (allow.reg.select != null && allow.reg.select) {
                                result.head = true;
                            }
                            break;
                        case UPDATE:
                            if (allow.reg.update != null && allow.reg.update) {
                                result.head = true;
                            }
                            break;
                        case DELETE:
                            if (allow.reg.delete != null && allow.reg.delete) {
                                result.head = true;
                            }
                            break;
                    }
                    result.tail = allow.reg.strain;
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
            if (access.giz != null && access.giz.path.equals(path)) {
                return true;
            }
        }
        if (this.group != null) {
            for (var access : this.group.access) {
                if (access.giz != null && access.giz.path.equals(path)) {
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean canAllowResource(Registier guarantor, Registier requester) {
        if (guarantor.registry != null && requester.registry != null
                        && Objects.equals(guarantor.registry.name,
                                        requester.registry.name)) {
            if (checkWeighted(guarantor.base, requester.base) &&
                            checkWeighted(guarantor.registry.catalog,
                                            requester.registry.catalog) &&
                            checkWeighted(guarantor.registry.schema,
                                            requester.registry.schema)) {
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
