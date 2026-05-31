package com.vidlus.qin_sunset.core;

import java.util.List;

import com.vidlus.jarch.data.Data;

public class AllowCmd implements Data {

    public String name;
    public List<String> argList;

    public AllowCmd() {
    }

    public AllowCmd(String name, List<String> argList) {
        this.name = name;
        this.argList = argList;
    }

    @Override
    public AllowCmd clone() {
        return (AllowCmd) this.deepClone();
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

    public static AllowCmd fromChars(String chars) {
        return Data.fromChars(chars, AllowCmd.class);
    }

}
