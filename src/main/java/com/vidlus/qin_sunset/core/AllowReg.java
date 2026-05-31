package com.vidlus.qin_sunset.core;

import com.vidlus.jarch.data.Data;
import com.vidlus.jarch.data.Registry;
import com.vidlus.jarch.data.Strain;

public class AllowReg implements Data {
    
    public Registry registry;
    public Boolean all;
    public Boolean insert;
    public Boolean select;
    public Boolean update;
    public Boolean delete;
    public Strain strain;

    public AllowReg() {
    }

    public AllowReg(Registry registry, Boolean all) {
        this.registry = registry;
        this.all = all;
    }

    public AllowReg(Registry registry, Boolean all, Strain strain) {
        this.registry = registry;
        this.all = all;
        this.strain = strain;
    }

    public AllowReg(Registry registry, Boolean all, Boolean insert, Boolean select, Boolean update, Boolean delete, Strain strain) {
        this.registry = registry;
        this.all = all;
        this.insert = insert;
        this.select = select;
        this.update = update;
        this.delete = delete;
        this.strain = strain;
    }

    @Override
    public AllowReg clone() {
        return (AllowReg) this.deepClone();
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

    public static AllowReg fromChars(String chars) {
        return Data.fromChars(chars, AllowReg.class);
    }

}
