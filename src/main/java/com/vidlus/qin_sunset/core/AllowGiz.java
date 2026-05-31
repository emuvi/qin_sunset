package com.vidlus.qin_sunset.core;

import com.vidlus.jarch.data.Data;

public class AllowGiz implements Data {

    public String path;

    public AllowGiz() {
    }

    public AllowGiz(String path) {
        this.path = path;
    }

    @Override
    public AllowGiz clone() {
        return (AllowGiz) this.deepClone();
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

    public static AllowGiz fromChars(String chars) {
        return Data.fromChars(chars, AllowGiz.class);
    }

}
