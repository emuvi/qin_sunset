package com.vidlus.qin_sunset.core;

import com.vidlus.jarch.data.Data;

public class AllowDir implements Data {

    public String path;
    public Boolean mutate;

    public AllowDir() {
    }

    public AllowDir(String path, Boolean mutate) {
        this.path = path;
        this.mutate = mutate;
    }

    @Override
    public AllowDir clone() {
        return (AllowDir) this.deepClone();
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

    public static AllowDir fromChars(String chars) {
        return Data.fromChars(chars, AllowDir.class);
    }
    
}
