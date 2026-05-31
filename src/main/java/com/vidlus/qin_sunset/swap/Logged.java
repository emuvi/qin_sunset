package com.vidlus.qin_sunset.swap;

import com.vidlus.jarch.data.Data;

public class Logged implements Data {

    public String token;
    public String lang;

    public Logged() {
    }

    public Logged(String token, String lang) {
        this.token = token;
        this.lang = lang;
    }

    @Override
    public Logged clone() {
        return (Logged) this.deepClone();
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

    public static Logged fromChars(String chars) {
        return Data.fromChars(chars, Logged.class);
    }

}
