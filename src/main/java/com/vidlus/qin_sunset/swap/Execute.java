package com.vidlus.qin_sunset.swap;

import java.util.List;

import com.vidlus.jarch.data.Data;

public class Execute implements Data {

    public String name;
    public List<String> argList;
    public List<String> inputList;
    public Boolean joinErrs;
    public Integer logLevel;

    public Execute(String name) {
        this(name, null, null, null, null);
    }

    public Execute(String name, List<String> argList) {
        this(name, argList, null, null, null);
    }

    public Execute(String name, List<String> argList, List<String> inputList) {
        this(name, argList, inputList, null, null);
    }

    public Execute(String name, List<String> argList, List<String> inputList, Boolean joinErrs) {
        this(name, argList, inputList, joinErrs, null);
    }

    public Execute(String name, List<String> argList, List<String> inputList, Boolean joinErrs, Integer logLevel) {
        this.name = name;
        this.argList = argList;
        this.inputList = inputList;
        this.joinErrs = joinErrs;
        this.logLevel = logLevel;
    }

    @Override
    public Execute clone() {
        return (Execute) this.deepClone();
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

    public static Execute fromChars(String chars) {
        return Data.fromChars(chars, Execute.class);
    }

}
