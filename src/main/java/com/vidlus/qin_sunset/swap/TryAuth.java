package com.vidlus.qin_sunset.swap;

import com.vidlus.jarch.data.Data;

public class TryAuth implements Data {

    public String name;
    public String pass;

    public TryAuth() {
    }
    
    public TryAuth(String name, String pass) {
        this.name = name;
        this.pass = pass;
    }

    @Override
    public TryAuth clone() {
        return (TryAuth) this.deepClone();
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

    public static TryAuth fromChars(String chars) {
        return Data.fromChars(chars, TryAuth.class);
    }
    
}
