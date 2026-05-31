package com.vidlus.qin_sunset.core;

import java.io.File;
import java.util.Objects;

import com.vidlus.jarch.data.Data;

public class Allow implements Data {

    public AllowApp allowApp;
    public AllowDir allowDir;
    public AllowCmd allowCmd;
    public AllowBas allowBas;
    public AllowReg allowReg;
    public AllowGiz allowGiz;

    public Allow() {
    }

    public Allow(AllowApp allowApp, AllowDir allowDir, AllowCmd allowCmd, AllowBas allowBas, AllowReg allowReg, AllowGiz allowGiz) {
        this.allowApp = allowApp;
        this.allowDir = allowDir;
        this.allowCmd = allowCmd;
        this.allowBas = allowBas;
        this.allowReg = allowReg;
        this.allowGiz = allowGiz;
    }

    public boolean isOnSameResource(Allow than) {
        if (this.allowApp != null && than.allowApp != null) {
            return Objects.equals(this.allowApp.name, than.allowApp.name);
        }
        if (this.allowDir != null && than.allowDir != null) {
            return Objects.equals(this.allowDir.path, than.allowDir.path);
        }
        if (this.allowCmd != null && than.allowCmd != null) {
            return Objects.equals(this.allowCmd.name, than.allowCmd.name);
        }
        if (this.allowBas != null && than.allowBas != null) {
            return Objects.equals(this.allowBas.name, than.allowBas.name);
        }
        if (this.allowReg != null && than.allowReg != null) {
            return Objects.equals(this.allowReg.registry, than.allowReg.registry);
        }
        if (this.allowGiz != null && than.allowGiz != null) {
            return Objects.equals(this.allowGiz.path, than.allowGiz.path);
        }
        return false;
    }

    @Override
    public void fixDefaults() {
        fixAllowAppDefaults();
        fixAllowDirDefaults();
        fixAllowCmdDefaults();
        fixAllowBasDefaults();
        fixAllowRegDefaults();
        fixAllowGizDefaults();
    }

    @Override
    public Allow clone() {
        return (Allow) this.deepClone();
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

    public static Allow fromChars(String chars) {
        return Data.fromChars(chars, Allow.class);
    }

    private void fixAllowAppDefaults() {
        if (this.allowApp != null) {
            if (this.allowApp.name == null || this.allowApp.name.isEmpty()) {
                this.allowApp = null;
            }
        }
    }

    private void fixAllowDirDefaults() {
        if (this.allowDir != null) {
            if (this.allowDir.path == null || this.allowDir.path.isEmpty()) {
                this.allowDir = null;
            } else {
                this.allowDir.path = new File(this.allowDir.path).getAbsolutePath();
                this.allowDir.mutate = this.allowDir.mutate != null ? this.allowDir.mutate : false;
            }
        }
    }

    private void fixAllowCmdDefaults() {
        if (this.allowCmd != null) {
            if (this.allowCmd.name == null || this.allowCmd.name.isEmpty()) {
                this.allowCmd = null;
            }
        }
    }

    private void fixAllowBasDefaults() {
        if (this.allowBas != null) {
            if (this.allowBas.name == null || this.allowBas.name.isEmpty()) {
                this.allowBas = null;
            }
        }
    }

    private void fixAllowRegDefaults() {
        if (this.allowReg != null) {
            if (this.allowReg.registry == null) {
                this.allowReg = null;
            } else {
                this.allowReg.all = this.allowReg.all != null ? this.allowReg.all : false;
                this.allowReg.insert = this.allowReg.insert != null ? this.allowReg.insert : false;
                this.allowReg.select = this.allowReg.select != null ? this.allowReg.select : false;
                this.allowReg.update = this.allowReg.update != null ? this.allowReg.update : false;
                this.allowReg.delete = this.allowReg.delete != null ? this.allowReg.delete : false;
            }
        }
    }

    private void fixAllowGizDefaults() {
        if (this.allowGiz != null && (this.allowGiz.path == null || this.allowGiz.path.isEmpty())) {
            this.allowGiz = null;
        }
    }
    
}
