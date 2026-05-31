package com.vidlus.qin_sunset.core;

import com.vidlus.jarch.data.Data;

public class AllowBas implements Data {

    public String name;
    public Boolean mutate;

    public AllowBas() {
    }

    public AllowBas(String name, Boolean mutate) {
        this.name = name;
        this.mutate = mutate;
    }

    @Override
    public AllowBas clone() {
        return (AllowBas) this.deepClone();
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

    public static AllowBas fromChars(String chars) {
        return Data.fromChars(chars, AllowBas.class);
    }

}
