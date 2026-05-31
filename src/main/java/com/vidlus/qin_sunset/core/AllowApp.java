package com.vidlus.qin_sunset.core;

import com.vidlus.jarch.data.Data;

public class AllowApp implements Data {
    
    public String name;

    public AllowApp() {
    }

    public AllowApp(String name) {
        this.name = name;
    }

    @Override
    public AllowApp clone() {
        return (AllowApp) this.deepClone();
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

    public static AllowApp fromChars(String chars) {
        return Data.fromChars(chars, AllowApp.class);
    }

}
